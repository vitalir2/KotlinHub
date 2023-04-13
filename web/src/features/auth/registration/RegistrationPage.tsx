import React, {useState} from "react";
import {TextInputData} from "../../../core/models/TextInputData";
import {Container, Link, Stack, SxProps, Typography} from "@mui/material";
import {RegistrationForm} from "./RegistrationForm";

const containerStyle: SxProps = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    height: "85vh",
}

export function RegistrationPage() {
    const [username, setUsername] = useState<TextInputData>({
        value: "",
        errorMessage: undefined,
    });
    const [password, setPassword] = useState<TextInputData>({
        value: "",
        errorMessage: undefined,
    });

    return (
        <Container maxWidth={"xs"} sx={containerStyle}>
            <RegistrationForm
                username={username}
                setUsername={setUsername}
                password={password}
                setPassword={setPassword}
            />
            <LoginNow sx={{marginTop: 8}}/>
        </Container>
    );
}

interface LoginNowProps {
    sx?: SxProps,
}

function LoginNow({sx}: LoginNowProps) {
    return (
        <Stack sx={sx}>
            <Typography variant={"body1"} sx={{
                textAlign: "center",
            }}>
                Already have an account?
            </Typography>
            <Link href={"/"} variant={"body1"} underline={"none"} sx={{
                textAlign: "center",
            }}>
                Login now
            </Link>
        </Stack>
    );
}
