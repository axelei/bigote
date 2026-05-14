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

function isNotYoutubeUrl(url) {
    const regex = /^(https?:\/\/)?(www\.)?(youtube\.com|youtu\.be)\/(watch\?v=|embed\/|v\/|shorts\/)?([a-zA-Z0-0_-]{11})/;
    return !regex.test(url);
}

function download() {
    const url = document.getElementById("urlText").value.trim();

    if (isBlank(url)) {
        alert("Can't download a blank URL.")
        return;
    }

    if (isInvalidUrl(url)) {
        alert("URL seems not valid.")
        return;
    }

    if (isNotYoutubeUrl(url)) {
        alert("That doesn't seem to be a YouTube® URL.")
        return;
    }

    window.location.href = "download/mp3?url=" + url;
}
