import {Avatar, Stack, Typography} from "@mui/material";
import React from "react";

export function Profile() {
    return (
        <Stack spacing={1} sx={{
            margin: 2,
        }}>
            <Avatar alt={"Account picture"} src={"#"} sx={{
                width: 75,
                height: 75,
                padding: 1,
            }}/>
            <Typography variant={"h5"}>
                Vitalir
            </Typography>
            <Typography variant={"body1"}>
                Description
            </Typography>
        </Stack>
    )
}
