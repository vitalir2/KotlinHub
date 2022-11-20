/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.vitalir.kotlinhub.server.app.infrastructure.docs

import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.ClientOptInput
import io.swagger.codegen.v3.ClientOpts
import io.swagger.codegen.v3.CodegenConfig
import io.swagger.codegen.v3.DefaultGenerator
import io.swagger.codegen.v3.Generator
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.parser.core.models.ParseOptions
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Creates a `get` endpoint at [path] with documentation rendered from the OpenAPI file.
 *
 * This method tries to lookup [swaggerFile] in the resources first, and if it's not found, it will try to read it from
 * the file system using [java.io.File].
 *
 * The documentation is generated using [StaticHtml2Codegen] by default. It can be customized using config in [block].
 * See [OpenAPIConfig] for more details.
 */
public fun Routing.openAPI(
    path: String,
    swaggerFile: String = "openapi.json",
    block: OpenAPIConfig.() -> Unit = {}
) {
    val resource = application.environment.classLoader.getResource(swaggerFile)
    val file = if (resource != null) File(resource.toURI()) else File(swaggerFile)

    val isSwaggerFromFile: Boolean = file.exists()

    val config = OpenAPIConfig()
    with(config) {
        val swaggerString = if (isSwaggerFromFile) {
            File(swaggerFile).readText()
        } else {
            val kompendiumSpec = application.attributes[KompendiumAttributes.openApiSpec]
            Json.encodeToString(kompendiumSpec)
        }
        val swagger = parser.readContents(swaggerString, null, options)

        opts.apply {
            config(codegen)
            opts(ClientOpts())
            openAPI(swagger.openAPI)
        }

        block(this)

        generator.opts(opts)
        generator.generate()

        static(path) {
            staticRootFolder = File("docs")
            files(".")
            default("index.html")
        }
    }
}

/**
 * Configuration for OpenAPI endpoint.
 */
public class OpenAPIConfig {
    /**
     * Specifies a parser used to parse OpenAPI.
     */
    public var parser: OpenAPIParser = OpenAPIParser()

    /**
     * Specifies options of the OpenAPI generator.
     */
    public var opts: ClientOptInput = ClientOptInput()

    /**
     * Specifies a generator used to generate OpenAPI.
     */
    public var generator: Generator = DefaultGenerator()

    /**
     * Specifies a code generator for [OpenAPIConfig].
     *
     * See also [StaticHtml2Codegen], [StaticHtmlCodegen] and etc.
     */
    public var codegen: CodegenConfig = StaticHtml2Codegen()

    /**
     * Provides access to options of the OpenAPI format parser.
     */
    public var options: ParseOptions = ParseOptions()
}
