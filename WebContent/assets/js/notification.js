// 通知を取得する関数（JSP から通知データを受け取る）
function fetchNotifications(notificationsJson) {
    try {
        const notifications = JSON.parse(notificationsJson);
        console.log("[DEBUG] 取得した通知データ:", notifications);

        if (notifications && notifications.length > 0) {
            let content = "";
            notifications.forEach(notification => {
                const date = new Date(notification.createDate);  // JavaScript で日付変換
                const formattedDate = date.toLocaleString();  // ローカルタイム形式に変換
                content += `<p>${notification.message} - ${formattedDate}</p>`;
            });

            document.getElementById("notification-content").innerHTML = content;
            document.getElementById("notification-popup").style.display = "block";
        }
    } catch (error) {
        console.error("[ERROR] 通知データのパースに失敗しました:", error);
    }
}

//  通知ポップアップを閉じる関数
function closeNotification() {
    document.getElementById("notification-popup").style.display = "none";
}

// ページが読み込まれたときに通知を表示
document.addEventListener("DOMContentLoaded", function () {
    const notificationsJson = document.getElementById("notifications-data").textContent;
    fetchNotifications(notificationsJson);
});
