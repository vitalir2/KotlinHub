import FolderIcon from '@mui/icons-material/Folder';
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import QuizOutlinedIcon from '@mui/icons-material/QuizOutlined';
import React from "react";
import {Grid, SxProps, Typography} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import {RepositoryFile, RepositoryFileType} from "../../RepositoryFile";

export interface RepositoryContentProps {
    repositoryFiles: RepositoryFile[],
}

export function RepositoryContent({ repositoryFiles }: RepositoryContentProps) {
    return (
        <>
            {repositoryFiles.map(file => <  RepositoryFileView repositoryFile={file}/>)}
        </>
    )
}

export interface RepositoryFileViewProps {
    repositoryFile: RepositoryFile,
    sxProps?: SxProps,
}

export function RepositoryFileView({ repositoryFile, sxProps }: RepositoryFileViewProps) {
    let iconView: React.ReactElement
    switch (repositoryFile.type) {
        case RepositoryFileType.FOLDER:
            iconView = <FolderIcon/>
            break;
        case RepositoryFileType.REGULAR:
            iconView = <DescriptionOutlinedIcon/>
            break;
        case RepositoryFileType.UNKNOWN:
            iconView = <QuizOutlinedIcon/>
            break;
    }
    return (
        <Grid container spacing={0.5} sx={sxProps}>
            <Grid item xs={1}>
                {iconView}
            </Grid>
            <Grid item xs={11}>
                <Typography
                    component={RouterLink}
                    variant={"body1"}
                    to={repositoryFile.name}
                    sx={linkStyle}
                >
                    {repositoryFile.name}
                </Typography>
            </Grid>
        </Grid>
    )
}

const linkStyle: SxProps = {
    textDecoration: "none",
    ":link": {
        color: "text.primary",
    },
    ":hover": {
        color: "primary.main",
    },
};
