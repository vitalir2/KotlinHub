import {Avatar, Box, CircularProgress, Stack, Typography} from "@mui/material";
import React, {Component, ReactElement} from "react";
import {User} from "../user/User";
import {Loadable} from "../../core/models/Loadable";
import {ProfileSidebar} from "./ProfileSidebar";

export interface LoadableProfileSidebarProps {
    loadableUser: Loadable<User>,
}

export function LoadableProfileSidebar(props: LoadableProfileSidebarProps) {
    const { loadableUser } = props

    let body: ReactElement
    switch (loadableUser.kind) {
        case "loading":
            body = <LoadingProfileSidebar/>
            break
        case "loaded":
            body = <ProfileSidebar user={loadableUser.data}/>
            break
        case "error":
            body =
                <Box>
                    <Typography variant={"h3"}>
                        Error: {loadableUser.error}
                    </Typography>
                </Box>

    }

    return (
        <Stack spacing={1} sx={{
            margin: 2,
            width: "10vw",
            height: "100vh",
        }}>
            {body}
        </Stack>
    )
}

function LoadingProfileSidebar() {
    return (
        <Box sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <CircularProgress/>
        </Box>
    )
}
