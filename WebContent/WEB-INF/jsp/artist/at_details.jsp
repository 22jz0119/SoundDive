<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>${livehouse.livehouse_name} - ライブハウス詳細画面</title>
    <style>
        .calendar-table {
            width: 100%;
            border-collapse: collapse;
        }
        .calendar-cell {
            width: 14%;
            height: 80px;
            text-align: center;
            vertical-align: middle;
            border: 1px solid #ddd;
        }
        .calendar-day {
            position: relative;
        }
        .status {
            position: absolute;
            bottom: 5px;
            width: 100%;
            text-align: center;
            font-size: 18px;
        }
        .status.x {
            color: red;
        }
        .status.o {
            color: green;
        }
    </style>
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
                    <li><a href="">リンク1</a></li>
                    <li><a href="">リンク2</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="artist-livehouse-details-main">
        <!-- ライブハウス情報 -->
        <section class="artist-livehouse-detail-section">
            <div class="A-t-detail-livehousename">
                <p class="artist-livehouse-detail-oner">${livehouse.livehouse_name}</p>
            </div>
            <div class="a-t-livehouse-detail-containar">
                <div class="a-t-livehouse-detail-img-div">
                    <img src="<%= request.getContextPath() %>/assets/img/key-visual.jpg" alt="" class="artist_livehouse_details-img1">
                </div>
                <div class="a-t-detail-description-div1">
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-onername-title a-t-detail-1"><p>オーナー</p></li>
                        <li class="a-t-detail-onername a-t-detail-2"><p>${livehouse.owner_name}</p></li>
                    </ul>
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-address-title a-t-detail-1"><p>住所</p></li>
                        <li class="a-t-detail-address a-t-detail-2"><p>${livehouse.live_address}</p></li>
                    </ul>
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-tell-title a-t-detail-1"><p>電話番号</p></li>
                        <li class="a-t-detail-tell a-t-detail-2"><p>${livehouse.live_tel_number}</p></li>
                    </ul>
                </div>
                <div class="a-t-detail-description-div2">
                    <ul class="A-t-discription-ul3">
                        <li class="a-t-detail-explanation-title a-t-detail-1"><p>ライブハウス説明情報</p></li>
                        <li class="a-t-detail-explanation a-t-detail-2"><p>${livehouse.livehouse_explanation_information}</p></li>
                    </ul> 
                    <ul class="A-t-discription-ul3">   
                        <li class="a-t-detail-description-title a-t-detail-1"><p>ライブハウス詳細情報</p></li>
                        <li class="a-t-detail-description a-t-detail-2"><p>${livehouse.livehouse_detailed_information}</p></li>
                    </ul>
                </div>
            </div>
        </section>

        <!-- カレンダーと空き状況 -->
        <section class="calendar-section">
            <div>
            <h2 class="OpenSpots-Reserve">空き状況・予約</h2>
            <p class="OpenSpots-Reserve-detile">空いてる日にちを選択して、予約に進んでください</p>
            <p class="Notes-or-Cautions">※誰も予約していない〇
                ※確定していないが予約者多数△</p>
        </div>

        <div class="a-t-detail-calendar-containar">
            <button id="prev-month" type="button">前の月</button>
            <button id="next-month" type="button">次の月</button>
        </div>
        <div id="calendar-container"></div>
    </main>

    <script src="/assets/js/A-t-Description.js"></script> <!-- JavaScriptファイルをリンク -->
        </section>
    </main>

    <!-- サーバーから渡された予約状況を JavaScript に埋め込む -->
    <script>
        const reservationStatus = <c:out value="${reservationStatus}" escapeXml="false" /> || {};
        const year = ${year};
        const month = ${month};
    </script>

    <!-- カレンダー用JavaScript -->
    <script src="<%= request.getContextPath() %>/assets/js/calendar.js"></script>

	<table class="calendar-table">
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
	    <tbody>
	        <!-- 1ヶ月分のカレンダーを6行(最大)で表示 -->
	        <c:forEach var="week" begin="0" end="5">
	            <tr>
	                <c:forEach var="day" begin="1" end="7">
	                    <c:set var="date" value="${week * 7 + day}" />
	                    <td class="calendar-cell">
	                        <c:if test="${reservationStatus[date] != null}">
	                            <div class="calendar-day">
	                                ${date}
	                                <div class="status ${reservationStatus[date] ? 'x' : 'o'}">
	                                    ${reservationStatus[date] ? '×' : '○'}
	                                </div>
	                            </div>
	                        </c:if>
	                    </td>
	                </c:forEach>
	            </tr>
	        </c:forEach>
	    </tbody>
	</table>
	
	<c:forEach var="application" items="${applications}">
            <tr>
                <td>${application.id}</td>
                <td>${application.livehouse_information_id}</td>
                <td>${application.user_id}</td>
                <td>${application.date_time}</td>
                <td>${application.true_false ? '予約済み' : '空き'}</td>
                <td>${application.start_time}</td>
                <td>${application.finish_time}</td>
                <td>${application.create_date}</td>
                <td>${application.update_date}</td>
            </tr>
        </c:forEach>
		
	<table>
	    <tr>
	        <th>ID</th>
	        <td>${application.id}</td>
	    </tr>
	    <tr>
	        <th>ライブハウス情報ID</th>
	        <td>${application.livehouse_information_id}</td>
	    </tr>
	    <tr>
	        <th>ユーザーID</th>
	        <td>${application.user_id}</td>
	    </tr>
	    <tr>
	        <th>申請日時</th>
	        <td>${application.date_time}</td>
	    </tr>
	    <tr>
	        <th>予約状態</th>
	        <td>${application.true_false ? '予約済み' : '空き'}</td>
	    </tr>
	    <tr>
	        <th>開始時間</th>
	        <td>${application.start_time}</td>
	    </tr>
	    <tr>
	        <th>終了時間</th>
	        <td>${application.finish_time}</td>
	    </tr>
	    <tr>
	        <th>作成日時</th>
	        <td>${application.create_date}</td>
	    </tr>
	    <tr>
	        <th>更新日時</th>
	        <td>${application.update_date}</td>
	    </tr>
	</table>
		
</body>
</html>
