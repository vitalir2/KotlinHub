// TODO: Replace by navigator API when https will be supported
export function copyText(
    text: string,
    onSuccess: () => void,
    onError: () => void = () => {},
) {
    const input = document.createElement('input');
    input.setAttribute('value', text);
    document.body.appendChild(input);
    input.select();
    document.execCommand('copy');
    document.body.removeChild(input);
}
