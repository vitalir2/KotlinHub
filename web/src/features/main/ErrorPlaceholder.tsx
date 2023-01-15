import {Typography} from "@mui/material";

interface ErrorPlaceholderProps {
    text: string,
}
export function ErrorPlaceholder(props: ErrorPlaceholderProps) {
    const { text } = props

    return (
        <Typography variant={"body1"}>
            {text}
        </Typography>
    )
}
