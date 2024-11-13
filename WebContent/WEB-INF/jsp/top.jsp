<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>SoundDive</title>
</head>

<body>
    <div class="video-background">
        <video autoplay loop muted>
            <source src="<%= request.getContextPath() %>/assets/img/TestMovie.mp4" type="video/mp4">
        </video>
    </div>
    
    <main class="top-main">
        <div class="top-main-containar">
            <h1 class="top-main-title">SoundDive</h1>
            <p class="top-title-detail">アーティストとライブハウスのブッキングサービス</p>
        </div>

        <div class="top-catchphrase">
            <h3 class="top-catchphrase-h3">音楽と出会いを繋ぐ、ライブハウスとアーティストの架け橋</h3>
        </div>

        <div class="top-explain">
            <p class="top-explain-txt">SoundDiveは、アーティストとライブハウスをつなぐために設計された画期的なブッキングサービスです。</p>
        </div>

        <div class="top-login-containar">
            <form action="<%= request.getContextPath() %>/Top" method="post">
                <ul class="top-login-ul">
                    <li class="top-login-title"><p>LogIn</p></li>
                    <li class="top-login-ID"><p>Tel Number</p></li>
                    <li class="top-login-id-txt"><input type="text" id="top-loginId-txtbox" name="tel_number"></li>
                    <li class="top-login-pass"><p>PassWord</p></li>
                    <li class="top-login-pass-txt"><input type="password" id="top-loginPass-textbox" name="password"></li>
                    <li><button type="submit" class="top-login-btn">Login</button></li>
                </ul>
            </form>

            <ul>
                <li class="top-newaccount-btnframe"><button type="button" id="top-newaccount-btn" onclick="location.href='<%= request.getContextPath() %>/New_Acount'">NewAccount</button></li>
            </ul>
        </div>

        <!-- エラーメッセージの表示 -->
        <c:if test="${not empty msg}">
            <div class="error-message">${msg}</div>
        </c:if>

    </main>
</body>
</html>
