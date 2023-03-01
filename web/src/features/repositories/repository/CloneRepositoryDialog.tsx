import {Repository} from "../Repository";
import {Stack, SxProps, Typography} from "@mui/material";

const cloneRepositoryStyle: SxProps = {
    padding: "1rem",
}

const cloneRepositoryUrlInputStyle: SxProps = {
    borderRadius: 1,
    borderColor: "light-grey",
    backgroundColor: "#EEEEEE",
    padding: "0.2rem",
}

export function CloneRepositoryDialog(repository: Repository) {
    return (
        <Stack spacing={1} sx={cloneRepositoryStyle}>
            <Typography variant={"body1"}>
                Clone your repository by HTTP
            </Typography>
            <Typography variant={"body2"} sx={cloneRepositoryUrlInputStyle}>
                {repository.cloneUrl}
            </Typography>
        </Stack>
    );
}
