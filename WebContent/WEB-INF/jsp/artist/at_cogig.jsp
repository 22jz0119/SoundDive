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
                    <li><a href="livehouse_home.html">HOME</a></li>
                    <li><a href="livehouse_mypage.html">MY PAGE</a></li>
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

    <div class="co-gig-group">
        <!-- ユニークなアーティストグループのリストを表示 -->
		<c:forEach var="artist" items="${artistGroups}">
		    <div class="co-gig-one">
		        <form action="<%= request.getContextPath() %>/At_Cogig" method="post">
		            <input type="hidden" name="action" value="apply">
		            <input type="hidden" name="applicationId" value="${artist.id}">
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

    <!-- 次へボタン -->
    <div class="next-button-container">
        <%
            Integer livehouseApplicationId = (Integer) session.getAttribute("livehouseApplicationId");
            String nextPageUrl = livehouseApplicationId != null 
                ? request.getContextPath() + "/At-livehouse_search?applicationId=" + livehouseApplicationId
                : request.getContextPath() + "/At-livehouse_search";
        %>
        <a href="<%= nextPageUrl %>" class="next-button">次へ</a>
    </div>
</body>
</html>
