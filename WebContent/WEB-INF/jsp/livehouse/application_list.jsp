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
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/Livehouse_home">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
                    <li><a href="<%= request.getContextPath() %>/Approval_history">承認履歴</a></li>
                    <li class="header-box-li2"><a href="#" onclick="logoutAndRedirect();" class="top-logout-btn">LOG OUT</a></li>
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
                    <!-- JSP側 -->
                <c:out value="${totalReservations}" default="0" />件
                </p>
            </div>
        </section>

        <div class="application-lists">
            <div class="application-list-solo">
                <c:choose>
                    <c:when test="${not empty soloApplications}">
                        <c:forEach var="application" items="${soloApplications}">
                            <div class="apllicationList-MaiinFrame">
                                <div class="application-artist-list-img-containar">
                                    <c:set var="groupId" value="${application.groupId}" />
                                    <c:set var="imagePath" value="${pictureImageMap[groupId]}" />
                                    <img src="${pageContext.request.contextPath}${imagePath}" 
                                         alt="バンドのイラスト" 
                                         class="application-artist-list-ikon"
                                         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
                                </div>
                                <div>
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
                                    <ul class="application-artist-list-ul5">
                                        <li class="application-artist-list-ul5-li1">
                                            <a href="<c:url value='/Application_confirmation' />?id=${application.id}" class="application-artist-list-ul5-li1-a">詳細を見る</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>  
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li>ソロデータがありません</li>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 対バンデータ -->
            <div class="application-list-multi">
                <c:choose>
                    <c:when test="${not empty cogigApplications}">
                        <c:set var="previousReservationId" value="-1" />
                        <c:forEach var="application" items="${cogigApplications}">
                            <div class="apllicationList-SubFrame">
                                <div class="application-artist-list-img-containar">
                                    <c:set var="groupId" value="${application.groupId}" />
                                    <c:set var="imagePath" value="${pictureImageMap[groupId]}" />
                                    <img src="${pageContext.request.contextPath}${imagePath}" 
                                         alt="バンドのイラスト" 
                                         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
                                </div>
                                <div>
                                    <ul class="application-artist-list-ul0">
                                        <li class="application-artist-list-ul0-li1"><p>${application.accountName}</p></li>
                                    </ul>
                                    <ul class="application-artist-list-ul1">
                                        <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                                        <li class="application-artist-list-ul1-li2"><p>${application.groupGenre}</p></li>
                                    </ul>
                                    <ul class="application-artist-list-ul2">
                                        <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                                        <li class="application-artist-list-ul2-li2"><p>${application.bandYears}年</p></li>
                                    </ul>
                                    <ul class="application-artist-list-ul5">
                                        <li class="application-artist-list-ul5-li1">
                                            <a href="<c:url value='/Application_confirmation' />?id=${application.id}" class="application-artist-list-ul5-li1-a">詳細を見る</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li>対バンデータがありません</li>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main