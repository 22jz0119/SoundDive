<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウス予約時間確定画面</title>
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
    
    <section class="artist-livehouse-detail-section">
        <!-- タイトル -->
        <div class="artist-livehouse-detail-title">
            <h1 class="artist-livehouse-rv-title-h1">予約時間確定画面</h1>
        </div>
    
        <!-- 日付 -->
        <p class="timeselection">${selectedYear}年${selectedMonth}月${selectedDay}日</p>
    
        <!-- 時間選択フォーム -->
        <form action="<%= request.getContextPath() %>/At_booking_confirmation" method="post" class="timeschedule">
		    <label for="time">開始時間を選択してください:</label>
		    <select id="time" name="time" class="timeschedule" required>
		        <option value="08:00">08:00</option>
		        <option value="09:00">09:00</option>
		        <option value="10:00">10:00</option>
		        <option value="11:00">11:00</option>
		        <option value="12:00">12:00</option>
		        <option value="13:00">13:00</option>
		        <option value="14:00">14:00</option>
		        <option value="15:00">15:00</option>
		        <option value="16:00">16:00</option>
		        <option value="17:00">17:00</option>
		        <option value="18:00">18:00</option>
		    </select>
		
		    <input type="hidden" name="year" value="${year}">
			<input type="hidden" name="month" value="${month}">
			<input type="hidden" name="day" value="${day}">
			<input type="hidden" name="livehouseId" value="${livehouseId}">
			<input type="hidden" name="userId" value="${userId}">
			<input type="hidden" name="applicationId" value="${applicationId}">
		    <input type="submit" value="確定" class="rv-btn">
		</form>

    
        <!-- ライブハウス情報 -->
        <div class="A-t-detail-livehousename">
            <p class="artist-livehouse-detail-oner">${livehouseName}</p>
        </div>
    
        <!-- ライブハウス詳細と説明 -->
        <div class="artist_livehouse_details-Discription">
            <div>
                <img src="../assets/img/Studio.jpg" alt="" class="artist_livehouse_details-img">
            </div>
            <div>
                <ul class="A-t-discription-ul">
                    <li><p>オーナー: ${ownerName}</p></li>
                    <li><p>住所: ${address}</p></li>
                    <li><p>ライブハウス説明情報: ${description}</p></li>
                    <li><p>ライブハウス詳細情報: ${details}</p></li>
                </ul>
            </div>
        </div>
    </section>
    
    <h2 class="Currentlyapplying">申請中アーティスト</h2>
    <div class="Applicationgroup">
        <c:forEach var="artist" items="${applyingArtists}">
            <div class="Applicationtable">
                <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
                <div class="artist-info">
                    <p class="artist-name">${artist.name}</p>
                    <p class="application-status">申請中</p>
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>
