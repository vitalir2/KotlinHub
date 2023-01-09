import {Box, Divider, Stack} from "@mui/material";
import {Repository} from "./Repository";
import React from "react";

export function Repositories() {
    const repositories: string[] = ["Hello"]
    return (
        <Stack
            spacing={1}
            sx={{
                width: "100%",
                height: "100%",
                padding: 3,
            }}
        >
            {repositories.map(repository =>
                <Box>
                    <Repository/>
                    <Divider/>
                </Box>
            )}
        </Stack>
    )
}
