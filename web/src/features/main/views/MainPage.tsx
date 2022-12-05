import {ProfileView} from "./ProfileView";
import {MainContent} from "./MainContent";


function MainPage() {
    return (
        <div className="flex flex-row justify-center gap-2">
            <ProfileView/>
            <div className="border-r"/>
            <MainContent/>
        </div>
    )
}

export default MainPage;
