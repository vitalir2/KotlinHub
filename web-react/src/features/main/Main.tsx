import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import React from "react";

export function Main() {
    return (
        <Stack direction={"row"} spacing={2}>
            <Profile/>
            <Repositories/>
        </Stack>
    )
}
