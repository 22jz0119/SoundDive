<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウス予約完了画面</title>
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

    <section class="artist-co-gig-container">
        <div class="artist-co-gig-title">
            <h2 class="artist-co-gig-title-h2">申請が完了しました</h2>
        </div>
    </section>

    <h2 class="complete_sentence">ライブハウスからの承認をお待ちください</h2>
    


    <div class="artist-livehouse-detail-section3">
        <div class="A-t-detail-livehousename">
            <p class="artist-livehouse-detail-oner">${livehouse.livehouse_name}</p>
        </div>
        <div class="artist_livehouse_details-Discription">
            <div><img src="<%= request.getContextPath() %>/assets/img/Studio.jpg" alt="" class="artist_livehouse_details-img"></div>
            <div>
                <ul class="A-t-discription-ul">
                    <li><p>オーナー</p></li>
                    <li><p>${livehouse.owner_name}</p></li>
                    <li><p>住所</p></li>
                    <li><p>${livehouse.live_address}</p></li>
                    <li><p>電話番号</p></li>
                    <li><p>${livehouse.live_tel_number}</p></li>
                    <li><p>ライブハウス説明情報</p></li>
                    <li><p>${livehouse.livehouse_explanation_information}</p></li>
                    <li><p>ライブハウス詳細情報</p></li>
                    <li><p>${livehouse.livehouse_detailed_information}</p></li>
                </ul>
            </div>
        </div>
    </div>
        <a href="<%= request.getContextPath() %>/At_Home" class="Return-to-home">ホームに戻る</a>
</body>
</html>