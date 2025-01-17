<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
<title>承認履歴</title>
</head>
<body>

<!-- ヘッダー -->
<header class="main-header">
    <div class="header-container">
        <h1 class="main-title">Sound Dive</h1>
        <nav class="header-nav">
            <ul class="header-nav-ul">
                <li><a href="${pageContext.request.contextPath}/Livehouse_home">HOME</a></li>
                <li><a href="${pageContext.request.contextPath}/Livehouse_mypage">MY PAGE</a></li>
                <li><a href="#">その他1</a></li>
                <li><a href="#">その他2</a></li>
            </ul>
        </nav>
    </div>
</header>

<main>
    <!-- 予約日付 -->
    <section class="application-list-count">
        <h2>承認履歴画面</h2>
        <div class="reservation-date">
            <p>日付: <c:out value="${param.date}" /></p>
            <p>件数: <c:out value="${fn:length(approvedReservations)}" />件</p>
        </div>
    </section>

    <div class="application-lists">
        <c:choose>
            <c:when test="${not empty approvedReservations}">
                <c:forEach var="application" items="${approvedReservations}">
                    <div class="application-artist-list-main">
                        <div class="application-artist-list-img-containar">
                            <img src="${pageContext.request.contextPath}/assets/img/アーティスト画像.png" alt="アーティスト画像" class="application-artist-list-ikon">
                        </div>
                        <div class="application-artist-list-frame">
                            <ul class="application-artist-list-ul0">
                                <li><p>
                                
                                    <c:out value="${empty application.accountName ? 'アカウント名未設定' : application.accountName}" />
                                </p></li>
                            </ul>
                            <ul class="application-artist-list-ul1">
                                <li><p>ジャンル: 
                                    <c:out value="${empty application.groupGenre ? 'ジャンル未設定' : application.groupGenre}" />
                                </p></li>
                            </ul>
                            <ul class="application-artist-list-ul2">
                                <li><p>バンド歴: 
                                    <c:out value="${empty application.bandYears ? '不明' : application.bandYears}年" />
                                </p></li>
                            </ul>
                            <ul class="application-artist-list-ul3">
                                <li><p>レーティング: 3.5</p></li>
                            </ul>
                            <ul class="application-artist-list-ul4">
                                <li><audio class="sound-source" controls 
                                    src="${pageContext.request.contextPath}/assets/audio/water.mp3" type="audio/mp3">音声が再生できません</audio></li>
                            </ul>
                            <ul class="application-artist-list-ul5">
                                <li>
                                    <a href="<c:url value='/Application_approval' />?id=${application.id}" class="application-artist-list-ul5-li1-a">
                                        詳細を見る
                                    </a>
                                </li>                              
                            </ul>
                            <!-- 削除ボタン追加 -->
							<ul class="application-artist-list-ul5">
							    <li>
							        <form action="${pageContext.request.contextPath}/Approval_history" method="post" onsubmit="return confirm('本当に削除しますか？');">
							            <input type="hidden" name="applicationId" value="${application.id}">
							            <button type="submit" class="delete-button">削除する</button>
							        </form>
							    </li>
							</ul>
                            
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>申請データはありません。</p>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty approvedReservations}">
            <c:forEach var="application" items="${approvedReservations}">
                <p>DEBUG: ID=${application.id}, アカウント名=${application.accountName}, ジャンル=${application.groupGenre}</p>
            </c:forEach>
        </c:if>
    </div>
</main>

</body>
</html>
