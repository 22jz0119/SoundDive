<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                    <li><a href="At_Home">HOME</a></li>
                    <li><a href="At_Mypage">MY PAGE</a></li>
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
                <c:choose>
                    <c:when test="${not empty userGroup.picture_image_movie}">
                        <!-- 条件分岐: 保存済み画像がある場合 -->
                        <img src="${pageContext.request.contextPath}/uploads/${userGroup.picture_image_movie}" alt="Profile Image" />
                    </c:when>
                    <c:otherwise>
                        <!-- 条件分岐: 画像がない場合 -->
                        <span class="placeholder-text">アイコンをアップロード</span>
                        <img id="profileImage" src="" alt="" style="display: none;">
                    </c:otherwise>
                </c:choose>
            </label>
            <input type="file" id="fileInput" name="picture_image_movie" accept="image/*" style="display: none;" onchange="previewImage()">
        </div>

        <!-- バンド名入力 -->
        <div class="form-group-1">
            <c:choose>
                <c:when test="${not empty userGroup}">
                    <input type="text" id="band-name" class="form-groupp" name="account_name" 
                           placeholder="バンド名を入力" value="${userGroup.account_name}" required>
                </c:when>
                <c:otherwise>
                    <input type="text" id="band-name" class="form-groupp" name="account_name" 
                           placeholder="バンド名を入力" required>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- メンバー詳細 -->
        <div id="member-details-container">
            <c:forEach var="member" items="${members}">
                <div class="member-detail">
                    <input type="text" class="profile-card" name="member_name[]" placeholder="氏名" 
                           value="${member.member_name}" required><br>
                    <input type="text" class="profile-card p-c-sub" name="member_role[]" placeholder="役割 例: ボーカル" 
                           value="${member.member_position}" required><br>
                </div>
            </c:forEach>
        </div>
        <button type="button" onclick="addMember()">メンバーを追加</button>

        <!-- バンド歴入力 -->
        <div class="form-group-2">
            <c:choose>
                <c:when test="${not empty userGroup}">
                    <textarea id="band-history" name="band_history" placeholder="バンド歴、詳細など.." rows="4" required>${userGroup.band_years}</textarea>
                </c:when>
                <c:otherwise>
                    <textarea id="band-history" name="band_history" placeholder="バンド歴、詳細など.." rows="4" required></textarea>
                </c:otherwise>
            </c:choose>
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
                const profileImage = document.getElementById('profileImage');
                profileImage.src = e.target.result;
                profileImage.style.display = 'block';

                // 「アイコンをアップロード」を非表示
                const placeholderText = document.querySelector('.placeholder-text');
                if (placeholderText) placeholderText.style.display = 'none';
            }
            if (file) {
                reader.readAsDataURL(file);
            }
        }

        function addMember() {
            const container = document.getElementById('member-details-container');
            const memberDetail = document.createElement('div');
            memberDetail.className = 'member-detail';

            memberDetail.innerHTML = ` 
                <input type="text" class="profile-card" name="member_name[]" placeholder="氏名" required><br>
                <input type="text" class="profile-card p-c-sub" name="member_role[]" placeholder="役割 例: ボーカル" required><br>
            `;

            container.appendChild(memberDetail);
        }
    </script>
</body>
</html>
