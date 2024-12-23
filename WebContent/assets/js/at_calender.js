document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");

    console.log("[DEBUG] reservationDataRaw before parsing:", reservationDataRaw);

    // reservationData のパース処理
    try {
        reservationData = JSON.parse(reservationDataRaw);
        console.log("[DEBUG] reservationData after parsing:", reservationData);
    } catch (error) {
        console.error("[ERROR] Failed to parse reservationDataRaw:", error);
    }

    generateCalendar();
});

function generateCalendar() {
    console.log("[DEBUG] generateCalendar function called");

    // カレンダー本体要素の取得と確認
    const calendarBody = document.getElementById("calendar-body");
    if (!calendarBody) {
        console.error("[ERROR] カレンダー本体が見つかりません！");
        return;
    } else {
        console.log("[DEBUG] カレンダー本体が見つかりました");
    }

    // 現在の年、月、日数、予約データの確認
    console.log(`[DEBUG] 現在の年: ${currentYear}`);
    console.log(`[DEBUG] 現在の月: ${currentMonth}`);
    console.log(`[DEBUG] 今月の日数: ${daysInCurrentMonth}`);
    console.log("[DEBUG] 予約データ:", reservationData);
    console.log("[DEBUG] JSP livehouseType:", livehouseType);


    calendarBody.innerHTML = ""; // カレンダーのリセット
    let date = 1;
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
    console.log(`[DEBUG] 今月の最初の曜日 (0=日曜): ${firstDay}`);
    console.log(`[DEBUG] First day of the month: ${firstDay}`);
    console.log(`[DEBUG] Total days in the month: ${daysInCurrentMonth}`);

    while (date <= daysInCurrentMonth) {
        const tr = document.createElement("tr");
        console.log(`[DEBUG] 日付 ${date} の行を作成中`);

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            // 空白セルの確認
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

                // ステータスによるログ
                if (status === "〇") {
                    console.log(`[DEBUG] 日付 ${date}: 空きあり ("〇")`);
                } else if (status === "×") {
                    console.log(`[DEBUG] 日付 ${date}: 予約済み ("×")`);
                } else {
                    console.warn(`[WARNING] 日付 ${date} のステータスが不明: "${status}"`);
                }

                if (status === "〇") {
                    td.classList.add("clickable");
                    const currentDate = date;
                    console.log(`[DEBUG] 日付 ${currentDate} はクリック可能です`);

                    td.addEventListener("click", () => {
					    let url;
					
					    if (livehouseType === "solo") {
						    // ソロライブの場合
						    url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}&livehouseId=${livehouseId}&livehouse_type=${livehouseType}`;
						} else if (livehouseType === "multi") {
						    // マルチライブの場合
						    url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}&userId=${userId}&livehouseId=${livehouseId}&applicationId=${applicationId}&livehouse_type=${livehouseType}`;
						} else {
						    // 無効なタイプの場合
						    console.error("[ERROR] 無効な livehouseType:", livehouseType);
						    return;
						}
						console.log("[DEBUG] livehouseType for URL:", livehouseType);
						console.log("[DEBUG] Generated URL:", url);

					    console.log(`[DEBUG] URL にリダイレクトします: ${url}`);
					    window.location.href = url;
					});

                } else {
                    td.classList.add("non-clickable");
                    console.log(`[DEBUG] 日付 ${date} はクリック不可です`);
                }

                td.appendChild(dayDiv);
                td.appendChild(statusDiv);
                date++;
            }

            tr.appendChild(td);
            console.log(`[DEBUG] 列=${col}, 日付=${date} のセルを追加`);
        }

        calendarBody.appendChild(tr);
        console.log("[DEBUG] 行をカレンダーに追加しました");
    }

    console.log("[DEBUG] カレンダー生成が完了しました");
}
