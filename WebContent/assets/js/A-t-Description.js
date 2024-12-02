const daysOfWeek = ['日', '月', '火', '水', '木', '金', '土'];
const currentDate = new Date();
let currentYear = currentDate.getFullYear();
let currentMonth = currentDate.getMonth() + 1;
const calendarConfig = {
    show: 1,
};



// カレンダーを表示する関数
function showCalendar(year, month) {
    for (let i = 0; i < calendarConfig.show; i++) {
        const calendarHtml = createCalendar(year, month, reservationStatus);
        const section = document.createElement('section');
        section.className = 'calendar-section';
        section.innerHTML = calendarHtml;
        document.querySelector('#calendar-container').appendChild(section);

        month++;
        if (month > 12) {
            year++;
            month = 1;
        }
    }
}

// カレンダーを作成する関数
function createCalendar(year, month, reservationStatus) {
    const startDate = new Date(year, month - 1, 1);
    const endDate = new Date(year, month, 0);
    const endDayCount = endDate.getDate();
    const lastMonthEndDate = new Date(year, month - 2, 0);
    const lastMonthEndDayCount = lastMonthEndDate.getDate();
    const startDay = startDate.getDay();
    let dayCount = 1;
    let calendarHtml = '';

    calendarHtml += `<h1 class="calendar-title">${year}/${month}</h1>`;
    calendarHtml += '<table class="calendar-table">';

    // 曜日行を作成
    calendarHtml += '<tr>';
    for (let i = 0; i < daysOfWeek.length; i++) {
        calendarHtml += `<th class="calendar-cell">${daysOfWeek[i]}</th>`;
    }
    calendarHtml += '</tr>';

    // カレンダーの日付を埋める
    for (let w = 0; w < 6; w++) {
        calendarHtml += '<tr>';

        for (let d = 0; d < 7; d++) {
            if (w === 0 && d < startDay) {
                // 前月の日を埋める
                const num = lastMonthEndDayCount - startDay + d + 1;
                calendarHtml += `<td class="calendar-cell is-disabled">${num}</td>`;
            } else if (dayCount > endDayCount) {
                // 翌月の日を埋める
                const num = dayCount - endDayCount;
                calendarHtml += `<td class="calendar-cell is-disabled">${num}</td>`;
                dayCount++;
            } else {
                // 予約状態に応じて「×」または「○」を表示
                const status = reservationStatus[dayCount] === 1 ? '×' : '○'; // 1なら予約済み (×)、0なら空き (○)
                calendarHtml += `<td class="calendar-cell calendar-day" data-date="${year}/${month}/${dayCount}">
                                    ${dayCount}<div class="status">${status}</div>
                                 </td>`;
                dayCount++;
            }
        }
        calendarHtml += '</tr>';
    }
    calendarHtml += '</table>';

    return calendarHtml;
}

// 月を移動する処理
function moveCalendar(e) {
    document.querySelector('#calendar-container').innerHTML = '';

    if (e.target.id === 'prev-month') {
        currentMonth--;

        if (currentMonth < 1) {
            currentYear--;
            currentMonth = 12;
        }
    }

    if (e.target.id === 'next-month') {
        currentMonth++;

        if (currentMonth > 12) {
            currentYear++;
            currentMonth = 1;
        }
    }

    showCalendar(currentYear, currentMonth);
}

document.querySelector('#prev-month').addEventListener('click', moveCalendar);
document.querySelector('#next-month').addEventListener('click', moveCalendar);

document.addEventListener("click", function(e) {
    if (e.target.classList.contains("calendar-day")) {
        alert('クリックした日付は' + e.target.dataset.date + 'です');
    }
});

// 初期表示
showCalendar(currentYear, currentMonth);
