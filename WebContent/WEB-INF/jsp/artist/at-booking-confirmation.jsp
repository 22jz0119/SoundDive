<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>ライブハウス予約確認ページ</title>
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

    <main class="A-t-Booking-Confirmation-main">
        <section class="a-t-reservation-confirmation">
            <div class="a-t-confirmation-header">
                <h1 class="a-t-confirmation-title">予約確認</h1>
            </div>
            
            <h2 class="a-t-details-title">予約詳細</h2>
            <div class="a-t-reservation-details">
                
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1">
                        <p class="a-t-info-label">予約名義</p>
                        
                    </li>
                    <li class="a-t-reservation-info-item2">
                        <p class="a-t-info-value" id="reservation-name">佐藤正夫</p>
                    </li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">希望時間</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">17:50</p></li>
                </ul>
                <ul class="a-t-reservation-info-list">
                    <li class="a-t-reservation-info-item1"><p class="a-t-info-label">電話番号</p></li>
                    <li class="a-t-reservation-info-item2"><p class="a-t-info-value">000-0000-0000</p></li>
                </ul>
                
            </div>
            <div>
                <h2>予約アーティスト</h2>
            <div class="a-t-booking-gourp-main">
                
                <div class="a-t-booking-group-containar">
                    <ul class="a-t-booking-group-frame">
                        <li><img src="../assets/img/guiter.png" alt="" id="a-t-booking-group-img"></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>アーティスト名</p></li>
                        <li class="a-t-booking-group-li2"> <p>つきみ</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>バンド歴</p></li>
                        <li class="a-t-booking-group-li2"><p>つきみ</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>人数</p></li>
                        <li class="a-t-booking-group-li2"><p>３人</p></li>
                    </ul>
                    <ul class="a-t-booking-group-frame">
                        <li class="a-t-booking-group-li1"><p>ジャンル</p></li>
                        <li class="a-t-booking-group-li2"><p>ロック</p></li>
                    </ul>
                </div>

            </div>

            <h2 class="a-t-booking-place-title">予約場所</h2>
            
            <div class="a-t-booking-place-main-div">
                
                <div class="a-t-booking-place-containar1">
                    <img src="../assets/img/Studio.jpg" alt="" class="a-t-venue-image">
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>オーナー</p></li>
                        <li class="a-t-booking-place1-li2"><p>佐藤正夫</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>アクセス</p></li>
                        <li class="a-t-booking-place1-li2"><p>東京都小平市花小金井</p></li>
                    </ul>
                    <ul class="a-t-booking-place-ul1">
                        <li class="a-t-booking-place1-li1"><p>キャパ</p></li>
                        <li class="a-t-booking-place1-li2"><p>100人</p></li>
                    </ul>
                </div>
                <div class="a-t-booking-place-containar2">
                    <ul class="a-t-booking-place-ul2">
                        <li class="a-t-booking-place1-li1"><p>説明</p></li>
                        <li class="a-t-booking-place1-li2"><p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Repellat error nihil vero deserunt ratione quas libero? Laboriosam exercitationem consequuntur esse sunt. Laborum in eum voluptas, doloribus ducimus accusantium consectetur qui necessitatibus sapiente harum dolor dicta quo eveniet distinctio doloremque beatae molestias. Eligendi animi libero minus, quae obcaecati deserunt incidunt quibusdam perspiciatis doloribus esse voluptate est beatae, porro nisi eveniet dolor deleniti voluptatem reprehenderit eius impedit et tempora explicabo repudiandae? Quidem exercitationem debitis, laboriosam aliquid reiciendis illum totam recusandae perferendis amet esse dignissimos modi repellat vero nostrum id quam! Quasi placeat iure vero corrupti quae culpa eligendi voluptates est nulla rem!</p></li>
                    </ul>
                </div>
            </div>
           

            
        
            <div class="a-t-payment-info-container">
                <h2 class="a-t-payment-title">申請が通った際の前払い金額</h2>
                <p class="a-t-payment-amount" id="advance-payment">48000円</p>
            </div>
        </section>
    </main>

   
    


</body>
</html>