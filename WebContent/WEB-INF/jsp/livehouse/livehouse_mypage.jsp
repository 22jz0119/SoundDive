<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
                    <li><a href="livehouse_home.html">HOME</a></li>
                    <li><a href="livehouse_mypage.html">MY PAGE</a></li>
                    <li><a href="">000</a></li>
                    <li><a href="">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <!-- メインフォーム -->
        <form action="<%= request.getContextPath() %>/Livehouse_mypage" method="POST" enctype="multipart/form-data">
            <!-- プロフィール画像 -->
            <div class="profile-container">
	            <label class="profile-icon" for="fileInput">
	                <span class="placeholder-text">アイコンをアップロード</span>
	                <img id="profileImage" src="" alt="" style="display: none;">
	            </label>
	            <input type="file" id="fileInput" name="picture_image_naigaikan" accept="image/*" style="display: none;" onchange="previewImage()">
	        </div>

            <!-- 基本情報入力 -->
            <div class="livehouse_mypage-inputfield-containar">
                <ul class="livehouse_mypage-inputfield-ul1">
                    <li class="livehouse_mypage-inputfield-livehouse-name">
                        <label for="livehouseName" class="livehouse_mypage-guide-livehousename">ライブハウス名</label>
                        <input type="text" id="livehouseName" name="livehouseName" required>
                    </li>
                    <li class="livehouse_mypage-inputfield-onername">
                        <label for="ownerName" class="livehouse_mypage-guide-onername">オーナー名</label>
                        <input type="text" id="ownerName" name="ownerName" required>
                    </li>
                    <li class="livehouse_mypage-inputfield-tel">
                        <label for="liveTelNumber" class="livehouse_mypage-guide-tel">電話番号</label>
                        <input type="tel" id="liveTelNumber" name="liveTelNumber" required>
                    </li>
                </ul>
            </div>
            <div class="livehouse-mypage-livehouseDetail">
            
                <label for="livehouse-mp-livehouse-detail">ライブハウス説明情報</label>
                <input type="text" id="livehouseExplanation" name="livehouseExplanation" style="width: 500px; height: 150px; " required>
        </div>
        <div class="livehouse-mypage-livehouseDiscription"> 
            
                <label for="livehouse-mp-livehouse-description">ライブハウス詳細情報</label>
                <input type="text" id="livehouseDetailed" name="livehouseDetailed" style="width: 500px; height: 150px;" required>
        </div>
        <div class="livehouse-mypage-gearinfo">
                <label for="livehouse-mp-gearinfo">機材情報</label>
                <input type="text" id="equipmentInformation" name="equipmentInformation" style="width: 500px; height: 150px;" required>
        </div>

            <!-- 画像アップロード -->
            <figure class="livehouse-picture">
                <!-- 内観画像 -->
                <div class="image-container">
                    <img id="naikan-preview" src="../assets/img/ライブハウス内観.jpg" alt="内観の写真" style="display: none; width: 300px; height: 200px; object-fit: cover;">
                    <input type="file" accept="image/*" id="naikan-input" name="naikanImage" required>
                    <label for="naikan-input">内観画像を選択</label>
                </div>

                <!-- 外観の画像 -->
                <div class="image-container">
                    <img id="gaikan-preview" src="../assets/img/ライブハウス外観.jpg" alt="外観の写真" style="display: none; width: 300px; height: 200px; object-fit: cover;">
                    <input type="file" accept="image/*" id="gaikan-input" name="gaikanImage" required>
                    <label for="gaikan-input">外観画像を選択</label>
                </div>
            </figure>

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
            // プロフィール画像プレビュー
	        document.getElementById('fileInput').addEventListener('change', function(event) {
	            const input = event.target; // ファイル入力要素を取得
	            if (input.files && input.files[0]) {
	                const reader = new FileReader(); // FileReaderオブジェクトを作成
	                reader.onload = function(e) {
	                    const profileImage = document.getElementById('profileImage'); // プレビュー画像要素
	                    const placeholderText = document.getElementById('placeholderText'); // プレースホルダー
	                    
	                    profileImage.src = e.target.result; // プレビュー画像を設定
	                    profileImage.style.display = 'block'; // プレビュー画像を表示
	                    placeholderText.style.display = 'none'; // プレースホルダーを非表示
	                };
	                reader.readAsDataURL(input.files[0]); // ファイルをDataURLとして読み込む
	            }
	        });

            // 汎用的なプレビュー関数
            function previewImage(event, previewId) {
                const input = event.target;
                if (input.files && input.files[0]) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const previewImage = document.getElementById(previewId);
                        previewImage.src = e.target.result;
                        previewImage.style.display = 'block';
                    };
                    reader.readAsDataURL(input.files[0]);
                }
            }

            // 内観画像のプレビュー設定
            document.getElementById('naikan-input').addEventListener('change', function(event) {
                previewImage(event, 'naikan-preview');
            });

            // 外観画像のプレビュー設定
            document.getElementById('gaikan-input').addEventListener('change', function(event) {
                previewImage(event, 'gaikan-preview');
            });
        </script>
    </main>
</body>
</html>