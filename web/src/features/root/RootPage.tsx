import {useAuthState} from "../auth/AuthHooks";
import {MainPage} from "../main/MainPage";
import {LoginPage} from "../login/LoginPage";

export function RootPage() {
    const authState = useAuthState();
    const isLoggedIn = authState.kind === "logged_in";
    if (isLoggedIn) {
        return <MainPage/>;
    } else {
        return <LoginPage/>;
    }
}
