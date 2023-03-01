import {Box, CircularProgress} from "@mui/material";

export function LoadingPlaceholder() {
    return (
        <Box sx={{
            width: "100%",
            height: "100vh", // TODO fix to be 100% of free space
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <CircularProgress/>
        </Box>
    )
}
