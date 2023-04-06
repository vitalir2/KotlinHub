import {Stack, SxProps, Typography} from "@mui/material";
import {Repository} from "../Repository";
import {GetCodeButton} from "./GetCodeButton";
import {Link, Outlet, useParams} from "react-router-dom";
import {ReactElement} from "react";

export interface RepositoryMainInfoProps {
    repository: Repository,
}

export function RepositoryMainInfo(props: RepositoryMainInfoProps) {
    const params = useParams();
    const path = params['*'];
    const {repository} = props
    console.log("Current path for segments = " + path);

    let currentPathSegments: string[] | undefined = undefined
    if (path !== undefined) {
        currentPathSegments = [repository.name, ...path.split('/')]
    }
    return (
        <Stack spacing={1}>
            <Stack
                direction={"row"}
                spacing={1}
                justifyContent={'start'}
                alignItems={'center'}
            >
                <Typography variant={"subtitle2"} sx={{marginRight: "auto"}}>
                    Branch: master
                </Typography>
                {currentPathSegments !== undefined &&
                    <SegmentsView segments={currentPathSegments}/>
                }
                <GetCodeButton repository={repository}/>
            </Stack>
            <Outlet/>
        </Stack>
    )
}

interface SegmentsViewProps {
    segments: string[],
    sx?: SxProps,
}

function SegmentsView({segments}: SegmentsViewProps) {
    let currentIndex = 0;
    const segmentsLastIndex = segments.length - 1
    return (
        <Stack direction={'row'} alignItems={"center"} spacing={1}>
            {segments.map(segment => <PathSegment
                segment={segment}
                isCurrentSegment={segmentsLastIndex === currentIndex}
                absolutePath={segments.slice(1, 1+currentIndex++).join("/")}
            />)
            }
        </Stack>
    );
}

interface PathSegmentProps {
    segment: string,
    isCurrentSegment: boolean,
    absolutePath: string,
}

function PathSegment({ segment, isCurrentSegment, absolutePath }: PathSegmentProps) {
    const { repositoryId } = useParams();
    let segmentView: ReactElement
    if (isCurrentSegment) {
        segmentView = <Typography variant={"subtitle1"}>
            {segment}
        </Typography>
    } else {
        let relativeLink: string
        if (absolutePath === "") {
            relativeLink = ""
        } else {
            relativeLink = `tree/${absolutePath}`
        }
        segmentView = <Typography
            component={Link}
            to={`/repositories/${repositoryId}/${relativeLink}`}
            sx={linkStyle}
            >
            {segment}
        </Typography>
    }
    return (
        <Stack direction={"row"} spacing={1} alignItems={'center'}>
            {segmentView}
            <Typography variant={'subtitle1'}>
                /
            </Typography>
        </Stack>
    );
}

const linkStyle: SxProps = {
    textDecoration: "none",
    ":link": {
        color: "info.dark",
    },
    ":visited": {
        color: "info.dark",
    },
    ":hover": {
        color: "info.dark",
        textDecoration: "underline",
    },
};
