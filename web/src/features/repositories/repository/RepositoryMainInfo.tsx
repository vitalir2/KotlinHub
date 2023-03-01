import {useRef, useState} from "react";
import {Button, Popover, Stack, Typography} from "@mui/material";
import {Repository} from "../Repository";

export interface RepositoryMainInfoProps {
    repository: Repository,
}

export function RepositoryMainInfo(props: RepositoryMainInfoProps) {
    const {repository} = props
    const [codeDialogAnchor, setCodeDialogAnchor] = useState<Element | null>(null)
    const codeButtonRef = useRef<HTMLButtonElement>(null)
    const isOpen = Boolean(codeDialogAnchor)

    return (
        <Stack spacing={1}>
            <Stack direction={"row"} spacing={3}>
                <Typography variant={"subtitle2"}>
                    Branch: master
                </Typography>
                <Button component={"span"}
                        variant={"outlined"}
                        ref={codeButtonRef}
                        onClick={() => {
                            setCodeDialogAnchor((prevState) => {
                                if (prevState === null) {
                                    return codeButtonRef.current
                                } else {
                                    return null
                                }
                            })
                        }}
                >
                    Code
                </Button>
                <Popover
                    open={isOpen}
                    anchorEl={codeDialogAnchor}
                    anchorOrigin={{
                        vertical: "bottom",
                        horizontal: "left",
                    }}
                    onClose={() => {
                        setCodeDialogAnchor(null)
                    }}
                >
                    <Stack spacing={1} sx={{
                        padding: "1rem",
                    }}>
                        <Typography variant={"body1"}>
                            Clone your repository by HTTP
                        </Typography>
                        <Typography variant={"body2"} sx={{
                            borderRadius: 1,
                            borderColor: "light-grey",
                            backgroundColor: "#EEEEEE",
                            padding: "0.2rem",
                        }}>
                            {repository.cloneUrl}
                        </Typography>
                    </Stack>
                </Popover>
            </Stack>
        </Stack>
    )
}
