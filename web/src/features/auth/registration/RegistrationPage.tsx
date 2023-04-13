import {useState} from "react";
import {TextInputData} from "../../../core/models/TextInputData";
import {Container, Stack, SxProps} from "@mui/material";
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
        </Container>
    );
}
