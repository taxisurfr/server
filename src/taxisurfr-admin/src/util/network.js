export function getUrl(type) {
    var url = '';

    if ("undefined" !== typeof window) {
        console.log(window.location.hostname);
        console.log(window.location.port);
        if (window.location.hostname === 'localhost') {
            url = 'http://localhost:8080/';
            console.log('CAUTION: connecting with:'+url);
        }
    }

    url = url + 'rest/admin/' + type;
    return url;
}

export function redirect(link) {
    window.location = 'arugambay';
}

export function getMethod(method, body) {
    var tokenId = localStorage.getItem("tokenId");
    return {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + tokenId
        },
        body: body

    };
}

export function getOptions(method) {
    var tokenId = localStorage.getItem("tokenId");
    var options = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + tokenId
        },
        // mode: 'no-cors',
        cache: 'default'
    };
    return options;
}
