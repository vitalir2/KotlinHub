import FolderIcon from '@mui/icons-material/Folder';
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import QuizOutlinedIcon from '@mui/icons-material/QuizOutlined';
import React from "react";
import {CircularProgress, Grid, Typography} from "@mui/material";
import {Link as RouterLink, useLocation} from "react-router-dom";
import {RepositoryFile, RepositoryFileType} from "../../RepositoryFile";
import {EmptyRepositoryContent} from "./EmptyRepositoryContent";

export interface RepositoryContentProps {
    repositoryFiles: RepositoryFile[],
    path: string,
    isLoading: boolean,
}

export function RepositoryContent({repositoryFiles, path, isLoading}: RepositoryContentProps) {
    let fileIndex = 0;
    if (isLoading) {
        return <CircularProgress/>;
    } else if (repositoryFiles.length === 0) {
        return <EmptyRepositoryContent/>;
    } else {
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
        );
    }
}

export interface RepositoryFileViewProps {
    repositoryFile: RepositoryFile,
    index: number,
    path: string,
}

export function RepositoryFileView({repositoryFile, index, path}: RepositoryFileViewProps) {
    const location = useLocation();
    const absolutePath = location.pathname;
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
    switch (repositoryFile.type) {
        case RepositoryFileType.FOLDER:
            if (path === "") {
                link = `tree/${repositoryFile.name}`;
            } else {
                link = `${path}/${repositoryFile.name}`;
            }
            break;
        case RepositoryFileType.REGULAR:
            if (path === "") {
                link = `content/${repositoryFile.name}`;
            } else {
                const repositoryPath = absolutePath.substring(0, absolutePath.indexOf("tree") - 1);
                link = `${repositoryPath}/content/${path}/${repositoryFile.name}`;
            }
            break;
        case RepositoryFileType.UNKNOWN:
            link = `${path}/${repositoryFile.name}`;

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
