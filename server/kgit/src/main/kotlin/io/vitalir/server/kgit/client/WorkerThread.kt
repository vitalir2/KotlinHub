package io.vitalir.server.kgit.client

/**
 * An annotation implying that the annotated method runs on a worker thread
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class WorkerThread
