<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<<%@ page import="java.util.List" %>
<%@ page import="model.Livehouse_application" %>
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
                    <li><a href="<%= request.getContextPath() %>/artist_mypage.html">MY PAGE</a></li>
                    <li><a href="#">000</a></li>
                    <li><a href="#">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <div><img src="<%= request.getContextPath() %>/assets/img/key-visual.jpg" alt=""></div>
        <div class="booking-title">
            <h2 class="booking-title-h2">Booking</h2>
        </div>
        <section class="booking-nav-section">
            <form action="" method="post">
                <div class="booking-button">
                    <div class="booking-solo-button">
    					<a href="<%= request.getContextPath() %>/At_Details" class="solo-button">SOLO LIVE</a>
					</div>
                    <div class="booking-multi-button">
                        <a href="<%= request.getContextPath() %>/At_Cogig" class="multi-button">MULTI LIVE</a>
                    </div>
                </div>
            </form>
        </section>
        <ul>
        	<li><p>aoooooooooooooo</p></li>
        </ul>
		
		<!-- applicationsリストが空でない場合 -->
    <c:if test="${not empty applications}">
        <table border="1">
            <tr>
                <th>申請ID</th>
                <th>ライブハウスID</th>
                <th>申請日</th>
                <th>開始時間</th>
                <th>終了時間</th>
                <th>コギグorソロ</th>
                <th>アーティストグループID</th>
            </tr>
            <!-- applicationsリストをループして表示 -->
            <c:forEach var="application" items="${applications}">
                <tr>
                    <td>${application.id}</td>
                    <td>${application.livehouse_information_id}</td>
                    <td>${application.date_time}</td>
                    <td>${application.start_time}</td>
                    <td>${application.finish_time}</td>
                    <td>${application.cogig_or_solo}</td>
                    <td>${application.artist_group_id}</td>
                </tr>
            </c:forEach>
            <p>Applications List: ${applications}</p>
        </table>
    </c:if>

    <!-- applicationsリストが空の場合 -->
    <c:if test="${empty applications}">
        <p>ライブハウス申請情報はありません。</p>
    </c:if>

		
</body>
</html>
