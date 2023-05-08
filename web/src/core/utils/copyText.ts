// TODO: Replace by navigator API when HTTPS will be supported
export function copyText(
    text: string,
    onSuccess: () => void,
    onError: () => void = () => {},
) {
    let aux = document.createElement("div");
    aux.setAttribute("contentEditable", "true");
    aux.innerHTML = text;
    document.body.appendChild(aux);
    window.getSelection()?.selectAllChildren(aux);
    const isSuccessful = document.execCommand("copy");
    if (isSuccessful) {
        onSuccess()
    } else {
        onError()
    }
    document.body.removeChild(aux);
}
