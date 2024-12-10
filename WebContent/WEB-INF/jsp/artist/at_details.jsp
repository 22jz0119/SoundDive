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
        .calendar-container {
            width: 100%;
            padding: 20px;
        }

        .calendar-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .calendar-header button {
            padding: 5px 10px;
            cursor: pointer;
        }

        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 1px;
        }

        .calendar-cell {
            width: 100%;
            height: 100px;
            display: flex;
            justify-content: center;
            align-items: center;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
        }

        .calendar-cell.disabled {
            background-color: #eaeaea;
            pointer-events: none;
        }

        .calendar-cell a {
            text-decoration: none;
            font-size: 1.5em;
            color: black;
        }

        .calendar-cell .status {
            font-size: 1.2em;
            position: absolute;
            bottom: 5px;
            width: 100%;
            text-align: center;
        }

        .calendar-cell .status.reserved {
            color: red;
        }

        .calendar-cell .status.available {
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
		<section class="calendar-section">
            <div>
                <h2 class="OpenSpots-Reserve">空き状況・予約</h2>
                <p class="OpenSpots-Reserve-detile">空いてる日にちを選択して、予約に進んでください</p>
                <p class="Notes-or-Cautions">※誰も予約していない〇 ※確定していないが予約者多数△</p>
            </div>
            <div class="custom-calendar-container">
                <div class="custom-calendar-header">
                    <button id="prev-month-btn">前の月</button>
                    <h2 id="current-month-label"></h2>
                    <button id="next-month-btn">次の月</button>
                </div>
                <div class="custom-calendar-grid" id="custom-calendar"></div>
            </div>
        </section>
<<<<<<< HEAD
    </main>

    <!-- サーバーから渡された予約状況を JavaScript に埋め込む -->
    <script>
        const reservationStatus = <c:out value="${reservationStatus}" escapeXml="false" /> || {};
        const year = ${year};
        const month = ${month};
    </script>

    <!-- カレンダー用JavaScript -->
    <script src="<%= request.getContextPath() %>/assets/js/at_calendar.js"></script>

<!-- サーブレットから渡されたLivehouse_applicationデータを使用 -->
	    <script>
	        const applications = ${applications};
	
	        // 予約データを日付ベースでマッピング
	        const reservationData = {};
	        applications.forEach(application => {
	            const date = new Date(application.date_time); // datetimeを取得
	            const day = date.getDate();
	            reservationData[day] = application.true_false; // true_falseの状態を日付に紐づけ
	        });
	    </script>
        <!-- JavaScriptファイルの読み込み -->
        <script src="<%= request.getContextPath() %>/assets/js/A-t-Description.js"></script>
        
		<c:choose>
        <c:when test="${not empty applications}">
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>ライブハウス情報ID</th>
                        <th>ユーザーID</th>
                        <th>申請日時</th>
                        <th>予約状態</th>
                        <th>開始時間</th>
                        <th>終了時間</th>
                        <th>作成日時</th>
                        <th>更新日時</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="application" items="${applications}">
                        <tr>
                            <td>${application.id}</td>
                            <td>${application.livehouse_information_id}</td>
                            <td>${application.user_id}</td>
                            <td>${application.datetime}</td>
                            <td>${application.trueFalse ? '予約済み' : '空き'}</td>
                            <td>${application.start_time}</td>
                            <td>${application.finish_time}</td>
                            <td>${application.create_date}</td>
                            <td>${application.update_date}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>現在、このライブハウスには申請がありません。</p>
        </c:otherwise>
    </c:choose>
    	<
   		<c:forEach var="application" items="${applications}">
    		<p>${application.datetime} - ${application.trueFalse}</p>
		</c:forEach>

	</main>
		
>>>>>>> branch 'main' of https://github.com/22jz0119/SoundDive.git
</body>
</html>