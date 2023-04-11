import {Stack, Typography} from "@mui/material";
import SyntaxHighlighter from "react-syntax-highlighter";
import {useAppSelector} from "../../../../app/hooks";
import {ErrorPlaceholder} from "../../../../core/view/placeholder/ErrorPlaceholder";

const createFirstAdvice = (
    repositoryName: string,
    repositoryUrl: string,
) => `echo "# ${repositoryName}" >> README.md
git init
git add README.md
git commit -m "First commit"
git remote add origin ${repositoryUrl}
git push -u origin master`;

const createSecondAdvice = (
    repositoryUrl: string,
) => `git remote add origin ${repositoryUrl}
git push -u origin master`;

export function EmptyRepositoryContent() {
    const repositoryState = useAppSelector(state => state.repository.repository);
    if (repositoryState.kind !== "loaded") {
        return (
            <ErrorPlaceholder error={"Something went wrong"}/>
        );
    }

    const repository = repositoryState.data;

    return (
        <PureEmptyRepositoryContent
            repositoryName={repository.name}
            repositoryUrl={repository.cloneUrl}
        />
    );
}

export interface PureEmptyRepositoryContentProps {
    repositoryName: string,
    repositoryUrl: string,
}

export function PureEmptyRepositoryContent({repositoryName, repositoryUrl}: PureEmptyRepositoryContentProps) {
    return (
        <Stack direction={"column"}>
            <Typography variant={"h6"}>Create a new repository on the command line</Typography>
            <SyntaxHighlighter language={"bash"}>
                {createFirstAdvice(repositoryName, repositoryUrl)}
            </SyntaxHighlighter>
            <Typography variant={"h6"}>...or push an existing repository from the command line</Typography>
            <SyntaxHighlighter language={"bash"}>
                {createSecondAdvice(repositoryUrl)}
            </SyntaxHighlighter>
        </Stack>
    );
}
