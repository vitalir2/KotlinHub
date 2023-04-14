import {Stack, SxProps, Typography} from "@mui/material";
import {Link, useParams} from "react-router-dom";
import {ReactElement, useEffect} from "react";


export interface SegmentsViewProps {
    segments: string[],
    sx?: SxProps,
}

export function SegmentsView({segments}: SegmentsViewProps) {
    useEffect(() => {
        if (segments.length === 0) {
            console.error("Empty segments are rendered")
        }
    }, [segments]);

    let currentIndex = 0;
    const segmentsLastIndex = segments.length - 1;
    return (
        <Stack direction={'row'} alignItems={"center"} spacing={1} flexWrap={"wrap"}>
            {segments.map(segment => <PathSegment
                segment={segment}
                isCurrentSegment={segmentsLastIndex === currentIndex}
                absolutePath={segments.slice(1, 1 + currentIndex++).join("/")}
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

function PathSegment({segment, isCurrentSegment, absolutePath}: PathSegmentProps) {
    const {repositoryId} = useParams();
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
