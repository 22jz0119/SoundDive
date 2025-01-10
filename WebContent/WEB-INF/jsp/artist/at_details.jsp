<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*" %>
<%@ page import="model.Livehouse_information" %>
<%@ page import="com.google.gson.Gson" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>${livehouse.livehouse_name} - ライブハウス詳細画面</title>
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
                    <li><a href="">リンク1</a></li>
                    <li><a href="">リンク2</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="artist-livehouse-details-main">
    
    	<%
			Livehouse_information livehouse = (Livehouse_information) request.getAttribute("livehouse");
		%>
    	
	    	<div class="at-detail-liveinfo-title-frame">
				<p class="at-detail-liveinfo-title"><%= livehouse.getLivehouse_name() %></p>
			</div>
	    	<div class="at-detail-liveinfo-containar">
	    	
	    		
				
				
				<div class="at-detail-liveinfo-main">
					<ul class="at-detail-liveinfo-img-ul">
						<li class="at-detail-liveinfo-img-frame"><img src="<%= livehouse.getPicture_image_naigaikan() %>" alt="画像" class="at-detail-liveinfo-img" style="width: 200px;, height: 300px;"/></li>
					</ul>
					<ul class="at-detail-liveinfo-main-ul">
						<li class="at-detail-liveinfo-main-li1"><p class="at-detail-liveinfo-main-li1-p">オーナー名  <%= livehouse.getOwner_name() %></p></li>
						<li class="at-detail-liveinfo-main-li2"><p class="at-detail-liveinfo-main-li2-p">住所  <%= livehouse.getLive_address() %></p></li>
						<li class="at-detail-liveinfo-main-li3"><p class="at-detail-liveinfo-main-li3-p">電話番号  <%= livehouse.getLive_tel_number() %></p></li>
						<li class="at-detail-liveinfo-main-li4"><p class="at-detail-liveinfo-main-li4-p">設備情報</p></li>
						<li class="at-detail-liveinfo-main-li5"><p><%= livehouse.getEquipment_information() %></p></li>
						<li class="at-detail-liveinfo-main-li6"><p class="at-detail-liveinfo-main-li5-p">詳細情報</p></li>
						<li class="at-detail-liveinfo-main-li7"><p><%= livehouse.getLivehouse_detailed_information() %></p></li>
					</ul>
				</div>	
				<div class="at-detail-liveinfo-easyinfo-title">
					<p>説明情報</p>
				</div>
				<div class="at-detail-liveinfo-easyinfo-main">
					<p><%= livehouse.getLivehouse_explanation_information() %></p>
				</div>
			</div>
    	
    	

        <!-- カレンダーと空き状況 -->
        <section class="calendar-section">
            <div>
                <h2 class="OpenSpots-Reserve">空き状況・予約</h2>
                <p class="OpenSpots-Reserve-detile">空いてる日にちを選択して、予約に進んでください</p>
                <p class="Notes-or-Cautions">※誰も予約していない〇
            </div>

            <div class="a-t-detail-calendar-containar">
                <button id="prev-month" type="button">前の月</button>
                <span id="current-month" class="current-month"></span>
                <button id="next-month" type="button">次の月</button>
            </div>
            <div id="calendar-container"></div>
        </section>
    </main>

    <%
 	// `reservationStatus` の定義
    Map<Integer, String> reservationStatus = new HashMap<>();
    reservationStatus.put(1, "〇");
    reservationStatus.put(2, "×");
    reservationStatus.put(3, "〇");
    reservationStatus.put(4, "〇");
    reservationStatus.put(5, "×");
    // 現在の年月を取得
    LocalDate currentDate = LocalDate.now();
    int year = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : currentDate.getYear();
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currentDate.getMonthValue();
    int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

    // ReservationStatusをJSON形式に変換
    String reservationStatusJson = new Gson().toJson(reservationStatus);
%>

<script>
    const contextPath = '<%= request.getContextPath() %>';
    const livehouseType = '<c:out value="${livehouseType}" escapeXml="true" />';
    const userId = livehouseType === 'multi' ? '<c:out value="${userId}" escapeXml="true" />' : null;
    const applicationId = livehouseType === 'multi' ? '<c:out value="${applicationId}" escapeXml="true" />' : null;
    const livehouseId = '<c:out value="${livehouse.id}" escapeXml="true" />';
    const reservationDataRaw = '<c:out value="${reservationStatus}" escapeXml="false" />';
    let reservationData = {};

    console.log("[DEBUG] Raw reservationData from JSP:", reservationDataRaw);

    // reservationData を安全にパース
    try {
        if (reservationDataRaw && reservationDataRaw.trim() !== "") {
            reservationData = JSON.parse(reservationDataRaw);
        } else {
            console.warn("[WARNING] reservationData is empty or null.");
        }
    } catch (error) {
        console.error("[ERROR] Failed to parse reservationData:", error);
    }

    const now = new Date();
    const defaultYear = now.getFullYear();
    const defaultMonth = now.getMonth() + 1; // JavaScriptの月は0ベース
    const defaultDaysInMonth = new Date(defaultYear, defaultMonth, 0).getDate();

    const currentYear = ${year != null ? year : defaultYear};
    const currentMonth = ${month != null ? month : defaultMonth};
    const daysInCurrentMonth = ${daysInMonth != null ? daysInMonth : defaultDaysInMonth};

    console.log("[DEBUG] Calculated Year:", currentYear);
    console.log("[DEBUG] Calculated Month:", currentMonth);
    console.log("[DEBUG] Days in Current Month:", daysInCurrentMonth);
</script>

    <script src="<%= request.getContextPath() %>/assets/js/at_calender.js" defer></script>

    <table id="calendar-table" class="calendar-table">
        <thead>
            <tr>
                <th class="calendar-cell">日</th>
                <th class="calendar-cell">月</th>
                <th class="calendar-cell">火</th>
                <th class="calendar-cell">水</th>
                <th class="calendar-cell">木</th>
                <th class="calendar-cell">金</th>
                <th class="calendar-cell">土</th>
            </tr>
        </thead>
        <tbody id="calendar-body"></tbody>
    </table>
</body>
</html>
