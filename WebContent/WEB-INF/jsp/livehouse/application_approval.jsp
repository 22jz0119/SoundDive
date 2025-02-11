<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>アーティスト申請承認ページ</title>
</head>
<body>
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul>
                    <li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
                    <li><a href="<%= request.getContextPath() %>/Approval_history">承認履歴</a></li>
                    <li class="header-box-li2"><a href="#" onclick="logoutAndRedirect();" class="top-logout-btn">LOG OUT</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main class="application-approval-main">
        <section class="main-application_approval">
            <div class="application_approvaled">
                <h2 class="application_approvaled-h2">アーティストを承認しました</h2>
            </div>
            <p class="approvaled-application-title">予約が正常に処理されました。</p>
        </section>
        
        <section class="application-approval-section">
            <ul class="application-approval-ul-1">
                <li class="approved-artist-img-li">
                    <p>
					    <c:set var="groupId" value="${application.groupId}" />
						<c:set var="imagePath" value="${pictureImageMap[groupId]}" />
						<c:if test="${not empty imagePath}">
						    <img src="${pageContext.request.contextPath}${imagePath}" 
						         alt="バンドのイラスト" 
						         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;" />
						</c:if>
					</p>
					 
                </li>
            </ul>
            
            <div class="application-approval-div1">
                <c:choose>
                    <c:when test="${application != null}">
                        <p>申請日</p>
                        <p>${application.datetime}</p> <!-- datetimeの値を表示 -->
                        
                        <ul class="application-approval-ul-2">
                            <li class="application-approval-li-1"><p>予約者名</p></li>
                            <li class="application-approval-li-2">
                                <p>${application.us_name != null ? application.us_name : '名前なし'}</p>
                            </li>
                        </ul>
                        
                        <ul class="application-approval-ul-2">
                            <li class="application-approval-li-1"><p>予約日時</p></li>
                            <li class="application-approval-li-2">
                                <c:choose>
                                    <c:when test="${not empty formattedDateTime}">
                                        <p>${formattedDateTime}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>未定</p>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </ul>
                        
                        <!-- 申請情報を表示 -->
                        <h2>承認予約詳細</h2>
                        <ul class="application-details-ul">
                            <li>申請ID: ${application.applicationId}</li>
                            <li>予約者名: ${application.accountName} ${application.us_name}</li>
                            <li>予約日時: ${application.datetime}</li>
                            <li>開始時間: ${application.startTime}</li>
                            <li>終了時間: ${application.finishTime}</li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p>データがありません。</p>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="application-approval-div2">
                <ul class="application-approval-ul-3">
                    <!-- 必要に応じて他のデータを追加 -->
                </ul>
            </div>
        </section>

        <!-- 対バンの場合の情報を追加 -->
        <c:if test="${cogigOrSolo == 2}">
            <div class="band-confirmation-container">
                <h2>対バングループ情報</h2>
                <div class="confirmation-profile">
                    <div class="confirmation-artist-img-div">
                        <c:set var="groupId" value="${application.groupId}" />
						<c:set var="imagePath" value="${pictureImageMap[groupId]}" />
						<c:if test="${not empty imagePath}">
						    <img src="${pageContext.request.contextPath}${imagePath}" 
						         alt="バンドのイラスト" 
						         style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;" />
						</c:if>

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
        
        <h2>承認ページから予約の確認・削除ができます。</h2>
        
        <div class="application-approval-backhome-btn-div">
            <a href="<%= request.getContextPath() %>/Livehouse_home" class="application-approval-backhome-btn">ホームへ戻る</a>
        </div>
    </main>
</body>
</html>
