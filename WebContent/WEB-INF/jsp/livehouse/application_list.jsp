<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../assets/css/style.css">
        <title>アーティスト申請一覧ページ</title>
    </head>
    <body>
        <!--ヘッダー-->
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
            <!--予約日付-->
            <section class="application-list-count">
                <div class="main-application_list">
                    <h2 class="application_list_h2">アーティスト申請一覧画面</h2>
                </div>
                <div class="reservation-date">
                    <p class="application-date">日付 2024/12/24</p>
                    <p class="application-number">件数〇件</p>
                </div>
            </section>
            <!--申請リスト-->
            <section class="application-artist-list-containar">
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                <div class="application-artist-list-main">
                    <div class="application-artist-list-img-containar">
                        <img src="../assets/img/アーティスト画像.png" alt="" class="application-artist-list-ikon">
                    </div>
                    <div class="application-artist-list-frame">
                        
                        <ul class="application-artist-list-ul0">
                            <li><p>つきみ</p></li>
                        </ul>
                        <ul class="application-artist-list-ul1">
                            <li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
                            <li class="application-artist-list-ul1-li2"><p>テクノバンド</p></li>
                        </ul>
                        <ul class="application-artist-list-ul2">
                            <li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
                            <li class="application-artist-list-ul2-li2"><p>3年目</p></li>
                        </ul>
                        <ul class="application-artist-list-ul3">
                            <li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
                            <li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
                        </ul>
                        <ul class="application-artist-list-ul4">
                            <li class="application-artist-list-ul4-li1"><audio class="sound-source" controls src="water.mp3" type="audio/mp3">とまとまん</audio></li>
                        </ul>
                        <ul class="application-artist-list-ul5">
                            <li class="application-artist-list-ul5-li1"><a href="" class="application-artist-list-ul5-li1-a">対バンを申し込む</a></li>
                        </ul>
                    </div>
                </div>
                
               
                
            </section>
            
        </main>
    </body>
</html>