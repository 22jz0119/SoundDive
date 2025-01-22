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
    <header class="main-header">
        <div class="header-container">
            <h1 class="main-title">Sound Dive</h1>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/Livehouse_home">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <section class="application-list-count">
            <h2>アーティスト申請一覧画面</h2>
            <div class="reservation-date">
                <p>日付: <c:out value="${param.date}" /></p>
                <p>件数: <c:out value="${fn:length(applicationList)}" />件</p>
            </div>
        </section>
        <div class="application-lists">
            <ul>
                <c:choose>
                    <c:when test="${cogigOrSolo eq 2}">
                        <c:forEach var="application" items="${applicationList}">
                            <li>
                                <strong>${application.accountName}</strong>
                                <ul>
                                    <li>ジャンル: ${application.groupGenre}</li>
                                    <li>バンド歴: ${application.bandYears}年</li>
                                    <li>レーティング: ${application.ratingStar}</li>
                                    <li>
                                        音声:
                                        <audio class="sound-source" controls src="${application.pictureImageMovie != null ? application.pictureImageMovie : '/default.mp3'}" type="audio/mp3">
                                            音声が再生できません
                                        </audio>
                                    </li>
                                    <li>
                                        <a href="<c:url value='/Application_confirmation' />?id=${application.id}">詳細を見る</a>
                                    </li>
                                </ul>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="application" items="${applicationList}">
                            <div class="application-artist-list-img-containar">
                                <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                            </div>
                            <li>
                                <ul class="application-artist-list-ul0">
                                    <li><p>${application.accountName}</p></li>
                                </ul>

                                <ul class="application-artist-list-ul1">
                                    <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                                    <li class="application-artist-list-ul1-li2"><p>${application.groupGenre}</p></li>
                                </ul>
                                <ul class="application-artist-list-ul2">
                                    <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                                    <li class="application-artist-list-ul2-li2"><p>${application.bandYears}年</p></li>
                                </ul>
                                <ul class="application-artist-list-ul3">
                                    <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                                    <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                                </ul>
                                <ul class="application-artist-list-ul4">
                                    <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                                </ul>
                                <ul class="application-artist-list-ul5">
                                    <li class="application-artist-list-ul5-li1">
                                        <a href="<c:url value='/Application_confirmation' />?id=${application.id}" class="application-artist-list-ul5-li1-a">詳細を見る</a>
                                    </li>
                                </ul>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>

            <c:if test="${empty applicationList}">
                <p>申請データはありません。</p>
            </c:if>
        </div>
    </main>
</body>
</html>
