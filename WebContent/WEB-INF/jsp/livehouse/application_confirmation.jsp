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
					<li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
					<li><a href="<%= request.getContextPath() %>/Approval_history">承認履歴</a></li>
					<li class="header-box-li2"><a href="#" onclick="logoutAndRedirect();" class="top-logout-btn">LOG OUT</a></li>
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
                            <c:set var="groupId" value="${application.groupId}" />
                                    <c:set var="imagePath" value="${pictureImageMap[groupId]}" />
                                    <img src="${pageContext.request.contextPath}${imagePath}" 
                                         alt="バンドのイラスト" class="application-artist-list-ikon"
                                         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
                        </div>
                        <ul class="confirmation-info">
                            <h2>${application.accountName}</h2>
                            <h1>ジャンル:${application.groupGenre}</h1>
                            <h1>メンバー</h1>
                            <c:forEach var="member" items="${members}">
							    <li class="confirmation-name">${member.member_position}: ${member.member_name}</li>
							</c:forEach>
                            <!-- <h2>サンプル音源</h2> -->
                            <audio class="sound-source" controls src="water.mp3" type="audio/mp3">まじかるろりぽっぷ☆（てきとう）</audio>
                        </ul>
                    </div>
                    <!-- 【追加】対バン予約（cogig_or_solo = 2）の場合のみ、対バングループ情報を表示 -->
                <c:if test="${cogigOrSolo == 2}">
                    <div class="band-confirmation-container">
                        <h2>対バングループ情報</h2>
                        <div class="confirmation-profile">
                            <div class="confirmation-artist-img-div">
                              <c:set var="groupId" value="${application.groupId}" />
                                    <c:set var="imagePath" value="${pictureImageMap[groupId]}" />
                                    <img src="${pageContext.request.contextPath}${imagePath}" 
                                         alt="バンドのイラスト" 
                                         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
                            </div>
                            <ul class="confirmation-info">
                                <h2>${artistGroup.accountName}</h2>
                                <h1>ジャンル: ${artistGroup.groupGenre}</h1>
                                <h1>メンバー</h1>
                                <c:forEach var="member" items="${artistMembers}">
                                    <li class="confirmation-name">${member.member_position}: ${member.member_name}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </c:if>
                    
                    <div class="reservation-details-container">
                        <h2>予約詳細</h2>
                        <ul class="reservation-details-container-info">
                        	<%-- 
                        	<li>申請ID: ${application.applicationId}</li>
                        	--%>
                            <li>予約者名: ${application.accountName} ${application.us_name}</li>
                            <li>開始時間: ${application.startTime}</li>
                            <li>終了時間: ${application.finishTime}</li>
                        </ul>
                    </div>
                </div>
                <div class="application_confirmation-btn">
			    <!-- 戻るボタン -->
			    <button class="approve-btn" onclick="location.href='navigate?action=list'">戻る</button>
			
			    <!-- 承認ボタン -->
			    <button class="decline-btn" onclick="location.href='Application_confirmation?action=approval&id=${application.applicationId}'">承認</button>
</div>

        </main>
    </body>
</html>
