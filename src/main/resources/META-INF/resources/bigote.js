function isBlank(value) {
    return typeof value !== "string" || value.trim().length === 0;
}

function isInvalidUrl(url) {
    try {
        new URL(url);
        return false; // valid URL
    } catch (e) {
        return true; // invalid URL
    }
}

function download() {
    const url = document.getElementById("urlText").value;

    if (isBlank(url)) {
        alert("Can't download a blank URL")
        return;
    }

    if (isInvalidUrl(url)) {
        alert("URL seems not valid")
        return;
    }

    window.location.href = "download/mp3?url=" + url;
}
