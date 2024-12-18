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
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/Livehouse_home">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/Livehouse_mypage">MY PAGE</a></li>
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main class="application-approval-main">
        <section class="main-application_approval">
            <div class="application_approvaled">
                <h2 class="application_approvaled-h2">アーティストを承認しました</h2>
            </div>
            <p class="approvaled-application-title">以下の予約が完了されました。</p>
        </section>
        <h1>test</h1>

        <section class="application-approval-section">
            <ul class="application-approval-ul-1">
                <li class="approved-artist-img-li">
                    <p><img class="approved-artist-img" src="<%= request.getContextPath() %>/assets/img/アーティスト画像.png" alt="アーティスト画像"></p>
                </li>
            </ul>

            <div class="application-approval-div1">
                <c:choose>
                    <c:when test="${application != null}">
                         <ul class="application-approval-ul-2">
                            <li class="application-approval-li-1"><p>予約者名</p></li>
                            <li class="application-approval-li-2">
                                <p>${application.us_name != null ? application.us_name : '名前なし'}</p>
                            </li>
                        </ul>

                        <ul class="application-approval-ul-2">
                            <li class="application-approval-li-1"><p>予約日時</p></li>
                            <li class="application-approval-li-2">
                                <!-- date_timeをフォーマットして表示 -->
                                <fmt:formatDate value="${application.datetime}" pattern="yyyy-MM-dd HH:mm:ss" var="formattedDate" />
                                <!-- フォーマットされた日時を表示 -->
                                <p>${formattedDate != null ? formattedDate : '未定'}</p>
                            </li>
                        </ul>

                        <ul class="application-approval-ul-2">
                            <li class="application-approval-li-1"><p>前払い金額</p></li>
                            <li class="application-approval-li-2"><p>4000円</p></li>
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
        <div class="application-approval-backhome-btn-div">
            <a href="<%= request.getContextPath() %>/Livehouse_home" class="application-approval-backhome-btn">ホームへ戻る</a>
        </div>
    </main>
</body>
</html>
