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

	<script src="${pageContext.request.contextPath}/assets/js/livehouse_home.js"></script>
</body>
</html>
