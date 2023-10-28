function getHtmlDate(date) {
    let dd = date.getDate();
    let mm = date.getMonth() + 1; //January is 0!
    let yyyy = date.getFullYear();

    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }
    return yyyy + '-' + mm + '-' + dd;
}

function getMinDate() {
    let now = new Date();
    now.setFullYear(now.getFullYear() - 100);
    return getHtmlDate(now);
}

function getMaxDate() {
    let now = new Date();
    now.setFullYear(now.getFullYear() - 18);
    return getHtmlDate(now);
}

let dateField = document.getElementById("dateField");
dateField.setAttribute("max", getMaxDate());
dateField.setAttribute("min", getMinDate());
