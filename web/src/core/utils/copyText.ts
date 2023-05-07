export function copyText(
    text: string,
    onSuccess: () => void,
    onError: () => void = () => {},
) {
    const type = "text/plain";
    const blob = new Blob([text], {type});
    const data = [new ClipboardItem({[type]: blob})];
    navigator.clipboard.write(data).then(
        () => onSuccess(),
        () => onError(),
    );
}
