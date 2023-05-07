import {Stack, Typography} from "@mui/material";
import React from "react";
import {getFullName, User} from "../auth/User";
import {UserAvatar} from "../../core/view/avatar/UserAvatar";

export interface ProfileSidebarProps {
    user: User
}

export function ProfileSidebar(props: ProfileSidebarProps) {
    const {user} = props
    const fullName = getFullName(user)

    return (
        <Stack spacing={1} sx={{
            width: "100%",
            height: "100%",
        }}>
            <UserAvatar name={user.username} sx={{
                width: "10vw",
                height: "10vw",
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
        </Stack>
    )
}
