import {TextField} from "@mui/material";
import {TextInputData} from "../../models/TextInputData";

export interface KotlinHubTextFieldProps {
    field: TextInputData,
    label: string,
    onChange: (value: string) => void,
    type?: string,
}

export function KotlinHubTextField({field, label, onChange, type}: KotlinHubTextFieldProps) {
    return (
        <TextField
            label={label}
            value={field.value}
            error={field.errorMessage !== undefined}
            helperText={field.errorMessage}
            onChange={event => onChange(event.target.value)}
            type={type}
        />
    );
}
