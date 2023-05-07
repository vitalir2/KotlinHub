import {Stack, Typography} from "@mui/material";

export function RepositoryMetaInfo(description: string) {
    return (
        <Stack spacing={1}>
            <Typography variant={"subtitle1"} fontWeight={600}>
                About
            </Typography>
            <Typography variant={"body1"}>
                {description}
            </Typography>
        </Stack>
    )
}
