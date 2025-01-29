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
    <h2>マイページ</h2>
    
    <form action="<%= request.getContextPath() %>/At_Mypage" method="POST" enctype="multipart/form-data">
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
            </label>
            <input type="file" id="fileInput" name="picture_image_movie" accept="image/*" style="display: none;" onchange="previewImage()">
        </div>

        <!-- バンド名入力 -->
        <div class="form-group-1">
        	<div>
        		<p>アーティスト名</p>
        	</div>
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
                    <!-- メンバーID（隠しフィールド） -->
                    <input type="hidden" name="existing_member_ids[]" value="${member.id}">
                    
                    <!-- 氏名 -->
                    <div class="plus-member-details-div1">
                    	<p class="plus-member-detail-title1">メンバー名</p>
                    	<input type="text" class="" name="member_name[]" placeholder="氏名" 
                           value="${member.member_name}" required>
                    </div>
                    
                    
                    <!-- 役割 -->
                    <div class="plus-member-details-div2">
                    	<p class="plus-member-detail-title2">ポジション</p>
                    	<input type="text" class="" name="member_role[]" placeholder="役割 例: ボーカル" 
                           value="${member.member_position}" required>
                    </div>
                    
                    
                    <!-- 削除チェックボックス -->
                    <%-- 
                    <div class="plus-member-details-div3">
                    	<label>
                        	<input type="checkbox" name="deleted_member_ids[]" value="${member.id}">
                    	</label>
                    </div>
                    --%>
                   
                </div>
            </c:forEach>
        </div>
        <div class="plus-member-detail-btn">
        	<button type="button" onclick="addMember()" class="plus-member-detail-button">メンバーを追加</button>
        </div>
        
        
        <!-- ジャンル入力 -->
		<div class="form-group-genre">
		    <label for="group-genre">ジャンル</label>
		    <c:choose>
		        <c:when test="${not empty userGroup}">
		            <input type="text" id="group-genre" name="group_genre" placeholder="ジャンルを入力" value="${userGroup.group_genre}" required>
		        </c:when>
		        <c:otherwise>
		            <input type="text" id="group-genre" name="group_genre" placeholder="ジャンルを入力" required>
		        </c:otherwise>
		    </c:choose>
		</div>

        <!-- バンド歴入力 -->
        
        <div class="form-group-2">
        	<p class="form-group-2-p">バンド歴</p>
            <c:choose>
                <c:when test="${not empty userGroup}">
                    <textarea id="band-history" name="band_years" placeholder="バンド歴" rows="4" required>${userGroup.band_years}</textarea>
                </c:when>
                <c:otherwise>
                    <textarea id="band-history" name="band_years" placeholder="バンド歴" rows="4" required></textarea>
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
                <button type="button" class="remove-member-button" onclick="removeMember(this)">閉じる</button>
            `;

            container.appendChild(memberDetail);
        }

        function removeMember(button) {
            const memberDetail = button.parentElement; // ボタンの親要素（メンバーフィールド全体）を取得
            memberDetail.remove(); // フィールドを削除
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
