import {Box, CircularProgress, Stack, SxProps, Typography} from "@mui/material";
import React, {ReactElement} from "react";
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
            body = <ProfileSidebarError error={loadableUser.error}/>
            break
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

const sidebarPlaceholderStyle: SxProps = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
}

function LoadingProfileSidebar() {
    return (
        <Box sx={sidebarPlaceholderStyle}>
            <CircularProgress/>
        </Box>
    )
}

interface ProfileSidebarErrorProps {
    error: string,
}

function ProfileSidebarError(props: ProfileSidebarErrorProps) {
    const {error} = props

    return (
        <Box sx={sidebarPlaceholderStyle}>
            <Typography variant={"h3"}>
                Error: {error}
            </Typography>
        </Box>
    )
}
