import {
    Button,
    Checkbox, CircularProgress,
    Container,
    FormControlLabel,
    Link,
    Stack,
    SxProps,
    TextField, Theme,
    Typography
} from "@mui/material";
import React, {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {loginUser, setLogin, setPassword, setRememberMe} from "./LoginSlice";
import {AppDispatch} from "../../app/store";
import {useNavigate} from "react-router-dom";

export function LoginPage() {
    const containerStyle: SxProps<Theme> = {
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        justifyContent: "center",
    }

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
    const navigate = useNavigate()

    useEffect(() => {
        if (state.isLoggedInSuccessfully) {
            // TODO save auth token
            navigate("/main")
        }
    }, [navigate, state.isLoggedInSuccessfully])

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
            <Button
                variant={"contained"}
                disabled={!isButtonEnabled}
                onClick={() => {
                    if (!state.isValidating) {
                        dispatch(
                            loginUser(
                                {
                                    username: state.login.value,
                                    password: state.password.value,
                                }
                            )
                        )
                    }
                }}>
                {state.isValidating ? <CircularProgress/> : "Sign in"}
            </Button>
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
            <Link href={"#"} variant={"body1"} underline={"none"} sx={{
                textAlign: "center",
            }}>
                Register now
            </Link>
        </Stack>
    )
}
