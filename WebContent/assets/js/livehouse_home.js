document.addEventListener("DOMContentLoaded", function () {
    let today = new Date();
    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth() + 1; // 月は0始まりなので+1

    const calendarDiv = document.getElementById("calendar");
    const reservationListDiv = document.getElementById("reservation-list");

    // カレンダーをレンダリング
    function renderCalendar(year, month) {
        const firstDay = new Date(year, month - 1, 1); // 1日
        const lastDay = new Date(year, month, 0); // 月末
        const daysInMonth = lastDay.getDate();
        const startDayOfWeek = firstDay.getDay(); // 日曜日=0

        let html = "<table border='1'><tr>";
        const daysOfWeek = ["日", "月", "火", "水", "木", "金", "土"];

        // 曜日ヘッダー
        daysOfWeek.forEach(day => {
            html += `<th>${day}</th>`;
        });
        html += "</tr><tr>";

        // 空白セル (1日の曜日に対応するまで)
        for (let i = 0; i < startDayOfWeek; i++) {
            html += "<td></td>";
        }

        // 日付セル
        for (let day = 1; day <= daysInMonth; day++) {
            if ((startDayOfWeek + day - 1) % 7 === 0 && day !== 1) {
                html += "</tr><tr>"; // 新しい行を開始
            }
            html += `<td class="calendar-day" data-day="${day}" data-year="${year}" data-month="${month}">${day}</td>`;
        }

        // 空白セル (月末の残り)
        const remainingCells = (7 - (startDayOfWeek + daysInMonth) % 7) % 7;
        for (let i = 0; i < remainingCells; i++) {
            html += "<td></td>";
        }

        html += "</tr></table>";
        calendarDiv.innerHTML = html;

        // 日付セルのクリックイベントを設定
        document.querySelectorAll(".calendar-day").forEach(cell => {
            cell.addEventListener("click", function () {
                const year = this.getAttribute("data-year");
                const month = this.getAttribute("data-month");
                const day = this.getAttribute("data-day");
                
                console.log(`Selected Date: ${year}-${month}-${day}`); // デバッグ用ログ
                fetchReservations(year, month, day);
            });
        });
    }

    // 特定の日付の予約リストを取得
    function fetchReservations(year, month, day) {
        fetch(`Livehouse_reservation?year=${year}&month=${month}&day=${day}`)
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    reservationListDiv.innerHTML = `<p>${data.error}</p>`;
                } else {
                    renderReservationList(data);
                }
            })
            .catch(error => {
                console.error("Error fetching reservations:", error);
                reservationListDiv.innerHTML = "<p>予約リストの取得に失敗しました。</p>";
            });
    }

    // 予約リストをレンダリング
    function renderReservationList(reservations) {
        if (reservations.length === 0) {
            reservationListDiv.innerHTML = "<p>この日に予約はありません。</p>";
            return;
        }

        let html = "<table border='1'><tr><th>グループ名</th><th>ジャンル</th><th>バンド歴</th><th>開始時間</th><th>終了時間</th></tr>";
        reservations.forEach(reservation => {
            html += `<tr>
                        <td>${reservation.accountName}</td>
                        <td>${reservation.groupGenre}</td>
                        <td>${reservation.bandYears}年</td>
                        <td>${reservation.startTime}</td>
                        <td>${reservation.finishTime}</td>
                    </tr>`;
        });
        html += "</table>";
        reservationListDiv.innerHTML = html;
    }

    // 初期カレンダー表示
    renderCalendar(currentYear, currentMonth);

    // 月を変更する
    document.getElementById("prev").addEventListener("click", function () {
        currentMonth--;
        if (currentMonth === 0) {
            currentMonth = 12;
            currentYear--;
        }
        renderCalendar(currentYear, currentMonth);
    });

    document.getElementById("next").addEventListener("click", function () {
        currentMonth++;
        if (currentMonth === 13) {
            currentMonth = 1;
            currentYear++;
        }
        renderCalendar(currentYear, currentMonth);
    });
    
});
