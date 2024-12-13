$(document).ready(function () {
    function drawCalendar(calendarData) {
        const currentDate = new Date();
        const currentMonth = currentDate.getMonth();
        const currentYear = currentDate.getFullYear();

        const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
        const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();

        const calendarGrid = $("#calendar");
        calendarGrid.empty();

        // 空セル
        for (let i = 0; i < firstDayOfMonth; i++) {
            calendarGrid.append('<div class="empty-cell"></div>');
        }

        // 各日を描画
        for (let day = 1; day <= daysInMonth; day++) {
            const dateKey = `${currentYear}-${String(currentMonth + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
            const status = calendarData[dateKey] || "空";

            const cellClass = status === "×" ? "booked" : "available";
            calendarGrid.append(`<div class="${cellClass}">${day}<br>${status}</div>`);
        }
    }

    drawCalendar(calendarData);
});
