<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">

    <title>アーティストホーム画面</title>
</head>

<body class="artist_home">
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="" class="main-logo">
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/At_Mypage">MY PAGE</a></li>
                    <li><a href="#">000</a></li>
                    <li><a href="#">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <div><img src="<%= request.getContextPath() %>/assets/img/key-visual.jpg" alt=""></div>
        <div class="booking-title">
            <h2 class="booking-title-h2">Booking</h2>
        </div>
        <section class="booking-nav-section">
            <form action="<%= request.getContextPath() %>/At_Home" method="post">
			    <div class="booking-button">
			        <div class="booking-solo-button">
			            <!-- SOLO LIVE ボタン -->
			            <button type="submit" name="action" value="solo" class="solo-button">SOLO LIVE</button>
			        </div>
			        <div class="booking-multi-button">
			            <!-- MULTI LIVE ボタン -->
			            <button type="submit" name="action" value="multi" class="multi-button">MULTI LIVE</button>
			        </div>
			    </div>
			</form>

        </section>

        <div class="main-calendar-button">
            <div class="calendar-next-button">
                <button id="prev" type="button">前の月</button>
            </div>
            <div class="calendar-back-button">
                <button id="next" type="button">次の月</button>
            </div>
        </div>
        
        <div class="home-calendar-div">
            <div id="calendar"></div>
        </div>
        
        <script src="<%= request.getContextPath() %>/assets/js/artist_home.js"></script>
        <script>
            // ボタンがクリックされたときにlivehouse_typeを設定
            function setLivehouseType(type) {
                document.getElementById("livehouse_type").value = type;
            }
        </script>
    </main>
</body>
</html>
