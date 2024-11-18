<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
	<title>アーティスト申請一覧ページ</title>
</head>
<body>
	<!--ヘッダー-->
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
        <!--予約日付-->
        <section class="application-list-count">
            <div class="main-application_list">
                <h2 class="application_list_h2">アーティスト申請一覧画面</h2>
            </div>
            <div class="reservation-date">
                <p class="application-date">日付 2024/12/24</p>
                <p class="application-number">件数〇件</p>
            </div>
        </section>
        <!--申請リスト-->
        <div class="application-lists">
        <!-- applicationList をループして各グループ情報を表示 -->
        <c:forEach var="application" items="${applicationList}">
            <div class="artist-list-container">
                <div class="application-lists-info">
                    <div class="artist-list-img-bg">
                        <!-- グループ画像: 必要に応じて画像パスを動的にする -->
                        <img class="artist-list-img" src="../assets/img/アーティスト画像.png" alt="アーティスト画像">
                    </div>
                    <div class="application-lists-info-2">
                        <!-- グループ情報を動的に表示 -->
                        <h1 class="application-lists-name">${application.accountName}</h1>
                        <ul class="application-lists-detail">
                            <li>ジャンル: ${application.groupGenre}</li>
                            <li>バンド歴: ${application.bandYears}年</li>
                        </ul>
                        <!-- 
                         --><ul class="application-lists-detail">
                            <!-- 申請情報を表示 
                            <li>申請ID: ${application.applicationId}</li>
                            <li>申請日時: ${application.datetime}</li>
                            <li>開始時間: ${application.startTime}</li>
                            <li>終了時間: ${application.finishTime}</li>
                            <li>承認状態: ${application.trueFalse ? '承認済み' : '未承認'}</li>
                        </ul>
                        
                        <!-- サンプル音源の表示 -->
                        <audio class="sound-source" controls>
                            <source src="water.mp3" type="audio/mp3">
                            このブラウザはオーディオ再生をサポートしていません。
                        </audio>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    </main>
</body>
</html>
