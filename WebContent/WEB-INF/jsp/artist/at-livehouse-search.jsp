<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>ライブハウス検索画面</title>
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
    <main class="artist-result-main">
        <section class="livehouse-search-container">
            <div class="livehouse-search-title">
                <h2 class="livehouse-search-title-h2">ライブハウス検索画面</h2>
            </div>
            <div class="livehouse-search-form">
                <form action="At_livehouse_search" method="post">
                    <div class="livehouse-search-box">
                        <input type="search" id="livehouse-search-textbox" name="q">
                    </div>
                    <div class="livehouse-search-button">
                        <button type="submit" class="livehouse-search-btn">検索</button>
                    </div>
                </form>
            </div>
        </section>

        <section class="artist-result-container">
        	<div class="artist-result-container-div">
	            <c:forEach var="livehouse" items="${livehouseList}">
	                <div class="artist-result-box">
	                    <div class="artist-result-LiveHouseName1">
	                        <h3 class="artist-result-LiveHouseName2">${livehouse.livehouse_name}</h3>
	                    </div>
	                    <div class="artist-result-Div3">
	                        <div>
	                            <img src="../assets/img/Studio.jpg" alt="StudioImg" class="artist-result-img">
	                        </div>
	                        <ul class="artist-result-frame">
	                            <li class="LiveHousemain-img"></li>
	                            <li class="LiveHouseAddress"><p>住所</p></li>
	                            <li class="LivehouseAddress-main"><p>${livehouse.live_address}</p></li>
	                            <li class="LivehouseCapa"><p>キャパ</p></li>
	                            <li class="LivehouseCapa-main"><p>100人</p></li>
	                            <li><p>${livehouse.livehouse_explanation_information}</p></li>
	                            <li class="artist-search-decision">
	                                <!-- ソロとマルチの分岐 -->
	                                <c:choose>
	                                    <c:when test="${param.livehouse_type == 'solo'}">
	                                        <!-- ソロライブの場合 -->
	                                        <a href="<c:url value='/At_details'>
	                                            <c:param name='livehouseId' value='${livehouse.id}' />
	                                            <c:param name='livehouse_type' value='solo'/>
	                                        </c:url>" class="artist-result-decision-button">詳細・予約</a>
	                                    </c:when>
	                                    <c:when test="${param.livehouse_type == 'multi'}">
	                                        <!-- マルチライブの場合 -->
	                                        <a href="<c:url value='/At_details'>
	                                            <c:param name='userId' value='${userId}' />
	                                            <c:param name='livehouseId' value='${livehouse.id}' />
	                                            <c:param name='applicationId' value='${applicationId}' />
	                                            <c:param name='livehouse_type' value='multi'/>
	                                        </c:url>" class="artist-result-decision-button">詳細・予約</a>
	                                    </c:when>
	                                </c:choose>
	                            </li>
	                        </ul>
	                    </div>
	                </div>
	            </c:forEach>
	    	</div>
        </section>
    </main>
    
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
