<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウス予約完了画面</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body>
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/At_Home">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/At_Mypage">MY PAGE</a></li>
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                    <li>
					    <a href="#" onclick="logoutAndRedirect();">ログアウト</a>
					</li>
                </ul>
            </nav>
        </div>
    </header>

    <section class="artist-co-gig-container">
        <div class="artist-co-gig-title">
            <h2 class="artist-co-gig-title-h2">申請が完了しました</h2>
        </div>
    </section>

    <h2 class="complete_sentence">ライブハウスからの承認をお待ちください</h2>
    


    <div class="artist-livehouse-detail-section3">
    	<ul>
    		<li>予約情報</li>
    		
    	</ul>
    	<ul>
    		<li>予約時間</li>
    		<li>${selectedYear}年${selectedMonth}月${selectedDay}日 ${selectedTime}</li>
    	</ul>
        <div class="A-t-detail-livehousename">
            <p class="artist-livehouse-detail-oner">${livehouse.livehouse_name}</p>
        </div>
        <div class="artist_livehouse_details-Discription">
            <div><img src="${pageContext.request.contextPath}${livehouse.picture_image_naigaikan}" alt="" class="artist_livehouse_details-img"></div>
            <div>
                <ul class="A-t-discription-ul">
                    <li><p>オーナー</p></li>
                    <li><p>${livehouse.owner_name}</p></li>
                    <li><p>住所</p></li>
                    <li><p>${livehouse.live_address}</p></li>
                    <li><p>電話番号</p></li>
                    <li><p>${livehouse.live_tel_number}</p></li>
                </ul>
            </div>
        </div>
    </div>
        <a href="<%= request.getContextPath() %>/At_Home" class="Return-to-home">ホームに戻る</a>
        
        <script>
        function previewImage() {
            const file = document.getElementById('fileInput').files[0];
            const reader = new FileReader();
            reader.onload = function(e) {
                const profileImage = document.getElementById('profileImage');
                profileImage.src = e.target.result;
                profileImage.style.display = 'block';

                const placeholderText = document.querySelector('.placeholder-text');
                if (placeholderText) placeholderText.style.display = 'none';
            }
            if (file) {
                reader.readAsDataURL(file);
            }
        }
    </script>
    
    <script>
	    function logoutAndRedirect() {
	        // フォームを送信してログアウト処理を実行
	        var form = document.createElement("form");
	        form.method = "post";
	        form.action = "<%= request.getContextPath() %>/At_Home";
	        
	        // 隠しフィールドにaction=logoutをセット
	        var input = document.createElement("input");
	        input.type = "hidden";
	        input.name = "action";
	        input.value = "logout";
	        form.appendChild(input);
	        
	        // フォームを送信
	        document.body.appendChild(form);
	        form.submit();
	    }
	</script>
</body>
</html>