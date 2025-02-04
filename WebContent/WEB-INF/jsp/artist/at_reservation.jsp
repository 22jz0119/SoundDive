<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウス予約時間確定画面</title>
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
                    <li>
					    <a href="#" onclick="logoutAndRedirect();">ログアウト</a>
					</li>
                </ul>
            </nav>
        </div>
    </header>
    
    <section class="artist-livehouse-detail-section">
        <!-- タイトル -->
        <div class="artist-livehouse-detail-title">
            <h1 class="artist-livehouse-rv-title-h1">予約時間確定画面</h1>
        </div>
    
        <p class="timeselection">${selectedYear}年${selectedMonth}月${selectedDay}日</p>

        <!-- ライブハウス情報 -->
        <div class="A-t-detail-livehousename">
            <p class="artist-livehouse-detail-oner">${livehouse.livehouse_name}</p>
        </div>
    
        <!-- ライブハウス詳細と説明 -->
        <div class="artist_livehouse_details-Discription">
            <div>
                <img src="${pageContext.request.contextPath}${livehouse.picture_image_naigaikan}" class="artist_livehouse_details-img" alt="">
            </div>
            <div>
                <ul class="A-t-discription-ul">
                    <li><p>オーナー: ${livehouse.owner_name}</p></li>
                    <li><p>住所: ${livehouse.live_address}</p></li>
                    <li><p>ライブハウス説明情報: ${livehouse.livehouse_explanation_information}</p></li>
                    <li><p>ライブハウス詳細情報: ${livehouse.livehouse_detailed_information}</p></li>
                </ul>
            </div>
        </div>
    </section>
    
    <c:if test="${livehouseType eq 'multi'}">
	    <h2 class="Currentlyapplying">申請中アーティスト</h2>
	    <div class="Applicationgroup">
	    	<div class="Applicationtable">
	        	<img src="<c:url value='${pictureImageMovie}'/>" alt="バンドのイラスト" class="band-image">
		        <div class="artist-info">
	            	<p class="artist-name">${artistName}</p>
	                <p class="application-status">申請中</p>
	            </div>
	    	</div>
	    </div>
	</c:if>
	
	<form action="<%= request.getContextPath() %>/At_booking_confirmation" method="get" class="timeschedule">
	    <label for="time">開始時間を選択してください:</label>
	    <input type="time" id="time" name="time" class="timeschedule" required step="60">
	
	    <label for="finish_time">終了時間を選択してください:</label>
	    <input type="time" id="finish_time" name="finish_time" class="timeschedule" required step="60">
	
	    <input type="hidden" name="year" value="${selectedYear}">
	    <input type="hidden" name="month" value="${selectedMonth}">
	    <input type="hidden" name="day" value="${selectedDay}">
	    <input type="hidden" name="livehouseId" value="${livehouseId}">
	    <input type="hidden" name="livehouse_type" value="${livehouseType}">
	    <% Integer userId = (Integer) session.getAttribute("userId"); %>
		<input type="hidden" name="userId" value="<%= userId %>">

	    <c:if test="${livehouseType eq 'multi'}">
	        <input type="hidden" name="applicationId" value="${applicationId}">
	    </c:if>
	
	    <input type="submit" value="確定" class="rv-btn">
	</form>

	<script>
		document.addEventListener("DOMContentLoaded", function () {
		    const startTimeInput = document.getElementById("time");
		    const finishTimeInput = document.getElementById("finish_time");
		
		    startTimeInput.addEventListener("change", function () {
		        const startTime = startTimeInput.value;
		        updateFinishTimeMin(startTime);
		    });
		
		    function updateFinishTimeMin(startTime) {
		        if (startTime) {
		            finishTimeInput.min = startTime; // 開始時間以降しか選べないようにする
		        }
		    }
		});
	</script>

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
