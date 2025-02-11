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
    
    <% if (request.getAttribute("msg") != null) { %>
    <script>
        alert("<%= request.getAttribute("msg") %>");
    </script>
<% } %>

    
    <main class="top-main">
        <div class="top-main-containar">
            <h1 class="top-main-title">SoundDive</h1>
            <p class="top-title-detail">アーティストとライブハウスのブッキングサービス</p>
            <button id="scroll-to-login" class="scroll-login-btn">ログインはこちら</button>
        </div>

        <div class="top-catchphrase">
            <h3 class="top-catchphrase-h3">音楽と出会いを繋ぐ、ライブハウスとアーティストの架け橋</h3>
        </div>

        <div class="top-explain">
            <p class="top-explain-txt">SoundDiveは、アーティストとライブハウスをつなぐために設計された画期的なブッキングサービスです。</p>
        </div>


            <form action="<%= request.getContextPath() %>/Top" method="post">
                <ul class="top-login-ul">
                    <li class="top-login-title" id="top-login-title"><p>LogIn</p></li>
                    <li class="top-login-ID"><p>Tel Number</p></li>
                    <li class="top-login-id-txt"><input 
					    type="text" 
					    id="top-loginId-txtbox" 
					    name="tel_number" 
					    maxlength="11" 
					    pattern="\d{10,11}" 
					    required 
					    title="電話番号は10桁または11桁の数字で入力してください。">
					</li>
                    <li class="top-login-pass"><p>PassWord</p></li>
                    <li class="top-login-pass-txt"><input type="password" id="top-loginPass-textbox" name="password"></li>
                    <li><button type="submit" class="top-login-btn">Login</button></li>
                </ul>
            </form>


            <ul class="top-newaccount">
                <li class="top-newaccount-btnframe"><button type="button" id="top-newaccount-btn" onclick="location.href='<%= request.getContextPath() %>/New_Acount'">NewAccount</button></li>
            </ul>
        </div>
    </main>
    
    <script>
        document.getElementById("scroll-to-login").addEventListener("click", function() {
            const target = document.getElementById("top-login-title");
            const targetPosition = target.getBoundingClientRect().top + window.scrollY;
            const startPosition = window.scrollY;
            const distance = targetPosition - startPosition;
            const duration = 2000; // スクロールの所要時間（ミリ秒）
            let startTime = null;

            function scrollAnimation(currentTime) {
                if (startTime === null) startTime = currentTime;
                const timeElapsed = currentTime - startTime;
                const scrollAmount = easeInOutQuad(timeElapsed, startPosition, distance, duration);

                window.scrollTo(0, scrollAmount);
                
                if (timeElapsed < duration) {
                    requestAnimationFrame(scrollAnimation);
                }
            }

            function easeInOutQuad(t, b, c, d) {
                t /= d / 2;
                if (t < 1) return c / 2 * t * t + b;
                t--;
                return -c / 2 * (t * (t - 2) - 1) + b;
            }

            requestAnimationFrame(scrollAnimation);
        });
    </script>
</body>
</html>
