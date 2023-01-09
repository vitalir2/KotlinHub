import {Box, Chip, Stack, Typography} from "@mui/material";
import React from "react";

export function Repository() {
    return (
        <Stack spacing={0.5}>
            <Stack spacing={1} direction={"row"}>
                <Typography variant={"h6"}>
                    KotlinHub
                </Typography>
                <Chip label={"Public"} variant={"outlined"} color={"secondary"} size={"small"}/>
            </Stack>
            <Box>
                <Typography variant={"body2"}>
                    Description
                </Typography>
            </Box>
            <Box>
                <Typography variant={"body2"}>
                    updated today
                </Typography>
            </Box>
        </Stack>
    )
}
