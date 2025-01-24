<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        <div class="header-container">
            <div class="main-title">
                <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="" class="main-logo">
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
				    <li><a href="<%= request.getContextPath() %>/At_Mypage">MY PAGE</a></li>
				    <li class="notification-container">
				        <a href="#" class="notification-icon" onclick="toggleNotificationWindow(event)">000</a>
				        <div class="notification-window" id="notificationWindow">
				            <ul id="notificationList">
				                <!-- 通知リストがここに追加されます -->
				            </ul>
				        </div>
				    </li>
				    <li><a href="#">000</a></li>
				    <li><a href="#" onclick="logoutAndRedirect();">ログアウト</a></li>
				</ul>
            </nav>
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
			            <!-- SOLO LIVE ボタン -->
			            <button type="submit" name="action" value="solo" class="solo-button">SOLO LIVE</button>
			        </div>
			        <div class="booking-multi-button">
			            <!-- MULTI LIVE ボタン -->
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
            
            // ウィンドウの表示/非表示を切り替え
            if (notificationWindow.style.display === 'none' || !notificationWindow.style.display) {
                notificationWindow.style.display = 'block';
                fetchNotifications(); // 通知リストを取得
            } else {
                notificationWindow.style.display = 'none';
            }
        }

        function fetchNotifications() {
            fetch('<%= request.getContextPath() %>/GetNotifications') // 通知データを取得するエンドポイント
                .then(response => response.json())
                .then(data => {
                    const notificationList = document.getElementById('notificationList');
                    notificationList.innerHTML = ''; // リストをクリア

                    if (data.notifications.length === 0) {
                        notificationList.innerHTML = '<li>通知はありません</li>';
                        return;
                    }

                    data.notifications.forEach(notification => {
                        const listItem = document.createElement('li');
                        listItem.classList.add(notification.is_read ? 'read' : 'unread');
                        listItem.textContent = notification.message;

                        // 通知クリック時に詳細表示するためのイベントを追加
                        listItem.setAttribute('data-id', notification.id); // 通知IDを設定
                        listItem.setAttribute('data-message', notification.message); // 通知メッセージを設定
                        listItem.onclick = () => showNotificationDetail(notification.id, notification.message);

                        notificationList.appendChild(listItem);
                    });
                });
        }

        function closeNotificationDetail() {
            const detailWindow = document.querySelector('.notification-detail-window');
            if (detailWindow) {
                detailWindow.remove();
            }
        }
                

        function logoutAndRedirect() {
            // フォームを送信してログアウト処理を実行
            var form = document.createElement("form");
            form.method = "post";
            form.action = "<%= request.getContextPath() %>/At_Home";
            
            // 隠しフィールドにaction=logoutをセット
            var input = document.createElement("input");
            input.type = "hidden";
            input.name = "action";
            input.value = "logout";
            form.appendChild(input);
            
            // フォームを送信
            document.body.appendChild(form);
            form.submit();
        }
    </script>
</body>
</html>
