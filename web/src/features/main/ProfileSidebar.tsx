import {Avatar, Stack, Typography} from "@mui/material";
import React from "react";
import {getFullName, User} from "../user/User";

export interface ProfileSidebarProps {
    user: User
}

export function ProfileSidebar(props: ProfileSidebarProps) {
    const {user} = props
    const fullName = getFullName(user)

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
            {fullName &&
                <Typography variant={"subtitle1"}>
                    {fullName}
                </Typography>
            }
            {user.email &&
                <Typography variant={"subtitle1"}>
                    {user.email}
                </Typography>
            }
            {user.description &&
                <Typography variant={"body1"}>
                    {user.description}
                </Typography>
            }
        </Stack>
    )
}
