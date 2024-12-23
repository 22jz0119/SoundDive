<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        
        <table border="1">
	        <thead>
	            <tr>
	                <th>ID</th>
	                <th>ライブハウス情報</th>
	                <th>申請日時</th>
	                <th>予約状態</th>
	                <th>開始日時</th>
	                <th>終了日時</th>
	                <th>コギグ/ソロ</th>
	                <th>アーティストグループID</th>
	            </tr>
	        </thead>
	        <tbody>
	            <c:forEach var="app" items="${applications}">
	                <tr>
	                    <!-- 申請ID -->
	                    <td>${app.id}</td>
	
	                    <!-- ライブハウス情報 -->
	                    <td>
	                        <c:choose>
	                            <c:when test="${not empty app.livehouse_information}">
	                                ${app.livehouse_information.livehouse_name} (住所: ${app.livehouse_information.live_address})
	                            </c:when>
	                            <c:otherwise>
	                                情報なし
	                            </c:otherwise>
	                        </c:choose>
	                    </td>
	
	                    <!-- 申請日時 -->
	                    <td>${app.date_time}</td>
	
	                    <!-- 予約状態 -->
	                    <td>
	                        <c:choose>
	                            <c:when test="${app.isTrue_False()}">
	                                予約完了
	                            </c:when>
	                            <c:otherwise>
	                                予約申請待ち
	                            </c:otherwise>
	                        </c:choose>
	                    </td>
	
	                    <!-- 開始日時 -->
	                    <td>${app.start_time != null ? app.start_time : '未設定'}</td>
	
	                    <!-- 終了日時 -->
	                    <td>${app.finish_time != null ? app.finish_time : '未設定'}</td>
	
	                    <!-- コギグ/ソロ -->
	                    <td>${app.cogig_or_solo == 1 ? 'ソロ' : 'コギグ'}</td>
	
	                    <!-- アーティストグループID -->
	                    <td>${app.artist_group_id}</td>
	                </tr>
	            </c:forEach>
	        </tbody>
	    </table>
        
     </main>   
       
    
</body>
</html>
