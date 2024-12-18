document.addEventListener("DOMContentLoaded", () => {
    console.log("[DEBUG] DOMContentLoaded event fired");

    generateCalendar();
});

function generateCalendar() {
    console.log("[DEBUG] generateCalendar function called");
    console.log("applicationId:", applicationId);


    const calendarBody = document.getElementById("calendar-body");
    if (!calendarBody) {
        console.error("[ERROR] Calendar body not found!");
        return;
    }

    calendarBody.innerHTML = ""; // Reset calendar
    let date = 1;
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
    console.log("[DEBUG] First day of the month (weekday):", firstDay);

    while (date <= daysInCurrentMonth) {
        const tr = document.createElement("tr");
        console.log(`[DEBUG] Creating row for date=${date}`);

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");
            td.classList.add("calendar-cell");

            if ((date === 1 && col < firstDay) || date > daysInCurrentMonth) {
                td.textContent = ""; // Empty cell
                console.log(`[DEBUG] Empty cell at col=${col}`);
            } else {
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
                    const currentDate = date; // 必ずイベント内でこの変数を使う
                    console.log(`[DEBUG] Day ${currentDate} is clickable`);
                    td.addEventListener("click", () => {
                        const url = `${contextPath}/At_Reservation?year=${currentYear}&month=${currentMonth}&day=${currentDate}&userId=${userId}&livehouseId=${livehouseId}&applicationId=${applicationId}`;
                        console.log(`[DEBUG] Redirecting to: ${url}`);
                        window.location.href = url;
                    });
                } else {
                    td.classList.add("non-clickable");
                    console.log(`[DEBUG] Day ${date} is non-clickable`);
                }

                td.appendChild(dayDiv);
                td.appendChild(statusDiv);
                date++;
            }

            tr.appendChild(td);
            console.log(`[DEBUG] Added cell for col=${col}, date=${date}`);
        }

        calendarBody.appendChild(tr);
        console.log("[DEBUG] Row added");
    }

    console.log("[DEBUG] Calendar generation complete");
}
