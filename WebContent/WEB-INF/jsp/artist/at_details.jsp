<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>ライブハウス詳細画面</title>
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

    <main class="artist-livehouse-details-main">
        <section class="artist-livehouse-detail-section">

            <div class="A-t-detail-livehousename">
                <p class="artist-livehouse-detail-oner">赤羽ReNY alpha</p>
            </div>
            <div class="a-t-livehouse-detail-containar">
                <div class="a-t-livehouse-detail-img-div">
                    <img src="../assets/img/Studio.jpg" alt="" class="artist_livehouse_details-img1">
                </div>
                
                <div class="a-t-detail-description-div1">
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-onername-title a-t-detail-1"><p>オーナー</p></li>
                        <li class="a-t-detail-onername a-t-detail-2"><p>佐藤正孝</p></li>
                    </ul>
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-address-title a-t-detail-1"><p>住所</p></li>
                        <li class="a-t-detail-address a-t-detail-2"><p>東京都新宿区歌舞伎町2-31-2</p></li>
                    </ul>
                    <ul class="A-t-discription-ul2">
                        <li class="a-t-detail-tell-title a-t-detail-1"><p>電話番号</p></li>
                        <li class="a-t-detail-tell a-t-detail-2"><p>012-3456-7890</p></li>
                    </ul>
                </div>
                <div class="a-t-detail-description-div2">
                    <ul class="A-t-discription-ul3">
                        <li class="a-t-detail-explanation-title a-t-detail-1"><p>ライブハウス説明情報</p></li>
                        <li class="a-t-detail-explanation a-t-detail-2"><p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Expedita, eaque itaque in eveniet ipsam ex ipsum reiciendis minus quos. Quasi omnis exercitationem amet molestias quaerat et nemo cupiditate officia magnam. Perspiciatis voluptatibus quidem eligendi placeat sapiente sequi libero officiis doloribus. Totam saepe ipsam qui commodi quae enim rem optio dignissimos?</p></li>
                    </ul> 
                    <ul class="A-t-discription-ul3">   
                        <li class="a-t-detail-description-title a-t-detail-1"><p>ライブハウス詳細情報</p></li>
                        <li class="a-t-detail-description a-t-detail-2"><p>Lorem ipsum dolor, sit amet consectetur adipisicing elit. Eligendi inventore, harum porro dicta ipsum, ut sint beatae fuga voluptatum facere at sapiente, quasi eius explicabo et suscipit voluptate nesciunt voluptatem veniam officiis repellendus. A saepe aliquam eveniet, consequatur libero, nesciunt illo totam ad recusandae, voluptas tempora hic laborum animi maxime?</p></li>
                    </ul>
                </div>

            </div>
            <div class="artist_livehouse_details-Discription">
                
            </div>
        </section>
        <div>
            <h2 class="OpenSpots-Reserve">空き状況・予約</h2>
            <p class="OpenSpots-Reserve-detile">空いてる日にちを選択して、予約に進んでください</p>
            <p class="Notes-or-Cautions">※誰も予約していない〇
                ※確定していないが予約者多数△</p>
        </div>

        <div class="a-t-detail-calendar-containar">
            <button id="prev-month" type="button">前の月</button>
            <button id="next-month" type="button">次の月</button>
        </div>
        <div id="calendar-container"></div>
    </main>

    <script src="../assets/js/A-t-Description.js"></script> <!-- JavaScriptファイルをリンク -->
    <a href="./artist_livehouse_reservation.html" class="button">次へ</a>
</body>
</html>