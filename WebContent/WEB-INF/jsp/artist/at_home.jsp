<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Notice" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">

    <title>アーティストホーム画面</title>
</head>

<body class="artist_home">
    <header class="main-header">
        <div class="header-container2">
            <div class="main-title2">
                <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="" class="main-logo2">
            </div>
            <ul class="header-nav-ul2">
                <li class="header-box-li2"><a href="<%= request.getContextPath() %>/At_Mypage" class="top-mypage-btn">MY PAGE</a></li>
                <li class="notification-container header-box-li2">
                    <a href=" #" class="notification-icon" onclick="toggleNotificationWindow(event)">
                        <% 
						    List<Notice> notifications = (List<Notice>) request.getAttribute("notifications");
						    long unreadCount = 0;
						    if (notifications != null) {
						        for (Notice notice : notifications) {
						            if (!notice.isRead()) {
						                unreadCount++;
						            }
						        }
						    }
						%>
                        <span id="notificationCount">通知<%= unreadCount %>件</span>
                    </a>
                    <div class="notification-window" id="notificationWindow" style="display: none;">
                        <ul id="notificationList">
						    <% if (notifications != null) {
						        for (Notice notice : notifications) { %>
						            <li class="<%= notice.isRead() ? "read" : "unread" %>">
						                <span class="tuuti"><%= notice.getMessage() != null ? notice.getMessage() : "メッセージがありません" %></span>
						                <button class="notification-button" onclick="markAsRead(<%= notice.getId() %>)">既読</button>
						            </li>
						    <%  } 
						    } else { %>
						        <li class="tuuti">通知はありません。</li>
						    <% } %>
						</ul>
                    </div>
                </li>
                <li class="header-box-li2"><a href="#" onclick="logoutAndRedirect();" class="top-logout-btn">ログアウト</a></li>
            </ul>

        </div>
    </header>
    <main>
        <div class="a-t-home-keyvisual-div"><img src="<%= request.getContextPath() %>/assets/img/key-visual.jpg" alt="" class="a-t-home-key-visual"></div>
        <div class="booking-title">
            <h2 class="booking-title-h2">Booking</h2>
        </div>
        <section class="booking-nav-section">
            <form action="<%= request.getContextPath() %>/At_Home" method="post">
                <div class="booking-button">
                    <div class="booking-solo-button">
                        <button type="submit" name="action" value="solo" class="solo-button">SOLO LIVE</button>
                    </div>
                    <div class="booking-multi-button">
                        <button type="submit" name="action" value="multi" class="multi-button">MULTI LIVE</button>
                    </div>
                </div>
            </form>
        </section>

        <section class="booking-status-section">
            <p class="at-home-bs-title">ライブ予約状況</p>
            <div class="at-homr-status-containar">
                <p class="at-home-bs-done">ライブ予約完了</p>
                <c:forEach var="app" items="${applicationsTrue}">
                    <div class="at-home-bs-done-frame">
                        <img src="${app.livehouse_information.picture_image_naigaikan}" alt="Livehouse Image" width="100px" height="100px"/>
                        <ul>
                            <li>${app.livehouse_information.livehouse_name}</li>
                            <li>予約日</li>
                            <li>${app.date_time}</li>
                        </ul>
                        <ul>
                            <li>${app.livehouse_information.live_address}</li>
                            <li>電話番号</li>
                            <li>${app.livehouse_information.live_tel_number}</li>
                        </ul>
                    </div>
                </c:forEach>
                <h2>予約申請中</h2>
                <c:forEach var="app" items="${applicationsFalse}">
                    <div class="at-home-bs-request-frame">
                        <img src="${app.livehouse_information.picture_image_naigaikan}" alt="Livehouse Image" width="100px" height="100px"/>
                        <ul>
                            <li>${app.livehouse_information.livehouse_name}</li>
                            <li>予約日</li>
                            <li>${app.date_time}</li>
                        </ul>
                        <ul>
                            <li>${app.livehouse_information.live_address}</li>
                            <li>電話番号</li>
                            <li>${app.livehouse_information.live_tel_number}</li>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </section>
     </main>

     <script>
        function toggleNotificationWindow(event) {
            event.preventDefault();
            const notificationWindow = document.getElementById('notificationWindow');

            if (notificationWindow.style.display === 'none' || !notificationWindow.style.display) {
                notificationWindow.style.display = 'block';
                fetchNotifications();
            } else {
                notificationWindow.style.display = 'none';
            }
        }

        function fetchNotifications() {
        	fetch('<%= request.getContextPath() %>/At_Home?action=getNotifications')
                .then(response => response.json())
                .then(data => {
                    const notificationList = document.getElementById('notificationList');
                    notificationList.innerHTML = '';

                    if (data.notifications.length === 0) {
                        notificationList.innerHTML = '<li>通知はありません</li>';
                        return;
                    }

                    data.notifications.forEach(notification => {
                        const listItem = document.createElement('li');
                        listItem.classList.add(notification.is_read ? 'read' : 'unread');
                        listItem.textContent = notification.message;

                        listItem.setAttribute('data-id', notification.id);
                        listItem.setAttribute('data-message', notification.message);
                        listItem.onclick = () => showNotificationDetail(notification.id, notification.message);

                        notificationList.appendChild(listItem);
                    });
                });
        }

        function markAsRead(noticeId) {
            const params = new URLSearchParams({
                action: 'markAsRead',
                noticeId: noticeId
            });

            fetch('<%= request.getContextPath() %>/At_Home', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('既読リクエストが失敗しました');
                }
                // DOMを更新して通知を既読にする
                const listItem = document.querySelector(`li[data-id='${noticeId}']`);
                if (listItem) {
                    listItem.classList.remove('unread');
                    listItem.classList.add('read');
                }
            })
            .catch(error => console.error('エラー:', error));
        }


        function closeNotificationDetail() {
            const detailWindow = document.querySelector('.notification-detail-window');
            if (detailWindow) {
                detailWindow.remove();
            }
        }

        function logoutAndRedirect() {
            var form = document.createElement("form");
            form.method = "post";
            form.action = "<%= request.getContextPath() %>/At_Home";

            var input = document.createElement("input");
            input.type = "hidden";
            input.name = "action";
            input.value = "logout";
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
        }
    </script>
</body>
</html>
