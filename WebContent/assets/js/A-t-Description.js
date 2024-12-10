document.addEventListener("DOMContentLoaded", () => {
    const calendarGrid = document.getElementById("calendar");
    const reservationData = {};  // サーブレットから渡された予約データ

    let today = new Date();
    let year = today.getFullYear();
    let month = today.getMonth();

    // カレンダーを描画する関数
    const renderCalendar = () => {
        calendarGrid.innerHTML = ""; // カレンダーを初期化

        const firstDay = new Date(year, month, 1).getDay(); // 月の最初の日の曜日
        const lastDate = new Date(year, month + 1, 0).getDate(); // 月の最後の日
        const prevLastDate = new Date(year, month, 0).getDate(); // 前月の最後の日

        // 前月の日付を表示
        for (let i = firstDay - 1; i >= 0; i--) {
            const cell = document.createElement("div");
            cell.classList.add("calendar-cell", "disabled");
            cell.textContent = prevLastDate - i;
            calendarGrid.appendChild(cell);
        }

        // 今月の日付を表示
        for (let date = 1; date <= lastDate; date++) {
            const cell = document.createElement("div");
            cell.classList.add("calendar-cell");

            // 予約状況の表示
            const status = reservationData[date];
            const statusDiv = document.createElement("div");
            statusDiv.classList.add("status", status ? "reserved" : "available");
            statusDiv.textContent = status ? "×" : "○";

            const link = document.createElement("a");
            link.href = "#"; // クリックアクション
            link.textContent = date;
            link.onclick = () => alert(`${year}年${month + 1}月${date}日が選択されました`);

            cell.appendChild(link);
            cell.appendChild(statusDiv);
            calendarGrid.appendChild(cell);
        }

        // 次月の日付を表示
        const nextDays = 42 - (firstDay + lastDate);
        for (let i = 1; i <= nextDays; i++) {
            const cell = document.createElement("div");
            cell.classList.add("calendar-cell", "disabled");
            cell.textContent = i;
            calendarGrid.appendChild(cell);
        }
    };

    // 前の月ボタンのイベント
    prevMonthBtn.addEventListener("click", () => {
        month--;
        if (month < 0) {
            month = 11;
            year--;
        }
        renderCalendar();
    });

    // 次の月ボタンのイベント
    nextMonthBtn.addEventListener("click", () => {
        month++;
        if (month > 11) {
            month = 0;
            year++;
        }
        renderCalendar();
    });

    renderCalendar(); // 初期描画
});
