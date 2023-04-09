import {
    AppBar,
    Box,
    Menu,
    Toolbar,
    Typography,
    MenuItem,
    Avatar,
    IconButton,
    SvgIcon, SxProps
} from "@mui/material";
import KotlinHubLogoIcon from "../icon/KotlinHubLogoIcon";
import React, {useState} from "react";
import {Link as NavLink} from "react-router-dom";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {logout} from "../../../features/user/UserSlice";

export interface KotlinHubToolbarProps {
    isLoggedIn: boolean,
}

const primaryLinkStyle: SxProps = {
    ":link": {
        color: "text.primary",
        textDecoration: "none",
    },
    ":visited": {
        color: "text.primary",
    },
};

export function KotlinHubToolbarRedux() {
    const isLoggedIn = useAppSelector(state => state.user.user?.kind !== undefined);
    return (
        <KotlinHubToolbar
            isLoggedIn={isLoggedIn}
        />
    );
}

export function KotlinHubToolbar({isLoggedIn}: KotlinHubToolbarProps) {
    const dispatch = useAppDispatch();

    const handleOnSignOutClick = () => {
        dispatch(logout())
    }

    const settingsMenu = (
        <>
            <MenuItem key={"Settings"}>
                <Typography
                    component={NavLink}
                    to={"/settings"}
                    sx={primaryLinkStyle}
                >
                    Settings
                </Typography>
            </MenuItem>
            <MenuItem key={"Sign out"} onClick={handleOnSignOutClick}>
                <Typography
                    component={NavLink}
                    to={"/"}
                    sx={primaryLinkStyle}
                >
                    Sign out
                </Typography>
            </MenuItem>
        </>
    );

    return (
        <AppBar position={"static"}>
            <Toolbar sx={{
                p: 0.5,
            }}>
                <SvgIcon
                    inheritViewBox
                    component={NavLink}
                    to={"/main"}
                    sx={{
                        width: 34,
                        height: 50,
                    }}
                >
                    <KotlinHubLogoIcon/>
                </SvgIcon>
                {!isLoggedIn &&
                    <Typography
                        variant={"h6"}
                        component={NavLink}
                        to={"/"}
                        sx={{
                            ...primaryLinkStyle,
                            marginLeft: "auto",
                        }}
                    >
                        Sign in
                    </Typography>
                }
                {isLoggedIn && <AvatarMenu settingsMenu={settingsMenu}/>}
            </Toolbar>
        </AppBar>
    )
}

interface AvatarMenuProps {
    settingsMenu: React.ReactElement,
}

function AvatarMenu({settingsMenu}: AvatarMenuProps) {
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return (
        <Box sx={{marginLeft: "auto"}}>
            <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                <Avatar alt="User Avatar" src={"TODO"}/>
            </IconButton>
            <Menu
                sx={{mt: '45px'}}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}
            >
                {settingsMenu}
            </Menu>
        </Box>
    );
}
