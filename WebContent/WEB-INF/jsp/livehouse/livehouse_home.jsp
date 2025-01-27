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
					<li><a href="<%= request.getContextPath() %>/Approval_history">承認履歴</a></li>
					<li><a href="${pageContext.request.contextPath}/Approval_history?year=${year}&month=${month}&day=${day}">承認履歴?</a></li>
					
					<li><a href="#">リンク2</a></li>
				</ul>
			</nav>
		</div>
	</header>
	
	<main class="artist-livehouse-details-main">
        <section class="artist-livehouse-detail-section">
            <!-- ライブハウス情報を表示 -->
        </section>
        <section class="application-list-count">
		<div class="main-application_list">
			<h2 class="application_list_h2">アーティスト申請カレンダートップ</h2>
		</div>
		<section class="calendar-section">
            <div>
            <!-- 
                <h2 class="OpenSpots-Reserve">アーティストからのライブハウス予約申請カレンダー</h2>
             -->
                <p class="OpenSpots-Reserve-detile">緑の日にちを選択して、申請情報の可否へ進んでください</p>
                <p class="Notes-or-Cautions">※申請データなし 青</p>
            </div>
            <div id="calendar-container"></div>
        </section>
	</section>

	<div class="live-main-calendar-button">
		<button id="prev-month" type="button">前の月</button>
                <span id="current-month" class="current-month"></span>
                <button id="next-month" type="button">次の月</button>
	</div>

	<div class="live-home-calendar-div">
		<div id="calendar"></div>
	</div>    

    </main>
     <script>
    const contextPath = '<%= request.getContextPath() %>';
    

    // ✅ livehouseId と livehouseType を埋め込む
    const livehouseInformationId = '<%= request.getAttribute("livehouseInformationId") %>';
    const livehouseType = '<%= request.getAttribute("livehouseType") != null ? request.getAttribute("livehouseType") : "" %>';

    const reservationDataRaw = '<%= request.getAttribute("reservationStatus") != null ? request.getAttribute("reservationStatus") : "{}" %>';
    const currentYear = <%= request.getAttribute("year") != null ? request.getAttribute("year") : "2025" %>;
    const currentMonth = <%= request.getAttribute("month") != null ? request.getAttribute("month") : "2" %>;

    console.log("[DEBUG] contextPath:", contextPath);
    console.log("[DEBUG] livehouseInformationId:", livehouseInformationId);
    console.log("[DEBUG] livehouseType:", livehouseType);
    console.log("[DEBUG] reservationDataRaw:", reservationDataRaw);
    console.log("[DEBUG] currentYear:", currentYear);
    console.log("[DEBUG] currentMonth:", currentMonth);
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
</body>
</html>
