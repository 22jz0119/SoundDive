<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>ライブハウス検索画面</title>
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
    <main class="artist-result-main">
        <section class="livehouse-search-container">
            <div class="livehouse-search-title">
                <h2 class="livehouse-search-title-h2">ライブハウス検索画面</h2>
            </div>
            <div class="livehouse-search-form">
                <form action="At-livehouse_search" method="post">
                    <div class="livehouse-search-box"><input type="search" id="livehouse-search-textbox" name="q">
                    </div>
                    <div class="livehouse-search-button"><button type="submit" class="livehouse-search-btn">検索</button></div>
                </form>               
            </div>
        </section>
        <!--
            boxの位置指定はposition: absolute;を使います
        -->
        <section class="artist-result-container">
        	<c:forEach var="livehouse" items="${livehouseList}">
			    <div class="artist-result-box">
			        <div class="artist-result-LiveHouseName1">
			            <h3 class="artist-result-LiveHouseName2">${livehouse.livehouse_name}</h3>
			        </div>
			        <div class="artist-result-Div3">
			            <div>
			                <img src="../assets/img/Studio.jpg" alt="StudioImg" class="artist-result-img">
			            </div>
			            <ul class="artist-result-frame">
			                <li class="LiveHousemain-img"></li>
			                <li class="LiveHouseAddress"><p>住所</p></li>
			                <li><p>${livehouse.live_address}</p></li>
			                <li><p>キャパ</p></li>
			                <li><p>100人</p></li>
			                <li><p>${livehouse.livehouse_explanation_information}</p></li>
			                <li class="artist-search-decision">
			                    <!-- userId をリンクに追加 -->
			                    <a href="<%= request.getContextPath() %>/At_details?id=${livehouse.id}&userId=${userId}" 
   									class="artist-result-decision-button">詳細・予約</a>
			                </li>
			            </ul>
			        </div>
			    </div>
			</c:forEach>
        </section>
    </main>
    <footer></footer>
</body>
</html>