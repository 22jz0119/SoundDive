console.log("Context Path:", contextPath);
console.log("Reservation Data Raw:", reservationDataRaw);
console.log("Current Year:", currentYear);
console.log("Current Month:", currentMonth);

document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");

    let year = currentYear;
    let month = currentMonth;

    console.log(`[DEBUG] 初期年月: ${year}/${month}`);

    // ✅ ボタンID修正: prev -> prev-month, next -> next-month
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

    // ✅ 月の表示を更新する関数
    function updateCurrentMonthDisplay(year, month) {
        if (currentMonthDisplay) {
            currentMonthDisplay.textContent = `${year}年 ${month}月`;
        }
    }

    // ✅ サーバーから予約データを取得する関数
    function fetchReservationData(year, month) {
        console.log(`[DEBUG] データ取得開始 - year: ${year}, month: ${month}`);

        // ✅ キャッシュ防止のためにtimestamp追加
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

        const daysInMonth = new Date(year, month, 0).getDate();
        const firstDay = new Date(year, month - 1, 1).getDay();

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

                    if (applicationCount > 0) {
                        statusDiv.textContent = `${applicationCount}件`;
                        td.classList.add("clickable");

                        td.addEventListener("click", () => {
                            const url = `${contextPath}/At_Reservation?year=${year}&month=${month}&day=${date}`;
                            console.log(`[DEBUG] URL にリダイレクトします: ${url}`);
                            window.location.href = url;
                        });
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
    // 新しい予約データを取得する関数
async function fetchReservationData(year, month) {
    const url = `${contextPath}/At_details?action=getReservationData&year=${year}&month=${month}&livehouseId=${livehouseId}&livehouse_type=${livehouseType}`;
    console.log(`[DEBUG] Fetching URL: ${url}`);
    try {
        const response = await fetch(url);
        console.log("[DEBUG] Response Content-Type:", response.headers.get("Content-Type"));
        const textResponse = await response.text();
        console.log("[DEBUG] Raw Response:", textResponse);

        if (!response.ok) {
            throw new Error(`Failed to fetch reservation data: ${response.statusText}`);
        }

        return JSON.parse(textResponse); // JSON形式でパース
    } catch (error) {
        console.error("[ERROR] Failed to fetch reservation data:", error);
        return {}; // エラー時は空のオブジェクトを返す
    }
}

// 月切り替え処理
const prevMonthButton = document.getElementById("prev-month");
const nextMonthButton = document.getElementById("next-month");

// 前の月ボタンのクリック処理
prevMonthButton.addEventListener("click", async () => {
    // 月の更新処理
    if (window.currentMonth === 1) {
        window.currentMonth = 12;
        window.currentYear--;
    } else {
        window.currentMonth--;
    }

    console.log(`[DEBUG] After prev-month click: currentYear=${window.currentYear}, currentMonth=${window.currentMonth}`);

    // 新しい予約データを取得して `reservationData` を更新
    reservationData = await fetchReservationData(window.currentYear, window.currentMonth);
    console.log("[DEBUG] Updated reservationData:", reservationData);

    // 表示を更新
    updateCurrentMonthDisplay();
    generateCalendar(); // 更新されたデータを使用して再描画
});

// 次の月ボタンのクリック処理
nextMonthButton.addEventListener("click", async () => {
    // 月の更新処理
    if (window.currentMonth === 12) {
        window.currentMonth = 1;
        window.currentYear++;
    } else {
        window.currentMonth++;
    }
    console.log(`[DEBUG] After next-month click: currentYear=${window.currentYear}, currentMonth=${window.currentMonth}`);

    // 新しい予約データを取得して `reservationData` を更新
    reservationData = await fetchReservationData(window.currentYear, window.currentMonth);
    console.log("[DEBUG] Updated reservationData:", reservationData);

    // 表示を更新
    updateCurrentMonthDisplay();
    generateCalendar(); // 更新されたデータを使用して再描画
});
});
