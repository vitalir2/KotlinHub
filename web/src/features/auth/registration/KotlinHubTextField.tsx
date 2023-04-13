import {TextField} from "@mui/material";
import {TextInputData} from "../../../core/models/TextInputData";

export interface KotlinHubTextFieldProps {
    field: TextInputData,
    label: string,
    onChange: (value: string) => void,
}

export function KotlinHubTextField({field, label, onChange}: KotlinHubTextFieldProps) {
    return (
        <TextField
            label={label}
            value={field.value}
            error={field.errorMessage !== undefined}
            helperText={field.errorMessage}
            onChange={event => onChange(event.target.value)}
        />
    );
}
