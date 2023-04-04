import {Stack, Typography} from "@mui/material";
import {Repository} from "../Repository";
import {GetCodeButton} from "./GetCodeButton";
import {Link, Outlet, useParams} from "react-router-dom";
import {ReactElement} from "react";

export interface RepositoryMainInfoProps {
    repository: Repository,
}

export function RepositoryMainInfo(props: RepositoryMainInfoProps) {
    const { path } = useParams();
    const {repository} = props

    let currentPathSegments: string[] | undefined = undefined
    if (path !== undefined) {
        currentPathSegments = [repository.name, ...path.split('/')]
    }
    return (
        <Stack spacing={1}>
            <Stack direction={"row"} spacing={1} justifyContent={"space-around"}>
                <Typography variant={"subtitle2"}>
                    Branch: master
                </Typography>
                {currentPathSegments !== undefined &&
                    // TODO fix jumping of segments view
                    <SegmentsView segments={currentPathSegments}/>
                }
                {GetCodeButton(repository)}
            </Stack>
            <Outlet/>
        </Stack>
    )
}

interface SegmentsViewProps {
    segments: string[],
}

function SegmentsView({segments}: SegmentsViewProps) {
    let currentIndex = 0;
    const segmentsLastIndex = segments.length - 1
    return (
        <Stack direction={'row'} spacing={1}>
            {segments.map(segment => <PathSegment
                segment={segment}
                isCurrentSegment={segmentsLastIndex === currentIndex}
                absolutePath={segments.slice(0, currentIndex++).join("/")}
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
        segmentView = <Typography
            component={Link}
            to={`/repositories/${repositoryId}/${absolutePath}`}
            >
            {segment}
        </Typography>
    }
    return (
        <Stack direction={"row"} spacing={1}>
            {segmentView}
            <Typography variant={'subtitle1'}>
                /
            </Typography>
        </Stack>
    );
}
