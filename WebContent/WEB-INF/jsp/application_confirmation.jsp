<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>アーティスト申請一覧ページ</title>
</head>
<body>
    <!-- ヘッダー -->
    <header class="main-header">
        <div class="header-container">
            <h1 class="main-title">Sound Dive</h1>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="livehouse_home.html">HOME</a></li>
                    <li><a href="livehouse_mypage.html">MY PAGE</a></li>
                    <li><a href="#">その他1</a></li>
                    <li><a href="#">その他2</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <!-- 予約日付 -->
        <section class="application-list-count">
            <h2>アーティスト申請一覧画面</h2>
            <div class="reservation-date">
                <!-- クエリパラメータから日付を取得して表示 -->
                <p>日付: <c:out value="${param.date}" /></p>
                <!-- 件数を動的に表示 -->
                <p>件数: <c:out value="${fn:length(applicationList)}" />件</p>
            </div>
        </section>
        <!-- 申請リスト -->
        <div class="application-lists">
            <c:forEach var="application" items="${applicationList}">
                <div class="artist-list-container">
                    <div class="application-lists-info">
                        <div class="artist-list-img-bg">
                            <!-- グループ画像 -->
                            <img class="artist-list-img" src="<%= request.getContextPath() %>/assets/img/artist_default.png" alt="アーティスト画像">
                        </div>
                        <div class="application-lists-info-2">
                            <!-- グループ情報 -->
                            <h1>${application.accountName}</h1>
                            <ul>
                                <li>ジャンル: ${application.groupGenre}</li>
                                <li>バンド歴: ${application.bandYears}年</li>
                            </ul>
                            <ul>
                                <li>申請ID: ${application.applicationId}</li>
                                <li>申請日時: ${application.datetime}</li>
                                <li>開始時間: ${application.startTime}</li>
                                <li>終了時間: ${application.finishTime}</li>
                                <li>承認状態: ${application.trueFalse ? '承認済み' : '未承認'}</li>
                            </ul>
                            <!-- サンプル音源 -->
                            <audio controls>
                                <source src="<%= request.getContextPath() %>/assets/audio/sample.mp3" type="audio/mp3">
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
