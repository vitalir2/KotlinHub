import {Repository} from "../Repository";
import {useCallback, useRef, useState} from "react";
import {Box, Button, Popover, SxProps} from "@mui/material";
import {CloneRepositoryDialog} from "./CloneRepositoryDialog";

export interface GetCodeButtonProps {
    repository: Repository,
    sx?: SxProps,
}
export function GetCodeButton({repository, sx}: GetCodeButtonProps) {
    const [codeDialogAnchor, setCodeDialogAnchor] = useState<Element | null>(null)
    const codeButtonRef = useRef<HTMLButtonElement>(null)
    const isOpen = Boolean(codeDialogAnchor)

    const onClick = useCallback(
        () => {
            setCodeDialogAnchor((prevState) => {
                if (prevState === null) {
                    return codeButtonRef.current
                } else {
                    return null
                }
            })
        }, [setCodeDialogAnchor, codeButtonRef]
    )

    return (
        <Box sx={sx}>
            <Button component={"span"}
                    variant={"outlined"}
                    ref={codeButtonRef}
                    onClick={onClick}
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
                {CloneRepositoryDialog(repository)}
            </Popover>
        </Box>
    )
}
