<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <section class="artist-livehouse-detail-section">
        <!-- タイトル -->
        <div class="artist-livehouse-detail-title">
            <h1 class="artist-livehouse-rv-title-h1">予約時間確定画面</h1>
        </div>
    
        <!-- 日付 -->
        <p class="timeselection">${selectedYear}年${selectedMonth}月${selectedDay}日</p>
    
        <!-- デバッグ用の値表示 -->
        
        <%-- 
        <section>
		    <h3>デバッグ情報</h3>
		    <p>Year: ${selectedYear}</p>
		    <p>Month: ${selectedMonth}</p>
		    <p>Day: ${selectedDay}</p>
		    <p>LivehouseId: ${livehouseId}</p>
		    <p>LivehouseType: ${livehouseType}</p>
		    <c:if test="${livehouseType == 'multi'}">
		        <p>UserId: ${userId}</p>
		        <p>ApplicationId: ${applicationId}</p>
		    </c:if>
		</section>
		--%>



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
                    <li class="A-t-discription-li1"><p>オーナー: ${livehouse.owner_name}</p></li>
                    <li class="A-t-discription-li2"><p>住所: ${livehouse.live_address}</p></li>
                    <li class="A-t-discription-li3"><p>ライブハウス説明情報</p></li>
                    <li class="A-t-discription-li4"><p>${livehouse.livehouse_explanation_information}</p></li>
                    <li class="A-t-discription-li5"><p>ライブハウス詳細情報</p></li>
                    <li class="A-t-discription-li6"><p>${livehouse.livehouse_detailed_information}</p></li>
                </ul>
            </div>
        </div>
        
    </section>
    
    
    <c:if test="${livehouseType eq 'multi'}">
	    <h2 class="Currentlyapplying">申請中アーティスト</h2>
	    <div class="Applicationgroup">
	        <c:forEach var="artist" items="${applyingArtists}">
	            <div class="Applicationtable">
	                <img src="${pageContext.request.contextPath}${userGroup.picture_image_movie}" alt="" class="band-image">
	                <div class="artist-info">
	                    <p class="artist-name">${artist.name}</p>
	                    <p class="application-status">申請中</p>
	                </div>
	            </div>
	        </c:forEach>
	    </div>
	</c:if>

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
    
    <!-- 時間選択フォーム -->
	<form action="<%= request.getContextPath() %>/At_booking_confirmation" method="get" class="timeschedule">
		<div class="at-reservation-select-time">
			<label for="time" class="at-reservation-select-time-announce">開始時間を選択してください:</label>
		    <select id="time" name="time" class="timeschedule" required>
		        <option value="08:00">08:00</option>
		        <option value="09:00">09:00</option>
		        <option value="10:00">10:00</option>
		        <option value="11:00">11:00</option>
		        <option value="12:00">12:00</option>
		        <option value="13:00">13:00</option>
		        <option value="14:00">14:00</option>
		        <option value="15:00">15:00</option>
		        <option value="16:00">16:00</option>
		        <option value="17:00">17:00</option>
		        <option value="18:00">18:00</option>
		    </select>
		
		    <!-- 共通の入力項目 -->
		    <input type="hidden" name="year" value="${selectedYear}">
		    <input type="hidden" name="month" value="${selectedMonth}">
		    <input type="hidden" name="day" value="${selectedDay}">
		    <input type="hidden" name="livehouseId" value="${livehouseId}">
		    <input type="hidden" name="livehouse_type" value="${livehouseType}">
		
		    <!-- マルチの場合のみ追加 -->
		    <c:if test="${livehouseType eq 'multi'}">
		        <input type="hidden" name="userId" value="${userId}">
		        <input type="hidden" name="applicationId" value="${applicationId}">
		    </c:if>
		
		    <input type="submit" value="確定" class="rv-btn">
		</div>
	    
	</form>
</body>
</html>
