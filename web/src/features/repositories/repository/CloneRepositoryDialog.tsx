import {Repository} from "../Repository";
import {IconButton, Snackbar, Stack, SxProps, Typography} from "@mui/material";
import {ContentCopy} from "@mui/icons-material";
import {copyText} from "../../../core/utils/copyText";
import {useState} from "react";

const cloneRepositoryStyle: SxProps = {
    padding: "1rem",
}

const cloneRepositoryUrlInputStyle: SxProps = {
    borderRadius: 1,
    borderColor: "light-grey",
    backgroundColor: "#EEEEEE",
    p: 1,
}

export function CloneRepositoryDialog(repository: Repository) {
    const [isSnackbarShown, setSnackbarShown] = useState(false);

    return (
        <Stack spacing={1} sx={cloneRepositoryStyle}>
            <Typography variant={"body1"}>
                Clone your repository by HTTP
            </Typography>
            <Stack direction={"row"}>
                <Typography variant={"body2"} sx={cloneRepositoryUrlInputStyle}>
                    {repository.cloneUrl}
                </Typography>
                <IconButton onClick={() => {
                    copyText(
                        repository.cloneUrl,
                        () => setSnackbarShown(true),
                    )
                }}>
                    <ContentCopy sx={{fontSize: 12}}/>
                </IconButton>
            </Stack>
            <Snackbar
                anchorOrigin={{vertical: "bottom", horizontal: "center"}}
                open={isSnackbarShown}
                onClose={() => setSnackbarShown(false)}
                autoHideDuration={2000}
                message={"Content was copied"}
                key={"copy"}
                />
        </Stack>
    );
}
