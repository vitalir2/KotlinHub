import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import KotlinHubLogoIcon from "../icon/KotlinHubLogoIcon";

export function KotlinHubToolbar() {
    return (
        <AppBar position={"static"}>
            <Toolbar sx={{}}>
                <KotlinHubLogoIcon viewbox={"0 0 34 50"}/>
                <Typography variant={"h6"}>
                    Sign in
                </Typography>
            </Toolbar>
        </AppBar>
    )
}
