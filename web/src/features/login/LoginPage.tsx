import {
    Box,
    Button,
    Checkbox,
    Container,
    FormControlLabel,
    Link,
    Stack,
    SxProps,
    TextField, Theme,
    Typography
} from "@mui/material";
import React from "react";

export function LoginPage() {
    return (
        <Container maxWidth={"xs"} sx={{
            height: "100vh",
        }}>
            <Stack spacing={1} justifyContent={"center"}>
                <Typography variant={"h3"} textAlign={"center"}>
                    Login
                </Typography>
                <TextField
                    variant={"outlined"}
                    label={"Username or email"}
                />
                <TextField
                    variant={"outlined"}
                    label={"Password"}
                />
                <AdditionalOptions/>
                <Button variant={"contained"}>
                    Sign in
                </Button>
            </Stack>
            <RegisterNow sx={{
                marginTop: 8,
            }}/>
        </Container>
    )
}

function AdditionalOptions() {
    return (
        <Stack direction={"row"} justifyContent={"space-between"}>
            <FormControlLabel control={<Checkbox/>} label={"Remember me"}/>
            <Link href={"#"} underline={"none"} sx={{
                maxWidth: "xs",
                width: 100,
            }}>
                Forgot your password?
            </Link>
        </Stack>
    )
}

interface RegisterNowProps {
    sx?: SxProps<Theme>,
}

function RegisterNow(props: RegisterNowProps) {
    return (
        <Stack sx={props.sx}>
            <Typography variant={"body1"} sx={{
                textAlign: "center",
            }}>
                Don't have an account yet?
            </Typography>
            <Link variant={"body1"} underline={"none"} sx={{
                textAlign: "center",
            }}>
                Register now
            </Link>
        </Stack>
    )
}
