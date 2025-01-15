console.log("Context Path:", contextPath);
console.log("Reservation Data Raw:", reservationDataRaw);
console.log("Current Year:", currentYear);
console.log("Current Month:", currentMonth);

document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");

    let year = currentYear;
    let month = currentMonth;

    console.log(`[DEBUG] 初期年月: ${year}/${month}`);

    const prevButton = document.getElementById("prev-month");
    const nextButton = document.getElementById("next-month");
    const currentMonthDisplay = document.getElementById("current-month");

    if (!prevButton || !nextButton) {
        console.error("[ERROR] ボタンが正しく設定されていません");
        return;
    }

    // 初回表示用にreservationDataRawをパース
    let reservationData = {};

    try {
        if (typeof reservationDataRaw === 'string') {
            console.log("[DEBUG] reservationDataRaw is string:", reservationDataRaw);
            reservationData = JSON.parse(reservationDataRaw);
        } else if (typeof reservationDataRaw === 'object') {
            console.log("[DEBUG] reservationDataRaw is already object:", reservationDataRaw);
            reservationData = reservationDataRaw;
        } else {
            throw new Error("Invalid data format for reservationDataRaw");
        }

        console.log("[DEBUG] Parsed reservationData:", reservationData);

    } catch (error) {
        console.error("[ERROR] Failed to parse reservationDataRaw:", error);
        reservationData = {};
    }

    // カレンダー初期化
    updateCurrentMonthDisplay(year, month);
    generateCalendar(year, month, reservationData);

    // 前の月ボタンの処理
    prevButton.addEventListener("click", () => {
        console.log("[DEBUG] 前の月ボタンがクリックされました");
        month--;
        if (month < 1) {
            month = 12;
            year--;
        }
        updateCurrentMonthDisplay(year, month);
        fetchReservationData(year, month);
    });

    // 次の月ボタンの処理
    nextButton.addEventListener("click", () => {
        console.log("[DEBUG] 次の月ボタンがクリックされました");
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
        updateCurrentMonthDisplay(year, month);
        fetchReservationData(year, month);
    });

    // 月の表示を更新する関数
    function updateCurrentMonthDisplay(year, month) {
        if (currentMonthDisplay) {
            currentMonthDisplay.textContent = `${year}年 ${month}月`;
        }
    }

    // サーバーから予約データを取得する関数
    function fetchReservationData(year, month) {
        console.log(`[DEBUG] データ取得開始 - year: ${year}, month: ${month}`);

        fetch(`${contextPath}/Livehouse_home?year=${year}&month=${month}&timestamp=${new Date().getTime()}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("[ERROR] サーバーからのレスポンスが不正です");
            }
            return response.json();
        })
        .then(data => {
            console.log("[DEBUG] サーバーからのデータ取得成功:", data);
            generateCalendar(year, month, data);
        })
        .catch(error => {
            console.error("[ERROR] データ取得に失敗しました:", error);
            alert("データの取得に失敗しました。しばらくしてから再度お試しください。");
        });
    }

   // カレンダーを生成する関数
function generateCalendar(year, month, reservationData) {
    console.log("[DEBUG] カレンダー生成開始");

    const calendarBody = document.getElementById("calendar-body");
    if (!calendarBody) {
        console.error("[ERROR] カレンダー本体が見つかりません");
        return;
    }

    calendarBody.innerHTML = "";

    const daysInMonth = new Date(year, month, 0).getDate();  // 月の日数を取得
    const firstDay = new Date(year, month - 1, 1).getDay();  // 月の初日曜日の曜日番号

    let date = 1;

    while (date <= daysInMonth) {
        const tr = document.createElement("tr");

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            if ((date === 1 && col < firstDay) || date > daysInMonth) {
                td.textContent = "";
            } else {
                const dayDiv = document.createElement("div");
                dayDiv.classList.add("calendar-day");
                dayDiv.textContent = date;

                const applicationCount = reservationData[String(date)] || 0;
                const statusDiv = document.createElement("div");
                statusDiv.classList.add("status");

                // 正しい day 属性を設定する
                td.setAttribute('data-year', year);
                td.setAttribute('data-month', month);
                td.setAttribute('data-day', date);  // ここで日付をセット

                if (applicationCount > 0) {
                    statusDiv.textContent = `${applicationCount}件`;
                    td.classList.add("clickable");

                    // クリックイベントに openReservationList 関数を設定
                    td.addEventListener("click", () => openReservationList(td));
                } else {
                    statusDiv.textContent = "×";
                    td.classList.add("non-clickable");
                }

                td.appendChild(dayDiv);
                td.appendChild(statusDiv);
                date++;
            }

            tr.appendChild(td);
        }

        calendarBody.appendChild(tr);
    }

    console.log("[DEBUG] カレンダー生成完了");
}


// Application_list サーブレット遷移
function openReservationList(element) {
    const year = element.getAttribute('data-year');  // 年
    const month = element.getAttribute('data-month');  // 月
    const day = element.getAttribute('data-day');  // クリックした日付

    if (day > 0 && day <= 31 && month >= 1 && month <= 12) {
        // Application_list に遷移するURLを生成
        const url = `${contextPath}/Application_list?year=${year}&month=${month}&day=${day}`;
        console.log(`[DEBUG] リダイレクト先: ${url}`);
        window.location.href = url;  // 遷移するURLにリダイレクト
    } else {
        console.error("[ERROR] 無効な日付が指定されています");
        alert("無効な日付です。");
    }
}

});