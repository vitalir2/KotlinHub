import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import React, {useState} from "react";

export function Main() {
    const [repositories] = useState(["KotlinHub"])

    return (
        <Stack direction={"row"} spacing={2}>
            <Profile/>
            <Repositories repositories={repositories}/>
        </Stack>
    )
}
