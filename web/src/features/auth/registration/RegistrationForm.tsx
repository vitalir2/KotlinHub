import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {Stack, Typography} from "@mui/material";
import {KotlinHubTextField} from "../../../core/view/input/KotlinHubTextField";
import {KotlinHubButton} from "../../../core/view/button/KotlinHubButton";
import {TextInputData} from "../../../core/models/TextInputData";
import {useAppDispatch} from "../../../app/hooks";
import {registerThunk} from "../AuthSlice";

export interface RegistrationFormProps {
    username: TextInputData,
    setUsername: (username: TextInputData) => void,
    password: TextInputData,
    setPassword: (password: TextInputData) => void,
}

export function RegistrationForm(props: RegistrationFormProps) {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const {
        username,
        setUsername,
        password,
        setPassword,
    } = props;

    const [isRegistering, setRegistering] = useState(false);
    const isButtonEnabled = username.value !== "" && username.errorMessage === undefined
        && password.value !== "" && password.errorMessage === undefined;
    const onRegistrationClick = async () => {
        if (isRegistering) return;

        setRegistering(true);
        const result = await dispatch(registerThunk({
            username: username.value,
            password: password.value,
        }));
        switch (result.kind) {
            case "success":
                navigate("/");
                break;
            case "error":
                setPassword({
                    ...password,
                    errorMessage: result.error,
                })
                break;
        }

        setRegistering(false);
    };

    return (
        <Stack direction={"column"} spacing={2}>
            <Typography variant={"h3"} align={"center"}>Register</Typography>
            <KotlinHubTextField
                label={"Username"}
                field={username}
                onChange={value => setUsername({
                    ...username,
                    value: value,
                    errorMessage: undefined,
                })}
            />
            <KotlinHubTextField
                label={"Password"}
                field={password}
                onChange={value => {
                    setUsername({
                        ...username,
                        errorMessage: undefined,
                    })
                    setPassword({
                        ...password,
                        value: value,
                        errorMessage: undefined,
                    })
                }}
                type={"password"}
            />
            <KotlinHubButton
                title={"Sign Up"}
                onClick={onRegistrationClick}
                isButtonEnabled={isButtonEnabled}
                isLoading={isRegistering}
            />
        </Stack>
    );
}
