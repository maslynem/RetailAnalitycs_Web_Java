function createHeader(title) {
    const element = document.querySelector("header");

    element.innerHTML = '<div class="row1">\n' +
        '            <div class="logo">\n' +
        '                <a href="/"><img src="/resources/img/welcome_page_logo.png" alt="" width="94px" height="88px"></a>\n' +
        '            </div>\n' +
        '            <div class="header__text header__title">' + title + '</div>\n' +
        '        </div>\n' +
        '        <div class="row2">\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref data__submenu">\n' +
        '                        <a class = "header__text" href="/peers/page-0?pageSize=30">Data\n' +
        '                            <ul class="data__list_submenu">\n' +
        '                                <li><a class = "header__text" href="/peers/page-0?pageSize=30">Peers</a></li>\n' +
        '                                <li><a class = "header__text" href="/tasks/page-0?pageSize=30">Tasks</a></li>\n' +
        '                                <li><a class = "header__text" href="/checks/page-0?pageSize=30"">Checks</a></li>\n' +
        '                                <li><a class = "header__text" href="/verters/page-0?pageSize=30"">Verter</a></li>\n' +
        '                                <li><a class = "header__text" href="/experiences/page-0?pageSize=30"">XP</a></li>\n' +
        '                                <li><a class = "header__text" href="/p2p/page-0?pageSize=30"">P2P</a></li>\n' +
        '                                <li><a class = "header__text" href="/transferred-points/page-0?pageSize=30"">TransferredPoints</a></li>\n' +
        '                                <li><a class = "header__text" href="/friends/page-0?pageSize=30"">Friends</a></li>\n' +
        '                                <li><a class = "header__text" href="/recommendations/page-0?pageSize=30"">Recommendations</a></li>\n' +
        '                                <li><a class = "header__text" href="/time-tracking/page-0?pageSize=30"">TimeTracking</a></li>\n' +
        '                            </ul>\n' +
        '                        </a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref data__submenu">\n' +
        '                       <a class = "header__text" href="/operations/">Operations\n' +
        '                            <ul class="data__list_submenu">\n' +
        '                                <li><a class = "header__text" href="/operations/">Procedures</a></li>\n' +
        '                                <li><a class = "header__text" href="/operations/query">Execute query</a></li>\n' +
        '                            </ul>\n' +
        '                        </a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '        </div>'
}