<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
	<title>ライブハウスマイページ</title>
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
                <div class="live-my-inputcontener">
                    <input class="livehouse-name" type="text" name="" placeholder="ライブハウス名を入力してください"><br>
                    <input class="livehouse-owner" type="text" name="" placeholder="オーナー名を入力してください">
                </div>
        
                <div class="livehouse-info">
                    <h3 class="subsubtitle">ライブハウス主要情報</h3>
                    <textarea class="main-text" id="message" name="message" placeholder="※ここに入力" rows="4" cols="50"></textarea>
                    <h3 class="live-exterior-interior">ライブハウスの外観内観・動画</h3>

                    
                    <!-- <figure class="livehouse-picture">
                        <img src="../assets/img/ライブハウス内観.jpg" alt="内観の写真" class="gaikan">
                        <img src="../assets/img/ライブハウス外観.jpg" alt="外観の写真" class="naikan">
                    </figure> -->


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
                    
                    

                    <h3 class="live-detail-info">ライブハウス詳細情報</h3>
                    <textarea class="livehouse-detail" id="message" name="message" rows="4" cols="50" placeholder="※機材情報などここに入力"></textarea>
                </div>
                <div class="live-mypage-keep">
                    <button class="keep-btn">保存</button>
                </div>

            </div>
        </main>
</body>
</html>