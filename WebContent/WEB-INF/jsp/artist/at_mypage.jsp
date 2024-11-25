<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>アーティストマイページ</title>
</head>
<body>
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="at_home.jsp">HOME</a></li>
                    <li><a href="at_mypage.jsp">MY PAGE</a></li>
                    <li><a href="#">000</a></li>
                    <li><a href="#">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <h2>マイページ</h2>
    
    <form action="<%= request.getContextPath() %>/At_Mypage" method="POST" enctype="multipart/form-data">
        <div class="profile-container">
            <label class="profile-icon" for="fileInput">
                <span class="placeholder-text">アイコンをアップロード</span>
                <img id="profileImage" src="" alt="" style="display: none;">
            </label>
            <input type="file" id="fileInput" name="profile_icon" accept="image/*" style="display: none;" onchange="previewImage()">
        </div>            
        
        <!-- バンド名入力 -->
        <div class="form-group-1">
            <input type="text" id="band-name" class="form-groupp" name="band_name" placeholder="バンド名を入力" required>
        </div>
        
        <!-- メンバー詳細 -->
        <div class="member-details">
            <div class="member-1">
                <input type="text" class="profile-card" id="member1-name" name="member1_name" placeholder="氏名" required><br>
                <input type="text" class="profile-card p-c-sub" id="member1-role" name="member1_role" placeholder="役割 例: ボーカル" required><br>
            </div>
            <div class="member-1">
                <input type="text" class="profile-card" id="member2-name" name="member2_name" placeholder="氏名" required><br>
                <input type="text" class="profile-card p-c-sub" id="member2-role" name="member2_role" placeholder="役割 例: ギター" required><br>
            </div>
            <div class="member-1">
                <input type="text" class="profile-card" id="member3-name" name="member3_name" placeholder="氏名" required><br>
                <input type="text" class="profile-card p-c-sub" id="member3-role" name="member3_role" placeholder="役割 例: ドラム" required><br>
            </div>
            <div class="member-1">
                <input type="text" class="profile-card" id="member4-name" name="member4_name" placeholder="氏名" required><br>
                <input type="text" class="profile-card p-c-sub" id="member4-role" name="member4_role" placeholder="役割 例: ベース" required><br>
            </div>
        </div>

        <!-- バンド歴入力 -->
        <div class="form-group-2">
            <textarea id="band-history" name="band_history" placeholder="バンド歴、詳細など.." rows="4" required></textarea>
        </div>

        <!-- サンプル音源 -->
        <div class="form-group-3">
            <h2>サンプル音源</h2>
            <label for="sample-music">サンプル音源をアップロード:</label><br>
            <input type="file" id="sample-music" name="sample_music" accept="audio/*">
        </div>

        <!-- 送信ボタン -->
        <div class="form-group-4">
            <input type="submit" value="送信" class="form-group-4">
        </div>
    </form>

    <script>
        function previewImage() {
            const file = document.getElementById('fileInput').files[0];
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('profileImage').src = e.target.result;
                document.getElementById('profileImage').style.display = 'block';
            }
            if (file) {
                reader.readAsDataURL(file);
            }
        }
    </script>
</body>
</html>