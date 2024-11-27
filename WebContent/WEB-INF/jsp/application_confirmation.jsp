<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
        <title>アーティスト申請確認ページ</title>
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
            <div class="main-reservation-confirmation">
                <section class="confirmation-sec">
                    <div class="confirmation-div">
                        <h1 class="confirmation-title">アーティスト申請詳細画面</h1>
                    </div>
                    <p class="question-approval">以下の予約を承認しますか？</p>
                </section>
                
                <div class="band-confirmation-container">
                 
                    <div class="confirmation-profile">
                        <div class="confirmation-artist-img-div">
                            <img class="confirmation-artist-img" src="../assets/img/アーティスト画像.png" alt="アーティスト画像">
                        </div>
                        <ul class="confirmation-info">
                            <h2>${application.accountName}</h2>
                            <h3>ジャンル:${application.groupGenre}</h3>
                            
                            <!-- メンバーリストをループで表示 -->
	                        <ul>
							    <c:forEach var="member" items="${applicationDetails.members}">
							        <li>${member.member_position}: ${member.member_name}</li>
							    </c:forEach>
							</ul>

                            <audio class="sound-source" controls src="water.mp3" type="audio/mp3">まじかるろりぽっぷ☆（てきとう）</audio>
                        </ul>
                    </div>
                    <div class="reservation-details-container">
                        <h2>予約詳細</h2>
                        <ul class="reservation-details-container-info">
                            <li>予約者名:${application.us_name }</li>
                            <li>予約日時:${application.datetime }</li>
                            <li>前払い金額: 8000円</li>
                        </ul>
                    </div>
                </div>
                <div class="application_confirmation-btn">
                    <button class="approve-btn" onclick="location.href='application_confirmation.jsp'">戻る</button>
                    <button class="decline-btn" onclick="location.href='application_approval.jsp'">承認</button>
                </div>
            </div>
        </main>
    </body>
</html>