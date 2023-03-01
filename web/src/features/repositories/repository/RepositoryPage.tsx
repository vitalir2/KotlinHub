import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Stack, SxProps, Tab, Tabs, Typography} from "@mui/material";
import {ReactElement, useEffect, useMemo, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {fetchRepository} from "./RepositorySlice";
import {User} from "../../user/User";
import {fetchCurrentUser} from "../../user/UserSlice";
import {ErrorPlaceholder} from "../../../core/view/placeholder/ErrorPlaceholder";
import {LoadingPlaceholder} from "../../../core/view/placeholder/LoadingPlaceholder";
import {RepositoryInfo} from "./RepositoryInfo";
import {RepositorySettingsInfo} from "./RepositorySettingsInfo";

export function RepositoryPage() {
    const dispatch = useAppDispatch()

    const { repositoryId } = useParams()
    const user = useAppSelector(state => state.user.user)
    const repository = useAppSelector(state => state.repository.repository)

    useEffect(() => {
        if (repositoryId === undefined) return
        dispatch(fetchRepository(repositoryId))
        dispatch(fetchCurrentUser())
    }, [repositoryId, dispatch])

    if (repository.kind === "loading" || user.kind === "loading") {
        return <LoadingPlaceholder/>
    }

    if (repositoryId === undefined || repository.kind === "error" || user.kind === "error") {
        return <ErrorPlaceholder error={"Something went wrong"} />
    }

    return <RepositoryContainer user={user.data} repository={repository.data}/>
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

    const formattedName = `${user.username} / ${repository.name}`

    return (
        <Stack spacing={2} sx={repositoryContainerStyle}>
            <Typography variant={"h5"}>{formattedName}</Typography>
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
