export function ProfileView() {
    return (
        <aside className="flex flex-col gap-2 p-2">
            <img className="w-48 h-48 rounded-full" src={"https://avatars.githubusercontent.com/u/35116812?v=4"}
                 alt="Profile"/>
            <p className="font-bold text-start">Vitalir</p>
            <p className="text-base font-light text-start">Description about me</p>
        </aside>
    )
}
