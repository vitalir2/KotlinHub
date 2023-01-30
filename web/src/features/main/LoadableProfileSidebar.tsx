import {Box, Typography} from "@mui/material";
import React from "react";
import {User} from "../user/User";
import {Loadable} from "../../core/models/Loadable";
import {ProfileSidebar} from "./ProfileSidebar";

export interface LoadableProfileSidebarProps {
    loadableUser: Loadable<User>,
}

export function LoadableProfileSidebar(props: LoadableProfileSidebarProps) {
    const { loadableUser } = props

    switch (loadableUser.kind) {
        case "loading":
            // TODO make better placeholder
            return (
                <Box>
                    <Typography variant={"h3"}>
                        User is loading
                    </Typography>
                </Box>
            )
        case "loaded":
            return (
                <ProfileSidebar user={loadableUser.data}/>
            )
        case "error":
            return (
                <Box>
                    <Typography variant={"h3"}>
                        Error: {loadableUser.error}
                    </Typography>
                </Box>
            )

    }
}
