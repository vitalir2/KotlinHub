import {Stack, Typography} from "@mui/material";
import SyntaxHighlighter from "react-syntax-highlighter";

export interface RepositoryFileContentProps {
    fileName: string,
    fileContent: string,
}

export function RepositoryFileContent({fileName, fileContent}: RepositoryFileContentProps) {
    const fileExtensionSeparatorIndex = fileName.lastIndexOf(".");
    let fileExtension: string;
    if (fileExtensionSeparatorIndex > 0) {
        fileExtension = fileName.substring(fileExtensionSeparatorIndex + 1);
    } else {
        fileExtension = "";
    }

    let contentLanguage: string;
    switch (fileExtension) {
        case "js":
        case "jsx":
            contentLanguage = "javascript";
            break;
        case "json":
            contentLanguage = "json";
            fileContent = JSON.stringify(fileContent, null, 2);
            break;
        case "ts":
        case "tsx":
            contentLanguage = "typescript";
            break;
        case "kt":
        case "kts":
            contentLanguage = "kotlin";
            break;
        case "html":
            contentLanguage = "html"
            break;
        case "yml":
            contentLanguage = "yml";
            break;
        case "sh":
            contentLanguage = "bash";
            break;
        default:
            contentLanguage = "text";
            break;
    }

    const numberOfLines = fileContent.split('\n').length;
    let linesText: string
    switch (numberOfLines) {
        case 0:
            linesText = "";
            break;
        case 1:
            linesText = "1 line";
            break;
        default:
            linesText = `${numberOfLines} lines`;
            break;
    }

    return (
        <Stack direction={"column"} spacing={0} sx={{
            borderColor: "grey.400",
            borderWidth: "1px",
            borderStyle: "solid",
            borderRadius: "4px",
        }}>
            <Stack direction={"row"} sx={{
                backgroundColor: "grey.100",
                padding: "0.5em",
                borderBottom: "solid 1px grey",
                borderBottomColor: "grey.400",
            }}>
                <Typography variant={"caption"}>
                    {linesText}
                </Typography>
            </Stack>
            <SyntaxHighlighter
                language={contentLanguage}
                showLineNumbers={true}
                customStyle={{
                    margin: 0,
                }}
            >
                {fileContent}
            </SyntaxHighlighter>
        </Stack>
    );
}
