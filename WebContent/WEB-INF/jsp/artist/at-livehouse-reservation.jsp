<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ライブハウス予約時間確定画面</title>
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body>
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <h1 class="main-title-h1">Sound Dive</h1>
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="artist_home.html">HOME</a></li>
                    <li><a href="artist_mypage.html">MY PAGE</a></li>
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
        <p class="timeselection">2024年10月08日 (火)</p>
    
        <!-- 時間選択フォーム -->
        <form action="/submit-time" method="post" class="timeschedule">
            <label for="time">開始時間を選択してください:</label>
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
        </form>
    
        <!-- ライブハウス情報 -->
        <div class="A-t-detail-livehousename">
            <p class="artist-livehouse-detail-oner">赤羽ReNY alpha</p>
        </div>
    
        <!-- ライブハウス詳細と説明 -->
        <div class="artist_livehouse_details-Discription">
            <!-- ライブハウスの画像 -->
            <div>
                <img src="../assets/img/Studio.jpg" alt="" class="artist_livehouse_details-img">
            </div>
    
            <!-- ライブハウスの詳細リスト -->
            <div>
                <ul class="A-t-discription-ul">
                    <li><p>オーナー</p></li>
                    <li><p>住所</p></li>
                    <li><p>東京都新宿区歌舞伎町2-31-2</p></li>
                    <li><p>電話番号</p></li>
                    <li><p>ライブハウス説明情報</p></li>
                    <li>
                        <p>
                            Lorem ipsum dolor sit amet consectetur adipisicing elit. Expedita, eaque itaque in eveniet ipsam ex ipsum reiciendis minus quos. Quasi omnis exercitationem amet molestias quaerat et nemo cupiditate officia magnam. Perspiciatis voluptatibus quidem eligendi placeat sapiente sequi libero officiis doloribus. Totam saepe ipsam qui commodi quae enim rem optio dignissimos?
                        </p>
                    </li>
                    <li><p>ライブハウス詳細情報</p></li>
                    <li>
                        <p>
                            Lorem ipsum dolor, sit amet consectetur adipisicing elit. Eligendi inventore, harum porro dicta ipsum, ut sint beatae fuga voluptatum facere at sapiente, quasi eius explicabo et suscipit voluptate nesciunt voluptatem veniam officiis repellendus. A saepe aliquam eveniet, consequatur libero, nesciunt illo totam ad recusandae, voluptas tempora hic laborum animi maxime?
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    </section>
    
    <h2 class="Currentlyapplying">申請中アーティスト</h2>
<div class="Applicationgroup">
    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">つきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>
    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>

    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>

    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>

    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>

    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>

    <div class="Applicationtable">
        <img src="../assets/img/Group 5.png" alt="バンドのイラスト" class="band-image">
        <div class="artist-info">
            <p class="artist-name">toきみ</p>
            <p class="application-status">申請中</p>
        </div>
    </div>
</div>

<div class="form-rv">
    <input type="submit" value="確定" class="rv-btn">
</div>

<a href="../aritist/artist_livehouse_reservation_completed.html">仮の遷移ボタン</a>

</body>
</html>