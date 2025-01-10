<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
    <title>対バン申請画面</title>
</head>
<body class="taiban">
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
                </ul>
            </nav>
        </div>
    </header>

    <section class="artist-co-gig-container">
        <div class="artist-co-gig-title">
            <h2 class="artist-co-gig-title-h2">対バンライブ申請</h2>
        </div>

        <!-- 成功・失敗メッセージの表示 -->
        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <div class="artist-search-form">
		    <form action="<%= request.getContextPath() %>/At_Cogig" method="get">
		        <div class="artist-search-box">
		            <input type="search" id="artist-search-textbox" name="q" 
		                   placeholder="アーティスト名を検索" value="${param.q}">
		        </div>
		        <div class="artist-search-button">
		            <button type="submit" class="artist-search-btn">検索</button>
		        </div>
		    </form>
		</div>
    </section>
    
    <div class="cogig-containar">
    	<c:forEach var="artist" items="${artistGroups}">
    		<form action="<%= request.getContextPath() %>/At_Cogig" method="post">
    			<input type="hidden" name="action" value="apply">
		            <input type="hidden" name="applicationId" value="${artist.id}">
		            
		            <%
		                // applicationIdをセッションから取得し、存在しない場合はリクエストから取得
		                String livehouseApplicationId = (String) session.getAttribute("livehouseApplicationId");
		                if (livehouseApplicationId == null) {
		                    livehouseApplicationId = request.getParameter("applicationId");
		                }
		            %>
		            <input type="hidden" name="livehouseApplicationId" value="<%= livehouseApplicationId %>">
    				
    				<div class="cogig-prof-frame">
    					<div class="cogig-prof-box">
    						<div class="cogig-prof-img-box">
    							<c:if test="${not empty artist.picture_image_movie}">
				                    <img src="${pageContext.request.contextPath}${artist.picture_image_movie}" alt="バンドのイラスト" class="band-image" width="100px" height="100px">
				                </c:if>
    						</div>
    						<div>
    							<ul class="cogig-prof-title">
		    						<li><p>${artist.account_name}</p></li>
		    					</ul>
		    					<ul class="cogig-prof-ul1">
		    						<li class="cogig-prof-ul1-li1"><p>バンド歴</p></li>
		    						<li class="cogig-prof-ul1-li2"><p>${artist.band_years} 年</p></li>
		    					</ul>
		    					<ul class="cogig-prof-ul2">
		    						<li class="cogig-prof-ul2-li1"><p>ジャンル</p></li>
		    						<li class="cogig-prof-ul2-li2"><p>${artist.group_genre}</p></li>
		    					</ul>
		    					<ul class="cogig-prof-button">
								  <li class="cogig-prof-buttton-li1">
								    <button type="submit" id="cogig-prof-join-btn">申請する</button>
								  </li>
								</ul> 
    						</div>
    					</div>
    					
    					
						   					
			                
    				</div>
    				
    				
    				
    		</form>
    		
    	</c:forEach>
    </div>
<%--  
    <div class="co-gig-group">
        <!-- ユニークなアーティストグループのリストを表示 -->
		<c:forEach var="artist" items="${artistGroups}">
		    <div class="co-gig-one">
		        <form action="<%= request.getContextPath() %>/At_Cogig" method="post">
		            <input type="hidden" name="action" value="apply">
		            <input type="hidden" name="applicationId" value="${artist.id}">

		            <%
		                // applicationIdをセッションから取得し、存在しない場合はリクエストから取得
		                String livehouseApplicationId = (String) session.getAttribute("livehouseApplicationId");
		                if (livehouseApplicationId == null) {
		                    livehouseApplicationId = request.getParameter("applicationId");
		                }
		            %>
		            <input type="hidden" name="livehouseApplicationId" value="<%= livehouseApplicationId %>">
		
		            <table class="taibantable">
		                <tr class="a">
		                    <th rowspan="4">
		                        <c:if test="${not empty artist.picture_image_movie}">
		                            <img src="${pageContext.request.contextPath}${artist.picture_image_movie}" 
		                                 alt="バンドのイラスト" class="band-image">
		                        </c:if>
		                    </th>
		                    <th>${artist.account_name}</th>
		                    <th class="b">ステータス</th>
		                </tr>
		                <tr class="a">
		                    <td class="b">${artist.account_name}</td>
		                    <td class="b">Active</td>
		                </tr>
		                <tr class="a">
		                    <td class="b">バンド歴: ${artist.band_years} 年</td>
		                    <td class="b">メンバー数: ${memberCounts[artist.id]}</td>
		                </tr>
		                <tr class="a">
		                    <td class="b">ジャンル: ${artist.group_genre}</td>
		                    <td class="b">
		                        <button type="submit">申請する</button>
		                    </td>
		                </tr>
		            </table>
		        </form>
		    </div>
		</c:forEach>
	</div>
--%>
    <!-- 次へボタン -->
    <div class="next-button-container">
        <%
		    String livehouseApplicationId = (String) session.getAttribute("livehouseApplicationId");
		    String livehouseType = request.getParameter("livehouse_type");
		    if (livehouseType == null) {
		        livehouseType = "multi";  // デフォルト値
		    }
		    String nextPageUrl = request.getContextPath() + "/At-livehouse_search";
		    if (livehouseApplicationId != null) {
		        nextPageUrl += "?applicationId=" + livehouseApplicationId + "&livehouse_type=" + livehouseType;
		    } else {
		        nextPageUrl += "?livehouse_type=" + livehouseType;
		    }
		%>
<a href="<%= nextPageUrl %>" class="co-gig-btn">次へ</a>
    </div>
</body>
</html>
