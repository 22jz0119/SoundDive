<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウスマイページ</title>
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
                    <li><a href="<%= request.getContextPath() %>/At_Details" >HOME</a></li>
                    <li><a href="livehouse_mypage.html">MY PAGE</a></li>
                    <li><a href="#">000</a></li>
                    <li><a href="#">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <!-- メインフォーム -->
        <form action="<%= request.getContextPath() %>/Livehouse_mypage" method="POST" enctype="multipart/form-data">
            <!-- プロフィール画像 
			<div class="profile-container">
			    <label class="profile-icon" for="fileInput">
			        <span class="placeholder-text">アイコンをアップロード</span>

			        <img id="profileImage" 
			             src="${livehouse != null && livehouse.picture_image_naigaikan != null ? pageContext.request.contextPath + livehouse.picture_image_naigaikan : ''}" 
			             alt="プロフィール画像" 
			             style="display: ${livehouse != null && livehouse.picture_image_naigaikan != null ? 'block' : 'none'};">
			    </label>
			    <input type="file" id="fileInput" name="picture_image_naigaikan" accept="image/*" style="display: none;" onchange="previewImage(event, 'profileImage')">
			</div>


<!-- プロフィール画像 -->
        <div class="profile-container">
            <label class="profile-icon" for="fileInput">
                <c:choose>
                    <c:when test="${not empty userGroup.livehouse.picture_image_naigaikan}">
                        <!-- 画像がある場合 -->
                        <img id="profileImage" src="${pageContext.request.contextPath}${livehouse.picture_image_naigaikan}" alt="Profile Image" />
                    </c:when>
                    <c:otherwise>
                        <!-- 画像がない場合 -->
                        <span class="placeholder-text">アイコンをアップロード</span>
                        <img id="profileImage" src="" alt="" style="display: none;">
                    </c:otherwise>
                </c:choose>
            </label>
            <input type="file" id="fileInput" name="picture_image_movie" accept="image/*" style="display: none;" onchange="previewImage()">
        </div>
        
        
        
        


            <!-- 基本情報入力 -->
            <div class="livehouse_mypage-inputfield-containar">
                <ul class="livehouse_mypage-inputfield-ul1">
                    <li class="livehouse_mypage-inputfield-livehouse-name">
                        <label for="livehouseName">ライブハウス名</label>
                        <input type="text" id="livehouseName" name="livehouseName" 
       						value="${livehouse != null ? livehouse.livehouse_name : ''}" required>

                    </li>
                    <li class="livehouse_mypage-inputfield-onername">
                        <label for="ownerName">オーナー名</label>
                        <input type="text" id="ownerName" name="ownerName" 
                               value="${livehouse != null ? livehouse.owner_name : ''}" required>
                    </li>
                    <li class="livehouse_mypage-inputfield-tel">
                        <label for="liveTelNumber">電話番号</label>
                        <input type="text" id="liveTelNumber" name="liveTelNumber" 
                               value="${livehouse != null ? livehouse.live_tel_number : ''}" required>
                    </li>
                </ul>
            </div>

            <div class="livehouse-mypage-livehouseDetail">
                <label for="livehouseExplanation">ライブハウス説明情報</label>
                <textarea id="livehouseExplanation" name="livehouseExplanation" style="width: 500px; height: 150px;" required>${livehouse != null ? livehouse.livehouse_explanation_information : ''}</textarea>
            </div>

            <div class="livehouse-mypage-livehouseDiscription"> 
                <label for="livehouseDetailed">ライブハウス詳細情報</label>
                <textarea id="livehouseDetailed" name="livehouseDetailed" style="width: 500px; height: 150px;" required>${livehouse != null ? livehouse.livehouse_detailed_information : ''}</textarea>
            </div>

            <div class="livehouse-mypage-gearinfo">
                <label for="equipmentInformation">機材情報</label>
                <textarea id="equipmentInformation" name="equipmentInformation" style="width: 500px; height: 150px;" required>${livehouse != null ? livehouse.equipment_information : ''}</textarea>
            </div>

            <!-- 画像アップロード -->
			<figure class="livehouse-picture">
			    <!-- 内観画像 -->
			    <div class="image-container">
			        <img id="naikan-preview" 
			             src="${livehouse_information != null && livehouse_information.picture_image_naigaikan != null ? pageContext.request.contextPath + livehouse_information.picture_image_naigaikan : ''}"  
			             alt="内観画像" 
			             style="${livehouse_information != null && livehouse_information.picture_image_naigaikan != null ? 'display: block;' : 'display: none;'}">
			        <input type="file" accept="image/*" id="naikan-input" name="naikanImage" onchange="previewImage(event, 'naikan-preview')">
			        <label for="naikan-input">内観画像を選択</label>
			    </div>
			
			    <!-- 外観画像 -->
			    <div class="image-container">
			        <img id="gaikan-preview" 
			             src="${livehouse_information != null && livehouse_information.picture_image_naigaikan != null ? pageContext.request.contextPath + livehouse_information.picture_image_naigaikan : ''}"  
			             alt="外観画像" 
			             style="${livehouse_information != null && livehouse_information.picture_image_naigaikan != null ? 'display: block;' : 'display: none;'}">
			        <input type="file" accept="image/*" id="gaikan-input" name="gaikanImage" onchange="previewImage(event, 'gaikan-preview')">
			        <label for="gaikan-input">外観画像を選択</label>
			    </div>
			</figure>
			
			
			<!-- プロフィール画像 -->
        <div class="profile-container">
            <label class="profile-icon" for="fileInput">
                <c:choose>
                    <c:when test="${not empty userGroup.picture_image_movie}">
                        <!-- 画像がある場合 -->
                        <img src="${pageContext.request.contextPath}${userGroup.picture_image_movie}" alt="Profile Image" />
                    </c:when>
                    <c:otherwise>
                        <!-- 画像がない場合 -->
                        <span class="placeholder-text">アイコンをアップロード</span>
                        <img id="profileImage" src="" alt="" style="display: none;">
                    </c:otherwise>
                </c:choose>





            <!-- 登録ボタン -->
            <div class="live-mypage-keep">
                <button type="submit" class="keep-btn">登録</button>
            </div>
        </form>

        <!-- エラーメッセージの表示 -->
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <p><%= request.getAttribute("errorMessage") %></p>
            </div>
        <% } %>
        
         <!-- スクリプト -->
        <script>
     // プレビュー関数 (指定されたプレビューIDに画像を表示)
	        function previewImage(event, previewId) {
	            const input = event.target;
	            if (input.files && input.files[0]) {
	                const reader = new FileReader();
	                reader.onload = function (e) {
	                    const previewImage = document.getElementById(previewId);
	                    previewImage.src = e.target.result; // 選択した画像をプレビュー
	                    previewImage.style.display = 'block'; // 表示を有効化
	                };
	                reader.readAsDataURL(input.files[0]);
	            }
	        }


            // ページ読み込み時に保存済み画像がある場合は表示
            window.onload = function () {
                const profileImage = document.getElementById('profileImage');
                const naikanPreview = document.getElementById('naikan-preview');
                const gaikanPreview = document.getElementById('gaikan-preview'); 

                // 保存された画像がある場合、その画像を表示
                if (profileImage && profileImage.src) {
                    profileImage.style.display = 'block';
                }
                if (naikanPreview && naikanPreview.src) {
                    naikanPreview.style.display = 'block';
                }
                if (gaikanPreview && gaikanPreview.src) {
                    gaikanPreview.style.display = 'block';
                }
            };
        </script>


    </main>
</body>
</html>
