import {Box, Typography} from "@mui/material";

export interface ErrorPlaceholderProps {
    error: string,
}

export function ErrorPlaceholder(props: ErrorPlaceholderProps) {
    return (
        <Box sx={{
            width: "100%",
            height: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <Typography variant={"h6"}>
                {props.error}
            </Typography>
        </Box>
    )
}
