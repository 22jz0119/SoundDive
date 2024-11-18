function createCalendar(year, month, reservations) {
    const calendar = document.getElementById("calendar");
    calendar.innerHTML = ""; // カレンダーをクリア

    // 月と年の表示
    const monthYear = document.getElementById("monthYear");
    monthYear.innerText = `${year}年 ${month + 1}月`;

    // 曜日のヘッダー
    const weekdays = ["日", "月", "火", "水", "木", "金", "土"];
    const headerRow = document.createElement("tr");
    weekdays.forEach(day => {
        const th = document.createElement("th");
        th.innerText = day;
        headerRow.appendChild(th);
    });
    calendar.appendChild(headerRow);

    // 月の日数と初日の曜日を取得
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    // 空のセルを追加
    let row = document.createElement("tr");
    for (let i = 0; i < firstDay; i++) {
        const td = document.createElement("td");
        row.appendChild(td);
    }

    // 日付を追加
    for (let date = 1; date <= lastDate; date++) {
        const td = document.createElement("td");
        td.innerText = date; // 日付を表示

        // 予約件数を表示
        const count = reservations[date] || 0; // 予約件数を取得
        if (count > 0) {
            const reservationCount = document.createElement("div");
            reservationCount.className = "reservation-count";
            reservationCount.innerText = `予約件数: ${count}`;
            td.appendChild(reservationCount);
        }

        row.appendChild(td);

        // 週の終わりで新しい行を作成
        if ((date + firstDay) % 7 === 0) {
            calendar.appendChild(row);
            row = document.createElement("tr");
        }
    }
    // 最後の行を追加
    if (row.children.length > 0) {
        calendar.appendChild(row);
    }
}

// 現在の年月でカレンダーを作成
const today = new Date();
const reservations = {
    1: 2,
    5: 1,
    10: 3,
    15: 0,
    20: 5,
    25: 1,
    30: 4
}; // 予約件数のサンプルデータ
createCalendar(today.getFullYear(), today.getMonth(), reservations);

// 月切り替え機能
let currentYear = today.getFullYear();
let currentMonth = today.getMonth();

document.getElementById("prevMonth").addEventListener("click", function () {
    currentMonth--;
    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }
    createCalendar(currentYear, currentMonth, reservations);
});

document.getElementById("nextMonth").addEventListener("click", function () {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    createCalendar(currentYear, currentMonth, reservations);
});
