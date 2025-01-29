<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                <p>総件数: 
                    <c:out value="${fn:length(cogigApplications) + fn:length(soloApplications)}" default="0" />件
                </p>
            </div>
        </section>

        <div class="application-lists">
            <ul>
                <c:choose>
                    <c:when test="${not empty soloApplications}">
                        <c:forEach var="application" items="${soloApplications}">
                            <li>
                                <div class="application-artist-list-img-containar">
                                    <img src="../assets/img/アーティスト画像.png" alt="アーティスト画像" class="application-artist-list-ikon">
                                </div>
                                <ul class="application-artist-list-ul0">
                                    <li><p>${application.accountName} (ソロ)</p></li>
                                </ul>
                                <ul class="application-artist-list-ul1">
                                    <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                                    <li class="application-artist-list-ul1-li2"><p>${application.groupGenre}</p></li>
                                </ul>
                                <ul class="application-artist-list-ul2">
                                    <li class="application-artist-list-ul2-li1"><p>活動歴</p></li>
                                    <li class="application-artist-list-ul2-li2"><p>${application.bandYears}年</p></li>
                                </ul>
                                <ul class="application-artist-list-ul3">
                                    <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                                    <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                                </ul>
                                <ul class="application-artist-list-ul4">
                                    <li class="application-artist-list-ul4-li1">
                                        <audio class="sound-source" controls src="water.mp3" type="audio/mp3">音源</audio>
                                    </li>
                                </ul>
                                <ul class="application-artist-list-ul5">
                                    <li class="application-artist-list-ul5-li1">
                                        <a href="<c:url value='/Application_confirmation' />?id=${application.id}" class="application-artist-list-ul5-li1-a">詳細を見る</a>
                                    </li>
                                </ul>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li>データがありません</li>
                    </c:otherwise>
                </c:choose>

                <!-- 対バン のデータ表示 -->
                <li>対バン</li>
                <c:choose>
                    <c:when test="${not empty cogigApplications}">
                        <c:set var="previousReservationId" value="-1" />
                        <c:forEach var="application" items="${cogigApplications}">
                            <c:if test="${previousReservationId ne application.id}">
                                <!-- 対バンデータの開始 -->
                                <ul class="cogig-group">
                            </c:if>

                            <li>
                                <div class="application-artist-list-img-containar">
                                    <img src="../assets/img/アーティスト画像.png" alt="アーティスト画像" class="application-artist-list-ikon">
                                </div>
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
                                    <li class="application-artist-list-ul4-li1">
                                        <audio class="sound-source" controls src="water.mp3" type="audio/mp3">音源</audio>
                                    </li>
                                </ul>
                            </li>

                            <!-- 最後のグループの場合、または次の予約IDが異なる場合に詳細ボタンを表示 -->
                            <c:if test="${previousReservationId ne application.id or fn:length(cogigApplications) eq 1}">
                                <ul class="application-artist-list-ul5">
                                    <li class="application-artist-list-ul5-li1">
                                        <a href="<c:url value='/Application_confirmation' />?id=${application.id}" class="application-artist-list-ul5-li1-a">詳細を見る</a>
                                    </li>
                                </ul>
                                </ul> <!-- cogig-groupの閉じタグ -->
                                <c:set var="previousReservationId" value="${application.id}" />
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li>データがありません</li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </main>
</body>
</html>
