import {Link as NavLink, useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Stack, SxProps, Tab, Tabs, Typography} from "@mui/material";
import {ReactElement, useEffect, useMemo, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {fetchRepository} from "./RepositorySlice";
import {User} from "../../auth/User";
import {ErrorPlaceholder} from "../../../core/view/placeholder/ErrorPlaceholder";
import {LoadingPlaceholder} from "../../../core/view/placeholder/LoadingPlaceholder";
import {RepositoryInfo} from "./RepositoryInfo";
import {RepositorySettingsInfo} from "./RepositorySettingsInfo";
import {useAuthState} from "../../auth/AuthHooks";
import {fetchCurrentUser} from "../../auth/AuthSlice";

export function RepositoryPage() {
    const dispatch = useAppDispatch()

    const { repositoryId } = useParams()
    const authState = useAuthState();
    const user = authState.user;
    const repository = useAppSelector(state => state.repository.repository)

    useEffect(() => {
        if (repositoryId === undefined) return;
        dispatch(fetchCurrentUser());
        dispatch(fetchRepository(repositoryId));
    }, [repositoryId, dispatch])

    if (user === undefined || repository.kind === "loading") {
        return <LoadingPlaceholder/>
    }

    if (repositoryId === undefined || repository.kind === "error") {
        return <ErrorPlaceholder error={"Something went wrong"} />
    }

    return <RepositoryContainer user={user} repository={repository.data}/>
}

interface RepositoryContainerProps {
    user: User,
    repository: Repository,
}

const repositoryContainerStyle: SxProps = {
    padding: "1rem",
}

const repositoryPageTabStyle: SxProps = {
    marginRight: "20vw",
}

const repositoryTabsStyle: SxProps = {
    width: "90vw",
}

function RepositoryContainer(props: RepositoryContainerProps) {
    const [currentTab, setCurrentTab] = useState(0)
    const { user, repository } = props

    let tabPanel: ReactElement = useMemo(() => {
        switch (currentTab) {
            case 0:
                return <RepositoryInfo repository={repository}/>
            case 1:
                return <RepositorySettingsInfo />
            default:
                console.log(`Unknown repository page tab=${currentTab}`)
                return <RepositoryInfo repository={repository}/>
        }
    }, [currentTab, repository])
    return (
        <Stack spacing={2} sx={repositoryContainerStyle}>
            <RepositoryName username={user.username} repositoryName={repository.name}/>
            <Tabs
                value={currentTab}
                onChange={(e, value: number) => setCurrentTab(value)}
                sx={repositoryTabsStyle}
            >
                <Tab label={"Code"} sx={repositoryPageTabStyle}/>
                <Tab label={"Settings"} sx={repositoryPageTabStyle}/>
            </Tabs>
            {tabPanel}
        </Stack>
    )
}

interface RepositoryNameProps {
    username: string,
    repositoryName: string,
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
    }
}

function RepositoryName({username, repositoryName}: RepositoryNameProps) {
    const variant = "h6"
    return (
        <Stack direction={"row"} spacing={0.5} alignItems={"center"}>
            <Typography
                variant={variant}
                sx={linkStyle}
                fontWeight={400}
                component={NavLink}
                to={`/`}
            >
                {username}
            </Typography>
            <Typography variant={variant}>/</Typography>
            <Typography
                component={NavLink}
                sx={linkStyle}
                variant={variant}
                to={`/repositories/${repositoryName}`}
            >
                {repositoryName}
            </Typography>
        </Stack>
    );
}
