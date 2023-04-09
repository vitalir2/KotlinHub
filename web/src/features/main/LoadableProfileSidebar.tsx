import {Box, CircularProgress, Stack, SxProps} from "@mui/material";
import React, {ReactElement} from "react";
import {User} from "../user/User";
import {ProfileSidebar} from "./ProfileSidebar";

export interface LoadableProfileSidebarProps {
    user?: User,
}

export function LoadableProfileSidebar({user}: LoadableProfileSidebarProps) {
    let body: ReactElement;
    if (user === undefined) {
        body = <LoadingProfileSidebar/>;
    } else {
        body = <ProfileSidebar user={user}/>;
    }

    return (
        <Stack spacing={1} sx={{
            m: 2,
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
