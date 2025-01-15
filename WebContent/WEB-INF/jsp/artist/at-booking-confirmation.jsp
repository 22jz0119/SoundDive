<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>


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
                    <li><a href="<%= request.getContextPath() %>/At_Home">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/At_Mypage">MY PAGE</a></li>
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="A-t-Booking-Confirmation-main">
        <section class="a-t-reservation-confirmation">
            <div class="a-t-confirmation-header">
                <h1 class="a-t-confirmation-title">予約確認</h1>
            </div>
            
            <h2 class="a-t-details-title">予約詳細</h2>
            <div class="a-t-reservation-details">
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1">
                        <p class="a-t-info-label">予約名義</p>
                    </li>
                    <li class="a-t-reservation-info-item2">
                        <p class="a-t-info-value" id="reservation-name">${userName}</p>
                    </li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">希望日時</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${selectedYear}年${selectedMonth}月${selectedDay}日</p></li>
                </ul>
                 <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">希望時間</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${selectedTime}</p></li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">電話番号</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">${telNumber}</p></li>
                </ul>
            </div>
            <div>
            <c:if test="${livehouseType eq 'multi'}">
                <h2>予約アーティスト</h2>
	            <div class="a-t-booking-gourp-main">
	                <div class="a-t-booking-group-containar">
	                    <ul class="a-t-booking-group-frame">
	                        <li><img src="${pageContext.request.contextPath}${userGroup.picture_image_movie}" id="a-t-booking-group-img" alt="Profile Image" />
	                    </ul>
	                    <ul class="a-t-booking-group-frame">
	                        <li class="a-t-booking-group-li1"><p>アーティスト名</p></li>
	                        <li class="a-t-booking-group-li2"><p>${account_Name}</p></li>
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
            </c:if>

            <h2 class="a-t-booking-place-title">予約場所</h2>
            <div class="a-t-booking-place-main-div">
                <div class="a-t-booking-place-containar1">
                    <img src="${pageContext.request.contextPath}${livehouse.picture_image_naigaikan}" class="a-t-venue-image" alt="" />
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>${livehouseInfo.livehouse_name}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>オーナー</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouse.owner_name}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>アドレス</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouse.live_address}</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>電話番号</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouse.live_tel_number}</p></li>
                    </ul>
                </div>
                <div class="a-t-booking-place-containar2">
                    <ul class="a-t-booking-place-ul2">
                        <li class="a-t-booking-place1-li1"><p>説明</p></li>
                        <li class="a-t-booking-place1-li2"><p>${livehouse.livehouse_explanation_information}</p></li>
                    </ul>
                </div>
            </div>
        </section>
		
        <form action="<%= request.getContextPath() %>/At_livehouse_reservation_completed" method="post">
		    <input type="hidden" name="year" value="${selectedYear}">
		    <input type="hidden" name="month" value="${selectedMonth}">
		    <input type="hidden" name="day" value="${selectedDay}">
		    <input type="hidden" name="time" value="${selectedTime}">
		    <input type="hidden" name="livehouseId" value="${livehouseId}">
		    <input type="hidden" name="livehouse_type" value="${livehouseType}">
		    <input type="hidden" name="userId" value="${userId}">
		    
		    <c:if test="${livehouseType eq 'multi'}">
		        <input type="hidden" name="applicationId" value="${applicationId}">
		    </c:if>
		    
		    <button type="submit" class="btn">予約を確定する</button>
		</form>
    </main>
</body>
</html>
