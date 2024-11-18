document.addEventListener('DOMContentLoaded', function () {
    function createCalendar(year, month, reservations) {
        const calendar = document.getElementById("calendar");
        calendar.innerHTML = ""; // カレンダーをクリア

        // 月と年の表示
        const monthYear = document.createElement("div");
        monthYear.id = "monthYear";
        monthYear.innerText = `${year}年 ${month + 1}月`;
        calendar.appendChild(monthYear);

        // 曜日のヘッダー
        const weekdays = ["日", "月", "火", "水", "木", "金", "土"];
        const headerRow = document.createElement("tr");
        weekdays.forEach(day => {
            const th = document.createElement("th");
            th.innerText = day;
            headerRow.appendChild(th);
        });
        const headerTable = document.createElement("table");
        headerTable.appendChild(headerRow);
        calendar.appendChild(headerTable);

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

            // クリックイベントを追加
            td.addEventListener("click", function () {
                const selectedDate = `${year}-${month + 1}-${date}`;
                window.location.href = `/your-target-page?date=${selectedDate}`;
            });

            // 予約件数を表示
            const count = reservations[date] || 0; // 予約件数を取得
            if (count > 0) {
                const reservationCount = document.createElement("div");
                reservationCount.className = "reservation-count";
                reservationCount.innerText = `予約: ${count}`;
                td.appendChild(reservationCount);
            }

            row.appendChild(td);

            // 週の終わりで新しい行を作成
            if ((date + firstDay) % 7 === 0) {
                const table = document.createElement("table");
                table.appendChild(row);
                calendar.appendChild(table);
                row = document.createElement("tr");
            }
        }

        // 最後の行を追加
        if (row.children.length > 0) {
            const table = document.createElement("table");
            table.appendChild(row);
            calendar.appendChild(table);
        }
    }

    // サンプルデータでカレンダーを作成
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
});
