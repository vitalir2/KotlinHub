import {
    Checkbox, Container,
    FormControlLabel,
    Link,
    Stack,
    SxProps,
    TextField, Theme,
    Typography
} from "@mui/material";
import React from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {setLogin, setPassword, setRememberMe} from "./LoginSlice";
import {AppDispatch} from "../../app/store";
import {loginThunk} from "../auth/AuthSlice";
import {KotlinHubButton} from "../../core/view/button/KotlinHubButton";

const containerStyle: SxProps<Theme> = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    height: "85vh",
}

export function LoginPage() {
    return (
        <Container maxWidth={"xs"} sx={containerStyle}>
            <LoginForm/>
            <RegisterNow sx={{
                marginTop: 8,
            }}/>
        </Container>
    )
}

function LoginForm() {
    const state = useAppSelector(state => state.login)
    const dispatch = useAppDispatch()
    const isButtonEnabled = state.login.errorMessage === undefined && state.password.errorMessage === undefined &&
        state.login.value !== "" && state.password.value !== ""

    return (
        <Stack spacing={1}>
            <Typography variant={"h3"} textAlign={"center"}>
                Login
            </Typography>
            <TextField
                variant={"outlined"}
                label={"Username or email"}
                error={state.login.errorMessage !== undefined}
                helperText={state.login.errorMessage}
                onChange={event => dispatch(setLogin(event.target.value))}
            />
            <TextField
                variant={"outlined"}
                label={"Password"}
                type={"password"}
                error={state.password.errorMessage !== undefined}
                helperText={state.password.errorMessage}
                onChange={event => dispatch(setPassword(event.target.value))}
            />
            <AdditionalOptions dispatch={dispatch} rememberUser={state.rememberUser}/>
            <KotlinHubButton
                title={"Sign in"}
                isButtonEnabled={isButtonEnabled}
                isLoading={state.isValidating}
                onClick={() => {
                    if (state.isValidating) return

                    dispatch(
                        loginThunk(
                            {
                                username: state.login.value,
                                password: state.password.value,
                            }
                        )
                    )
                }}/>
        </Stack>
    )
}

interface AdditionalOptionsProps {
    dispatch: AppDispatch,
    rememberUser: boolean,
}

function AdditionalOptions(props: AdditionalOptionsProps) {
    const {dispatch, rememberUser} = props

    return (
        <Stack direction={"row"} justifyContent={"space-between"}>
            <FormControlLabel
                control={<Checkbox/>}
                label={"Remember me"}
                checked={rememberUser}
                onChange={(event, checked) => dispatch(setRememberMe(checked))}
            />
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
    const {sx} = props
    return (
        <Stack sx={sx}>
            <Typography variant={"body1"} sx={{
                textAlign: "center",
            }}>
                Don't have an account yet?
            </Typography>
            <Link href={"/register"} variant={"body1"} underline={"none"} sx={{
                textAlign: "center",
            }}>
                Register now
            </Link>
        </Stack>
    )
}
