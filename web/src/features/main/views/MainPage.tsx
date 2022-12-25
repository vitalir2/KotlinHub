import {ProfileView} from "./ProfileView";
import {MainContent} from "./MainContent";
import {useEffect, useState} from "react";
import {Repository} from "../models/Repository";
import {fetchRepositories} from "../fetcher/RepositoryFetcher";


function MainPage() {
    const [repositories, setRepositories] = useState<Repository[]>([])
    useEffect(() => {
            const fetchPageData = async () => {
                const repositories = await fetchRepositories()
                setRepositories(repositories)
            }
            fetchPageData()
        }
        , [])
    return (
        <div className="flex flex-row justify-center gap-2">
            <ProfileView/>
            <div className="border-r"/>
            <MainContent repositories={repositories}/>
        </div>
    )
}

export default MainPage;
