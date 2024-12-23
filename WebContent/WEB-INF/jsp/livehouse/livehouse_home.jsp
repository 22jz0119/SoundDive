<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
	<title>ライブハウスホームページ</title>
</head>
<body>
	<header class="main-header">
		<div class="header-container">
			<div class="main-title">
				<h1 class="main-title-h1">Sound Dive</h1>
			</div>
			<nav class="header-nav">
				<ul class="header-nav-ul">
					<li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
					<li><a href="#">リンク1</a></li>
					<li><a href="#">リンク2</a></li>
				</ul>
			</nav>
		</div>
	</header>
	
	<main class="artist-livehouse-details-main">
        <section class="artist-livehouse-detail-section">
            <!-- ライブハウス情報を表示 -->
        </section>

        <!-- カレンダーと空き状況 -->
        <section class="calendar-section">
            <div>
                <h2 class="OpenSpots-Reserve">アーティストからのライブハウス予約申請カレンダー</h2>
                <p class="OpenSpots-Reserve-detile">件数表示されている日にちを選択して、申請情報の承認承諾へ進んでください</p>
                <p class="Notes-or-Cautions">※申請データなし×</p>
                    
            </div>

            <div class="a-t-detail-calendar-containar">
                <button id="prev-month" type="button">前の月</button>
                <button id="next-month" type="button">次の月</button>
            </div>
            <div id="calendar-container"></div>
        </section>
    </main>

	<section class="application-list-count">
		<div class="main-application_list">
			<h2 class="application_list_h2">アーティスト申請カレンダートップ</h2>
		</div>
		<div class="reservation-date">
			<p class="application-date">日付 ${year}/${month}/${day}</p>
			<p class="application-number">件数 ${reservationCount}件</p>
		</div>
	</section>

	<div class="live-main-calendar-button">
		<div class="live-calendar-next-button">
			<button id="prev" type="button">前の月</button>
		</div>
		<div class="live-calendar-back-button">
			<button id="next" type="button">次の月</button>
		</div>
	</div>

	<div class="live-home-calendar-div">
		<div id="calendar"></div>
	</div>
	<script>
    const contextPath = '<%= request.getContextPath() %>';
    const livehouseType = '<c:out value="${livehouseType}" escapeXml="true" />';
    const userId = livehouseType === 'multi' ? '<c:out value="${userId}" escapeXml="true" />' : null;
    const applicationId = livehouseType === 'multi' ? '<c:out value="${applicationId}" escapeXml="true" />' : null;
    const livehouseId = '<c:out value="${livehouse.id}" escapeXml="true" />';
    const reservationDataRaw = '<c:out value="${reservationStatus}" escapeXml="false" />';
    let reservationData = {};

    console.log("[DEBUG] Raw reservationData from JSP:", reservationDataRaw);

    try {
        if (reservationDataRaw && reservationDataRaw.trim() !== "") {
            reservationData = JSON.parse(reservationDataRaw);
        } else {
            console.warn("[WARNING] reservationData is empty or null.");
        }
    } catch (error) {
        console.error("[ERROR] Failed to parse reservationData:", error);
    }

    const currentYear = ${year != null ? year : 2024};
    const currentMonth = ${month != null ? month : 12};
    const daysInCurrentMonth = ${daysInMonth != null ? daysInMonth : 31};

    console.log("[DEBUG] JSP contextPath:", contextPath);
    console.log("[DEBUG] JSP livehouseType:", livehouseType);
    console.log("[DEBUG] JSP userId:", userId);
    console.log("[DEBUG] JSP applicationId:", applicationId);
    console.log("[DEBUG] JSP livehouseId:", livehouseId);
    console.log("[DEBUG] Parsed reservationData:", reservationData);
    console.log("[DEBUG] currentYear:", currentYear);
    console.log("[DEBUG] currentMonth:", currentMonth);
    console.log("[DEBUG] daysInCurrentMonth:", daysInCurrentMonth);
</script>

<script src="<%= request.getContextPath() %>/assets/js/livehouse_home.js" defer></script>

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

	

	<script src="${pageContext.request.contextPath}/assets/js/livehouse_home.js"></script>
</body>
</html>
