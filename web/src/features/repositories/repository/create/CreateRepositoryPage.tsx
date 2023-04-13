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
import {useNavigate} from "react-router-dom";

export function CreateRepositoryPage() {
    const navigate = useNavigate();

    const [name, setName] = useState<TextInputData>({
        value: "",
        errorMessage: undefined,
    });
    const [accessMode, setAccessMode] = useState(RepositoryAccessMode.PUBLIC);
    const [description, setDescription] = useState("");
    const [isCreating, setCreatingRepository] = useState(false);

    const isButtonEnabled = name.value !== "" && name.errorMessage === undefined;
    const onRegisterClick = async () => {
        if (isCreating) return;
        setCreatingRepository(true);

        const caller = appGraph.repositoriesGraph.repositoriesRepository;
        const result = await caller.createRepository({
            name: name.value,
            accessMode: accessMode,
            description: description,
        });

        setCreatingRepository(false);

        switch (result.kind) {
            case "success":
                navigate(`/repositories/${result.repositoryId}`)
                break;
            case "error":
                setName({
                    ...name,
                    errorMessage: "Unknown error", // TODO replace by some helpful message
                })
                break;
        }
    };

    return (
        <Container maxWidth={"xs"} sx={containerStyle}>
            <Stack direction={"column"} spacing={2}>
                <CreateRepositoryForm
                    name={name}
                    accessMode={accessMode}
                    description={description}
                    setName={(newName: string) => setName({
                        value: newName,
                        errorMessage: undefined,
                    })}
                    setNameError={error => setName({
                        ...name,
                        errorMessage: error,
                    })}
                    setAccessMode={setAccessMode}
                    setDescription={setDescription}
                />
                <KotlinHubButton
                    title={"Create"}
                    isButtonEnabled={isButtonEnabled}
                    isLoading={isCreating}
                    onClick={onRegisterClick}
                />
            </Stack>
        </Container>
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
        <Stack direction={"column"} spacing={2}>
            <Typography
                variant={"h4"}
                align={"center"}
            >
                Create Repository
            </Typography>
            <TextField
                variant={"outlined"}
                label={"Repository name"}
                required
                onChange={event => setName(event.target.value)}
                value={name.value}
                helperText={name.errorMessage}
                error={name.errorMessage !== undefined}
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
                label={"Repository Description"}
                onChange={event => setDescription(event.target.value)}
                value={description}
                multiline
                rows={3}
            />
        </Stack>
    );
}
