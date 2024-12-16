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
        </section>
    </main>
    
    <!-- JSPからJavaScriptにデータを渡す -->
    <script>
        const contextPath = '<%= request.getContextPath() %>';
        const userId = '<c:out value="${param.userId}" escapeXml="true" />';
        const livehouseId = '<c:out value="${livehouse.id}" escapeXml="true" />';  // livehouseId を渡す
        const reservationData = JSON.parse('<c:out value="${reservationStatus}" escapeXml="false" />');
        const currentYear = ${year != null ? year : 2024};
        const currentMonth = ${month != null ? month : 12};
        const daysInCurrentMonth = ${daysInMonth != null ? daysInMonth : 31};
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
