function generateCalendar() {
    console.log("[DEBUG] generateCalendar function called");
	console.log("Reservation Status (Parsed):", reservationStatus);
	console.log("Days in Month:", daysInMonth);
	console.log("Year:", year);
	console.log("Month:", month);

    console.log(`[DEBUG] daysInCurrentMonth: ${daysInCurrentMonth}`);

    calendarBody.innerHTML = ""; // カレンダーをリセット

    let date = 1;
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
    console.log("[DEBUG] First day of the month (weekday):", firstDay);

    while (date <= daysInCurrentMonth) {
        const tr = document.createElement("tr");

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            if ((date === 1 && col < firstDay) || date > daysInCurrentMonth) {
                td.textContent = ""; // 空白セル
            } else {
                console.log(`[DEBUG] Generating cell for date: ${date}`);

                const dayDiv = document.createElement("div");
                dayDiv.classList.add("calendar-day");
                dayDiv.textContent = date;

                const status = reservationData[date] || "〇";
                console.log(`[DEBUG] Status for day ${date}:`, status);

                const statusDiv = document.createElement("div");
                statusDiv.classList.add("status");
                statusDiv.textContent = status;

                if (status === "〇") {
                    td.classList.add("clickable");
                    td.addEventListener("click", () => {
                        const url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${date}&userId=${userId}`;
                        console.log("[DEBUG] Click event triggered for URL:", url);
                        window.location.href = url;
                    });
                } else {
                    td.classList.add("non-clickable");
                }

                td.appendChild(dayDiv);
                td.appendChild(statusDiv);

                date++; // 次の日付
                console.log(`[DEBUG] Incremented date: ${date}`);
            }

            tr.appendChild(td);

            if (date > daysInCurrentMonth) {
                console.log(`[DEBUG] Exiting inner loop: date=${date}, daysInCurrentMonth=${daysInCurrentMonth}`);
                break; // 月の日数を超えた場合、ループを終了
            }
        }

        calendarBody.appendChild(tr);
    }

    console.log("[DEBUG] Calendar generated successfully");
}
