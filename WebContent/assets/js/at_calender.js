// 現在の年月を初期化
if (typeof window.currentYear === "undefined" && typeof window.currentMonth === "undefined") {
    const now = new Date();
    window.currentYear = now.getFullYear();
    window.currentMonth = now.getMonth() + 1;
    console.log(`[DEBUG] Initialized currentYear=${window.currentYear}, currentMonth=${window.currentMonth}`);
}

console.log("Type of currentYear:", typeof currentYear);
console.log("Type of currentMonth:", typeof currentMonth);
console.log("Value of currentYear:", currentYear);
console.log("Value of currentMonth:", currentMonth);

document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");
    console.log(`[DEBUG] Initial currentYear=${currentYear}, currentMonth=${currentMonth}`);

    // reservationData のパース処理
    try {
        reservationData = JSON.parse(reservationDataRaw);
        console.log("[DEBUG] reservationData after parsing:", reservationData);
    } catch (error) {
        console.error("[ERROR] Failed to parse reservationDataRaw:", error);
    }

    updateCurrentMonthDisplay();
    generateCalendar();
});

// カレンダーの月を更新する関数
function updateCurrentMonthDisplay() {
    console.log(`[DEBUG] updateCurrentMonthDisplay called with currentYear=${window.currentYear}, currentMonth=${window.currentMonth}`);
    const currentMonthElement = document.getElementById("current-month");

    if (!currentMonthElement) {
        console.error("[ERROR] #current-month 要素が見つかりません！");
        return;
    }

    const monthNames = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
    currentMonthElement.textContent = `${window.currentYear}年 ${monthNames[window.currentMonth - 1]}`;
    console.log(`[DEBUG] 表示する月: ${window.currentYear}年 ${monthNames[window.currentMonth - 1]}`);
}

// カレンダーを生成する関数
function generateCalendar() {
    console.log("[DEBUG] generateCalendar function called");

    const daysInCurrentMonth = new Date(currentYear, currentMonth, 0).getDate();
    console.log(`[DEBUG] Recalculated daysInCurrentMonth=${daysInCurrentMonth}`);

    const calendarBody = document.getElementById("calendar-body");
    if (!calendarBody) {
        console.error("[ERROR] カレンダー本体が見つかりません！");
        return;
    } else {
        console.log("[DEBUG] カレンダー本体が見つかりました");
    }

    calendarBody.innerHTML = ""; // カレンダーのリセット
    let date = 1;
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
    console.log(`[DEBUG] 今月の最初の曜日 (0=日曜): ${firstDay}`);

    while (date <= daysInCurrentMonth) {
        const tr = document.createElement("tr");
        console.log(`[DEBUG] 日付 ${date} の行を作成中`);

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            if ((date === 1 && col < firstDay) || date > daysInCurrentMonth) {
                td.textContent = ""; // 空白セル
                console.log(`[DEBUG] 空白セル (列=${col})`);
            } else {
                const dayDiv = document.createElement("div");
                dayDiv.classList.add("calendar-day");
                dayDiv.textContent = date;

                const status = reservationData[String(date)] || "〇"; // デフォルトは"〇"
                console.log(`[DEBUG] 日付 ${date} のステータス: ${status}`);

                const statusDiv = document.createElement("div");
                statusDiv.classList.add("status");
                statusDiv.textContent = status;

                if (status === "〇") {
                    td.classList.add("clickable");
                    const currentDate = date;
                    td.addEventListener("click", () => {
                        let url;
                        if (livehouseType === "solo") {
                            url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}&livehouseId=${livehouseId}&livehouse_type=${livehouseType}`;
                        } else if (livehouseType === "multi") {
                            url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}&userId=${userId}&livehouseId=${livehouseId}&applicationId=${applicationId}&livehouse_type=${livehouseType}`;
                        } else {
                            console.error("[ERROR] 無効な livehouseType:", livehouseType);
                            return;
                        }
                        console.log(`[DEBUG] Generated URL: ${url}`);
                        window.location.href = url;
                    });
                } else {
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

    console.log("[DEBUG] カレンダー生成が完了しました");
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