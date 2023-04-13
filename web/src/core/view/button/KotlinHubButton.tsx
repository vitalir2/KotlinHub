import {Button, CircularProgress} from "@mui/material";
import React from "react";

export interface KotlinHubButtonProps {
    title: string,
    onClick: () => void,
    isButtonEnabled: boolean,
    isLoading: boolean,
}

export function KotlinHubButton(props: KotlinHubButtonProps) {
    const { title, onClick, isButtonEnabled, isLoading } = props;

    return (
        <Button
            variant={"contained"}
            disabled={!isButtonEnabled}
            onClick={onClick}
        >
            {isLoading ? <CircularProgress/> : title}
        </Button>
    );
}
