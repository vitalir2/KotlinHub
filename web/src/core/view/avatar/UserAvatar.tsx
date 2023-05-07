import {Avatar, SxProps} from "@mui/material";
import {deepPurple} from "@mui/material/colors";

interface UserAvatarProps {
    name: string,
    imageUrl?: string,
    sx?: SxProps,
}

export function UserAvatar({name, imageUrl, sx}: UserAvatarProps) {
    if (imageUrl !== undefined) {
        return (
            <Avatar variant={"circular"}
                    src={imageUrl}
                    alt={`${name} avatar`}
                    sx={sx}
            >

            </Avatar>
        );
    } else {
        return (
            <Avatar variant={"circular"}
                    alt={`${name} avatar`}
                    sx={{...sx, bgcolor: deepPurple[500]}}
            >
                {name.charAt(0).toUpperCase()}
            </Avatar>
        );
    }
}
