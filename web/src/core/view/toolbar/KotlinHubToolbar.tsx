import {
    AppBar,
    Box,
    Menu,
    Toolbar,
    Typography,
    MenuItem,
    IconButton,
    SvgIcon, SxProps, Stack
} from "@mui/material";
import KotlinHubLogoIcon from "../icon/KotlinHubLogoIcon";
import React, {useState} from "react";
import {Link as NavLink, useNavigate} from "react-router-dom";
import {useAppDispatch} from "../../../app/hooks";
import {useAuthState} from "../../../features/auth/AuthHooks";
import {logoutThunk} from "../../../features/auth/AuthSlice";
import {Add as AddIcon} from "@mui/icons-material";
import {UserAvatar} from "../avatar/UserAvatar";
import {User} from "../../../features/auth/User";

export interface KotlinHubToolbarProps {
    user?: User,
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
    const authState = useAuthState();
    return (
        <KotlinHubToolbar user={authState.user}/>
    );
}

export function KotlinHubToolbar({user}: KotlinHubToolbarProps) {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const isLoggedIn = user !== undefined;

    const handleOnSignOutClick = () => {
        dispatch(logoutThunk());
        navigate("/");
    }

    const settingsMenu = [
        <MenuItem key={"Settings"}>
            <Typography
                component={NavLink}
                to={"/settings"}
                sx={primaryLinkStyle}
            >
                Settings
            </Typography>
        </MenuItem>
        ,
        <MenuItem key={"Sign out"} onClick={handleOnSignOutClick}>
            <Typography
                sx={primaryLinkStyle}
            >
                Sign out
            </Typography>
        </MenuItem>
        ,
    ];

    return (
        <AppBar position={"static"}>
            <Toolbar sx={{
                p: 0.5,
            }}>
                <SvgIcon
                    inheritViewBox
                    component={NavLink}
                    to={"/"}
                    sx={{
                        width: 34,
                        height: 50,
                    }}
                >
                    <KotlinHubLogoIcon/>
                </SvgIcon>
                {isLoggedIn &&
                    <Stack direction={"row"} sx={{marginLeft: "auto"}}>
                        <IconButton
                            component={NavLink}
                            to={"/repositories/create"}
                        >
                            <AddIcon/>
                        </IconButton>
                        <AvatarMenu settingsMenu={settingsMenu} username={user.username}/>
                    </Stack>
                }
            </Toolbar>
        </AppBar>
    )
}

interface AvatarMenuProps {
    settingsMenu: React.ReactElement[],
    username: string,
}

function AvatarMenu({settingsMenu, username}: AvatarMenuProps) {
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return (
        <Box>
            <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                <UserAvatar name={username}/>
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
