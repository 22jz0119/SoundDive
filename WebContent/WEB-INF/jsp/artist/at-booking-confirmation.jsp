<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>予約確認ページ</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body>
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="artist_home.html">HOME</a></li>
                    <li><a href="artist_mypage.html">MY PAGE</a></li>
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="A-t-Booking-Confirmation-main">
        <!-- Debug 1: Livehouse type -->
        <p>[DEBUG 1] livehouseType: ${livehouseType}</p>
        <p>[DEBUG 1] Is Solo: <c:out value="${livehouseType eq 'solo'}" /></p>
        <p>[DEBUG 1] Is Multi: <c:out value="${livehouseType eq 'multi'}" /></p>

        <section class="a-t-reservation-confirmation">
            <div class="a-t-confirmation-header">
                <h1 class="a-t-confirmation-title">予約確認</h1>
            </div>
            
            <h2 class="a-t-details-title">予約詳細</h2>
            <div class="a-t-reservation-details">
                <!-- Debug 2: Selected date and time -->
                <p>[DEBUG 2] Selected Date: ${selectedYear}/${selectedMonth}/${selectedDay}</p>
                <p>[DEBUG 2] Selected Time: ${selectedTime}</p>
                
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1">
                        <p class="a-t-info-label">予約名義</p>
                    </li>
                    <li class="a-t-reservation-info-item2">
                        <p class="a-t-info-value" id="reservation-name">${user.name}</p>
                    </li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">希望日時</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${selectedYear}年${selectedMonth}月${selectedDay}日</p></li>
                </ul>
                 <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">希望時間</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${application.start_time}</p></li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">電話番号</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${user.tel_number}</p></li>
                </ul>
            </div>
            <div>
                <h2>予約アーティスト</h2>
            <div class="a-t-booking-gourp-main">
                <div class="a-t-booking-group-containar">
                    <ul class="a-t-booking-group-frame">
                        <li><img src="" alt="" id="a-t-booking-group-img"></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>アーティスト名</p></li>
                        <li class="a-t-booking-group-li2"><p>${artistGroup.account_name}</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>バンド歴</p></li>
                        <li class="a-t-booking-group-li2"><p>${artistGroup.band_years}</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>評価</p></li>
                        <li class="a-t-booking-group-li2"><p>${artistGroup.rating_star}</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>ジャンル</p></li>
                        <li class="a-t-booking-group-li2"><p>${artistGroup.group_genre}</p></li>
                    </ul>
                </div>
            </div>

            <h2 class="a-t-booking-place-title">予約場所</h2>
            <div class="a-t-booking-place-main-div">
                <div class="a-t-booking-place-containar1">
                    <img src="" alt="" class="a-t-venue-image">
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>${livehouseInfo.livehouse_name}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>オーナー</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouseInfo.owner_name}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>アクセス</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouseInfo.live_address}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>電話番号</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouseInfo.live_tel_number}</p></li>
                    </ul>
                </div>
                <div class="a-t-booking-place-containar2">
                    <ul class="a-t-booking-place-ul2">
                        <li class="a-t-booking-place1-li1"><p>説明</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouseInfo.livehouse_explanation_information}</p></li>
                    </ul>
                </div>
            </div>
        </section>
		
		<!-- Debug 3: Form submission details -->
        <p>[DEBUG 3] Form details:</p>
        <form action="<%= request.getContextPath() %>/At_livehouse_reservation_completed" method="post">
		    <input type="hidden" name="year" value="${selectedYear}">
		    <input type="hidden" name="month" value="${selectedMonth}">
		    <input type="hidden" name="day" value="${selectedDay}">
		    <input type="hidden" name="time" value="${selectedTime}">
		    <input type="hidden" name="livehouseId" value="${livehouseId}">
		    <input type="hidden" name="livehouse_type" value="${livehouseType}">
		
		    <!-- マルチ用データ -->
		    <c:if test="${livehouseType eq 'multi'}">
		        <input type="hidden" name="userId" value="${user.id}">
		        <input type="hidden" name="applicationId" value="${application.id}">
		    </c:if>
		
		    <!-- このボタンでのみフォーム送信 -->
		    <button type="submit" class="btn">予約を確定する</button>
		</form>



    </main>
</body>
</html>
