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
	        				<li>
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
	        				<li>
	        				<li>${app.livehouse_information.live_address}</li>
	        				<li>${app.livehouse_information.live_tel_number}</li>
	        			</ul>
        			</div>
        			
        		</c:forEach>
        		
        	</div>
        </section>
        
       
        
     </main>   
       
    
</body>
</html>
