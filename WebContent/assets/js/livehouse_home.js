// ページ読み込み後の初期化
document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");

    // サーバーから渡された予約データを解析
    let reservationData = {};
    try {
        reservationData = JSON.parse(reservationDataRaw); // サーバーから渡される JSON 文字列
        console.log("[DEBUG] Parsed reservationData:", reservationData);
    } catch (error) {
        console.error("[ERROR] Failed to parse reservationDataRaw:", error);
        reservationData = {}; // パース失敗時は空のオブジェクト
    }

    // カレンダーを生成
    generateCalendar(reservationData);
});

// カレンダーを生成する関数
function generateCalendar(reservationData) {
    console.log("[DEBUG] generateCalendar function called");

    // カレンダー本体要素を取得
    const calendarBody = document.getElementById("calendar-body");
    if (!calendarBody) {
        console.error("[ERROR] カレンダー本体が見つかりません！");
        return;
    }

    // カレンダーをリセット
    calendarBody.innerHTML = "";

    // 現在の年月と月の日数を取得
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1; // 月は0始まりなので +1
    const daysInCurrentMonth = new Date(currentYear, currentMonth, 0).getDate();
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();

    console.log(`[DEBUG] 現在の年: ${currentYear}, 現在の月: ${currentMonth}, 今月の日数: ${daysInCurrentMonth}`);
    console.log(`[DEBUG] 今月の最初の曜日 (0=日曜): ${firstDay}`);

    let date = 1;

    // カレンダーを行単位で作成
    while (date <= daysInCurrentMonth) {
        const tr = document.createElement("tr");

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            if ((date === 1 && col < firstDay) || date > daysInCurrentMonth) {
                // 空白セル
                td.textContent = "";
            } else {
                // 日付セル
                const dayDiv = document.createElement("div");
                dayDiv.classList.add("calendar-day");
                dayDiv.textContent = date;

                // 申請件数を取得
                const applicationCount = reservationData[String(date)] || 0; // 申請件数（デフォルトは0）
                const statusDiv = document.createElement("div");
                statusDiv.classList.add("status");

                if (applicationCount > 0) {
                    // 申請件数が1以上の場合
                    statusDiv.textContent = `${applicationCount}件`;
                    td.classList.add("clickable");

                    const currentDate = date;

                    // クリックイベントの設定
                    td.addEventListener("click", () => {
                        const url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}`;
                        console.log(`[DEBUG] URL にリダイレクトします: ${url}`);
                        window.location.href = url;
                    });
                } else {
                    // 申請件数が0の場合
                    statusDiv.textContent = "×";
                    td.classList.add("non-clickable");
                }

                // 日付とステータスをセルに追加
                td.appendChild(dayDiv);
                td.appendChild(statusDiv);
                date++;
            }

            // セルを行に追加
            tr.appendChild(td);
        }

        // 行をカレンダー本体に追加
        calendarBody.appendChild(tr);
    }

    console.log("[DEBUG] カレンダー生成が完了しました");
}
