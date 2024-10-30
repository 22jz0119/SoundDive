<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>予約完了</title>
</head>
<body>
<header class="main-header">
    <div class="header-container">
        <div class="main-title">
            <h1 class="main-title-h1">Sound Dive</h1>
        </div>
        <nav class="header-nav">
            <ul class="header-nav-ul">
                <li><a href="livehouse_home.html">HOME</a></li>
                <li><a href="livehouse_mypage.html">MY PAGE</a></li>
                <li><a href="">000</a></li>
                <li><a href="">000</a></li>
            </ul>
        </nav>
    </div>
</header>
<main>
    <section class="main-application_approval">
        <div class="application_approvaled">
            <h2 class="application_approvaled-h2">アーティストを承認しました！</h2>
        </div>
        <p class="approvaled-application">以下の予約が完了されました。</p>
    </section>
    <div class="livehouse-details-container">
        <div class="approved-artist-div">
            <img class="approved-artist-img" src="../assets/img/アーティスト画像.png" alt="アーティスト画像">
        </div>
        <div class="application_approval-info">
            <h1 class="booking-detail">予約詳細</h1>
            <ul class="booking-detail-info">
                <li class="approved-name">予約者名: ${reservationName}</li>
                <li class="approved-date">予約日時: ${reservationDateTime}</li>
                <li class="approved-pay">前払い金額: 8000円</li>
            </ul>
        </div>
    </div>
    <a class="back-home" href="livehouse_home.html">ホームへ戻る</a>
</main>
</body>
</html>
