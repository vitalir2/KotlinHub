import {
    Container,
    Stack,
    SxProps,
    TextField,
    Theme,
    ToggleButton,
    ToggleButtonGroup,
    Typography
} from "@mui/material";
import React, {useState} from "react";
import {RepositoryAccessMode} from "../../Repository";
import {TextInputData} from "../../../../core/models/TextInputData";
import {KotlinHubButton} from "../../../../core/view/button/KotlinHubButton";
import {appGraph} from "../../../../app/dependency_injection";

export function CreateRepositoryPage() {
    const [name, setName] = useState<TextInputData>({
        value: "",
        errorMessage: undefined,
    });
    const [accessMode, setAccessMode] = useState(RepositoryAccessMode.PUBLIC);
    const [description, setDescription] = useState("");
    const [isRegistering, setRegistering] = useState(false);
    const onRegisterClick = () => {
        setRegistering(true)
        const caller = appGraph.repositoriesGraph.repositoriesRepository; // TODO
        setRegistering(false)
    };

    return (
        <Stack direction={"column"} spacing={2}>
            <CreateRepositoryForm
                name={name}
                accessMode={accessMode}
                description={description}
                setName={(newName: string) => setName({
                    ...name,
                    value: newName,
                })}
                setNameError={error => setName({
                   ...name,
                   errorMessage: error,
                })}
                setAccessMode={setAccessMode}
                setDescription={setDescription}
            />
            <KotlinHubButton
                title={"Sign In"}
                isButtonEnabled={true}
                isLoading={isRegistering}
                onClick={onRegisterClick}
            />
        </Stack>
    );
}

export interface CreateRepositoryFormProps {
    name: TextInputData,
    accessMode: RepositoryAccessMode,
    description: string,
    setName: (name: string) => void,
    setNameError: (error: string | undefined) => void,
    setAccessMode: (accessMode: RepositoryAccessMode) => void,
    setDescription: (description: string) => void,
}

const containerStyle: SxProps<Theme> = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    height: "85vh",
}

export function CreateRepositoryForm(props: CreateRepositoryFormProps) {
    const {
        name,
        accessMode,
        description,
        setName,
        setAccessMode,
        setDescription,
    } = props;

    return (
        <Container maxWidth={"xs"} sx={containerStyle}>
            <Stack direction={"column"} spacing={2}>
                <Typography variant={"h4"}>Create Repository</Typography>
                <TextField
                    variant={"outlined"}
                    onChange={event => setName(event.target.value)}
                    value={name}
                    />
                <ToggleButtonGroup
                    color={"primary"}
                    value={accessMode}
                    exclusive
                    onChange={(_, newAccessMode) => setAccessMode(newAccessMode)}
                >
                    <ToggleButton value={RepositoryAccessMode.PUBLIC}>Public</ToggleButton>
                    <ToggleButton value={RepositoryAccessMode.PRIVATE}>Private</ToggleButton>
                </ToggleButtonGroup>
                <TextField
                    variant={"outlined"}
                    onChange={event => setDescription(event.target.value)}
                    value={description}
                    multiline
                    rows={3}
                />
            </Stack>
        </Container>
    );
}
