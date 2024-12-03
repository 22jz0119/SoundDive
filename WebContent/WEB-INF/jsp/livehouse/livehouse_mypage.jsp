<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
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
    <form action="<%= request.getContextPath() %>/WEB-INF/jsp/livehouse/livehouse_mypage.jsp" method="POST">
        <div class="profile-container">
            <label class="profile-icon" for="fileInput">
                <!-- デフォルトのテキストを中央に表示 -->
                <span class="placeholder-text" id="placeholderText">アイコンをアップロード</span>
                <!-- プロフィール画像がアップロードされた後に表示される -->
                <img id="profileImage" src="" alt="">
            </label>
            <input type="file" id="fileInput" accept="image/*" style="display: none;">
        </div>
        <script>
            document.getElementById('fileInput').addEventListener('change', function(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const img = document.getElementById('profileImage');
                        const placeholderText = document.getElementById('placeholderText');
    
                        // 画像を表示し、プレースホルダーテキストを隠す
                        img.src = e.target.result;
                        img.style.display = 'block';  // 画像を表示
                        placeholderText.style.display = 'none'; // テキストを非表示
                    }
                    reader.readAsDataURL(file);
                }
            });
        </script>
        <button class="edit-btn">編集する</button>
        <div class="livehouse_mypage-inputfield-containar">
            <ul class="livehouse_mypage-inputfield-ul1">
                <li class="livehouse_mypage-inputfield-livehouse-name">
                    <form>
                        <label for="livehouseName" class="livehouse_mypage-guide-livehousename">ライブハウス名</label>
                        <input type="text" id="livehouseName" name="livehouseName">
                    </form>
                </li>
                <li class="livehouse_mypage-inputfield-onername">
                    <form>  
                        <label for="ownerName" class="livehouse_mypage-guide-onername">オーナー名</label>       
                        <input type="text" id="ownerName" name="ownerName">
                    </form>
                </li>
                <li class="livehouse_mypage-inputfield-tel">
                    <form>
                        <label for="liveTelNumber" class="livehouse_mypage-guide-tel">電話番号</label>
                        <input type="tel" id="liveTelNumber" name="liveTelNumber" required>
                    </form>
                </li>
            </ul>
        </div>
        <div class="livehouseDetailed">
            <form>
                <label for="livehouse-mp-livehouse-detail">ライブハウス説明情報</label>
                <input type="text" id="livehouseDetailed" name="livehouseDetailed" style="width: 500px; height: 150px;">
            </form>
        </div>
        <div class="livehouse-mypage-livehouseDiscription"> 
            <form>
                <label for="livehouseDetailed">ライブハウス詳細情報</label>
                <input type="text" id="livehouseDetailed" name="livehouseDetailed" style="width: 500px; height: 150px;">
            </form>
        </div>
        <div class="livehouse-mypage-gearinfo">
            <form>
                <label for="equipmentInformation">機材情報</label>
                <input type="text" id="equipmentInformation" name="equipmentInformation" style="width: 500px; height: 150px;">
            </form>
        </div>
        <div>
            <h1>ライブハウスの外観内観・動画</h1>
        </div>

        <figure class="livehouse-picture">
            <!-- 内観の画像 -->
            <div class="image-container">
                <img id="naikan-preview" src="../assets/img/ライブハウス内観.jpg" alt="内観の写真" class="gaikan">
                <input type="file" accept="image/*" id="naikan-input" onchange="previewImage(event, 'naikan-preview')">
                <label for="naikan-input">内観画像を選択</label>
            </div>
            
            <!-- 外観の画像 -->
            <div class="image-container">
                <img id="gaikan-preview" src="../assets/img/ライブハウス外観.jpg" alt="外観の写真" class="naikan">
                <input type="file" accept="image/*" id="gaikan-input" onchange="previewImage(event, 'gaikan-preview')">
                <label for="gaikan-input">外観画像を選択</label>
            </div>
        </figure>
        
        <script>
            function previewImage(event, previewId) {
                const input = event.target;
                if (input.files && input.files[0]) {
                    const reader = new FileReader();
                    
                    reader.onload = function(e) {
                        const previewImage = document.getElementById(previewId);
                        previewImage.src = e.target.result;
                    };
                    
                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
        <div class="live-mypage-keep">
            <button type= submit="keep-btn">登録</button>
        </div>
        </form>
    </main>
</body>
</html>