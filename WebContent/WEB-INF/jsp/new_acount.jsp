<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
        <title>新規登録画面</title>
    </head>
    <body>
        <header class="main-header">
            <div class="header-container">
                <div class="main-title">
                    <h1 class="main-title-h1">Sound Dive</h1>
                </div>
                <nav class="header-nav">
                    <ul class="header-nav-ul">
                        <li><a href="<%= request.getContextPath() %>/artist_home.jsp">HOME</a></li>
                        <li><a href="<%= request.getContextPath() %>/artist_mypage.jsp">MY PAGE</a></li>
                        <li><a href="#">000</a></li>
                        <li><a href="#">000</a></li>
                    </ul>
                </nav>
            </div>
        </header>
        
        <h1 class="new-account">新規登録ページ</h1>
        
        <section class="Create-newacount-form">
            <form class="Login-info" action="<%= request.getContextPath() %>/submit" method="POST">
                <!-- 氏名入力欄 -->
                <h2 class="new-name">氏名<span class="Form-Item-Label-Required">必須</span></h2>
                <input class="info-text" type="text" id="name" name="name" placeholder="氏名" required><br>
    
                <!--ログインID-->
                <h2 class="new-login">ログインID<span class="Form-Item-Label-Required">必須</span></h2>
                <input class="info-id" type="text" id="login-id" name="login" placeholder="ログインID" required><br>
    
                <!-- パスワード入力欄 -->
                <h2 class="new-pass">パスワード<span class="Form-Item-Label-Required">必須</span></h2>
                <input class="info-pass" type="password" id="password" name="password" placeholder="パスワード" required><br>
    
                <!-- アカウントタイプの選択 -->
                <h2 class="new-choose">アカウント選択<span class="Form-Item-Label-Required">必須</span></h2>
                <div class="radio-group">
                    <input class="info-radio2" type="radio" id="artist" name="account_type" value="artist" required>
                    <label class="artist-ac" for="artist">アーティスト</label>
                    
                    <input class="info-radio2" type="radio" id="livehouse" name="account_type" value="livehouse" required>
                    <label class="livehouse-ac" for="livehouse">ライブハウス</label>
                </div>
    
                <!--電話番号-->
                <h2 class="new-tel">電話番号<span class="Form-Item-Label-Required">必須</span></h2>
                <input class="info-tel" type="tel" name="tel" pattern="[\d\-]*" placeholder="例）092999999999"><br>
                <span class="attention">※電話番号は半角英数字で入力してください。</span><br>
                <span class="attention">※ハイフンは"-"は不要です。</span><br>
    
                <!--住所 都道府県-->
                <h2 class="new-address">住所<span class="Form-Item-Label-Required">必須</span></h2>
                <input class="info-prefectures" type="text" placeholder="〇〇県〇〇市〇〇"><br>
                <span class="attention">※都道府県・市区町村を入力してください。</span><br>
    
                <!--住所 番地-->
                <input class="info-address" type="text" placeholder="〇-〇"><br>
                <span class="attention">※番地は半角英数で入力してください。</span><br>
    
                <!-- 送信ボタン -->
                <input class="info-check" type="submit" value="登録">
            </form>
        </section>
    </body>
</html>