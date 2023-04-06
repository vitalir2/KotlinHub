import FolderIcon from '@mui/icons-material/Folder';
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import QuizOutlinedIcon from '@mui/icons-material/QuizOutlined';
import React from "react";
import {Grid, Typography} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import {RepositoryFile, RepositoryFileType} from "../../RepositoryFile";

export interface RepositoryContentProps {
    repositoryFiles: RepositoryFile[],
    path: string,
}

export function RepositoryContent({repositoryFiles, path}: RepositoryContentProps) {
    let fileIndex = 0;
    return (
        <Grid container spacing={0.5} sx={{
            border: '1px solid lightgrey',
        }}>
            {repositoryFiles.map(file =>
                <RepositoryFileView
                    key={fileIndex}
                    repositoryFile={file}
                    index={fileIndex++}
                    path={path}
                />)
            }
        </Grid>
    )
}

export interface RepositoryFileViewProps {
    repositoryFile: RepositoryFile,
    index: number,
    path: string,
}

export function RepositoryFileView({repositoryFile, index, path}: RepositoryFileViewProps) {
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

    let border: string
    if (index === 0) {
        border = ""
    } else {
        border = "1px solid lightgray"
    }

    let link: string;
    if (path === "" && repositoryFile.type === RepositoryFileType.FOLDER) {
        link = `tree/${repositoryFile.name}`
    } else {
        link = `${path}/${repositoryFile.name}`
    }
    return (
        <Grid container xs={12} sx={{
            borderTop: border,
            padding: "4px",
            ":hover": {
                backgroundColor: "grey.100",
            }
        }}>
            <Grid item xs={1}>
                {iconView}
            </Grid>
            <Grid item xs={11}>
                <Typography
                    component={RouterLink}
                    variant={"body1"}
                    to={link}
                    sx={linkStyle}
                >
                    {repositoryFile.name}
                </Typography>
            </Grid>
        </Grid>
    )
}

const linkStyle = {
    textDecoration: "none",
    ":link": {
        color: "text.primary",
    },
    ":visited": {
        color: "text.primary",
    },
    ":hover": {
        color: "info.dark",
    },
}
