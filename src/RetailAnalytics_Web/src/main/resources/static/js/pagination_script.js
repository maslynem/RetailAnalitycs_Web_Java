let url = window.location.href;

function checkUrl(currentPage, pageSize) {
    if (url.indexOf('?page=') === -1) {
        url += '?page=' + currentPage;
    }
    if (url.indexOf('&size=') === -1) {
        url += '&size=' + pageSize;
    }
    console.log(url);
}

function createSelector(totalPages, currentPage, pageSize) {
    checkUrl(currentPage - 1, pageSize);
    const element = document.querySelector(".selector");

    element.innerHTML = '<div class="item_text">Строк на странице</div> ' +
        '<select class=auto-send-select data-action=peers name=select onchange="onSelectionChange(this)">' +
        '<option>30</option>' +
        '<option>50</option>' +
        '<option>100</option>' +
        '</select>' +
        '<div class="item_text"> Страница: ' + currentPage + ' из ' + totalPages + '</div>';

    const selector = document.querySelector('.auto-send-select');
    selector.value = pageSize;
}

function onSelectionChange(select) {
    let selectedOption = select.options[select.selectedIndex];
    url = url.replace(/size=\d+/, 'size=' + selectedOption.value);
    window.location.href = url;
}

function createPagination(currentPage, pageSize, totalPages) {
    checkUrl(currentPage, pageSize);

    const element = document.querySelector(".pagination ul");
    if (element == null) return;
    let liTag = '';
    let active;
    let beforePage = currentPage - 1;
    let afterPage = currentPage + 1;
    if (currentPage > 1) { //show the prev button if the page value is greater than 1

        url = url.replace(/page=\d+/, 'page=' + (currentPage - 2));
        liTag += `<li class="btn prev" onclick="window.location.href = '${url}'"><span><i class="fas fa-angle-left"></i> Prev</span></li>`;
    }

    if (totalPages != 3 && currentPage > 2) { //if page value is greater than 2 then add 1 after the previous button
        url = url.replace(/page=\d+/, 'page=' + 0);
        liTag += `<li class="first numb" onclick="window.location.href = '${url}'"><span>1</span></li>`;
        if (currentPage > 3) { //if page value is greater than 3 then add this (...) after the first li or page
            liTag += `<li class="dots"><span>...</span></li>`;
        }
    }

    // how many pages or li show before the current li
    if (totalPages == 2) {
        beforePage = 1;
    } else if (currentPage == totalPages) {
        beforePage = beforePage - 2;
    } else if (currentPage == totalPages - 1) {
        beforePage = beforePage - 1;
    }

    // how many pages or li show after the current li
    if (currentPage == 1) {
        afterPage = afterPage + 2;
    } else if (currentPage == 2) {
        afterPage = afterPage + 1;
    }

    for (let plength = beforePage; plength <= afterPage; plength++) {
        if (plength > totalPages) { //if plength is greater than totalPage length then continue
            continue;
        }
        if (plength == 0) { //if plength is 0 than add +1 in plength value
            plength = plength + 1;
        }
        if (currentPage == plength) { //if page is equal to plength than assign active string in the active variable
            active = "active";
        } else { //else leave empty to the active variable
            active = "";
        }
        url = url.replace(/page=\d+/, 'page=' + (plength - 1));
        liTag += `<li class="numb ${active}" onclick="window.location.href = '${url}'"><span>${plength}</span></li>`;
    }

    if (totalPages != 3 && currentPage < totalPages - 1) { //if page value is less than totalPage value by -1 then show the last li or page
        if (currentPage < totalPages - 2) { //if page value is less than totalPage value by -2 then add this (...) before the last li or page
            liTag += `<li class="dots"><span>...</span></li>`;
        }
        url = url.replace(/page=\d+/, 'page=' + (totalPages - 1));
        liTag += `<li class="last numb" onclick="window.location.href = '${url}'"><span>${totalPages}</span></li>`;
    }

    if (currentPage < totalPages) { //show the next button if the page value is less than totalPage(20)
        url = url.replace(/page=\d+/, 'page=' + currentPage);
        liTag += `<li class="btn next" onclick="window.location.href = '${url}'"><span>Next <i class="fas fa-angle-right"></i></span></li>`;
    }
    element.innerHTML = liTag; //add li tag inside ul tag
}

