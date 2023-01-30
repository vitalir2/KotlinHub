import {Avatar, Stack, Typography} from "@mui/material";
import React from "react";
import {User} from "../user/User";

export interface ProfileSidebarProps {
    user: User
}

export function ProfileSidebar(props: ProfileSidebarProps) {
    const {user} = props

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
                {user.username}
            </Typography>
            <Typography variant={"body1"}>
                Description
            </Typography>
        </Stack>
    )
}
