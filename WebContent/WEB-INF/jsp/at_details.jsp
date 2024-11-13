<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
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
                    <li><a href="<%= request.getContextPath() %>/artist_home.jsp">HOME</a></li>
                    <li><a href="<%= request.getContextPath() %>/artist_mypage.jsp">MY PAGE</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="artist-livehouse-details-main">
        <section class="artist-livehouse-detail-section">
            <div class="a-t-detail-title">
                <h1 class="a-t-detail-h1">ライブハウス詳細画面</h1>
            </div>
            <div class="A-t-detail-livehousename">
                <p class="artist-livehouse-detail-oner">${livehouseName}</p>
            </div>
            <div>
                <div class="A-t-l-detail-img-div">
                    <img src="<%= request.getContextPath() %>/assets/img/Studio.jpg" alt="" class="artist_livehouse_details-img">
                </div>
                <div class="a-t-detail-description-div">
                    <ul class="A-t-discription-ul">
                        <li class="a-t-detail-onername-title a-t-detail-1"><p>オーナー</p></li>
                        <li class="a-t-detail-onername a-t-detail-2"><p>${ownerName}</p></li>
                        <li class="a-t-detail-address-title a-t-detail-1"><p>住所</p></li>
                        <li class="a-t-detail-address a-t-detail-2"><p>${address}</p></li>
                        <li class="a-t-detail-tell-title a-t-detail-1"><p>電話番号</p></li>
                        <li class="a-t-detail-tell a-t-detail-2"><p>${phone}</p></li>
                        <li class="a-t-detail-explanation-title a-t-detail-1"><p>ライブハウス説明情報</p></li>
                        <li class="a-t-detail-explanation a-t-detail-2"><p>${description}</p></li>
                    </ul>
                </div>
            </div>
        </section>
    </main>
</body>
</html>
