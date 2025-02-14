<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
<title>承認履歴</title>
</head>
<body>

	<!-- ヘッダー -->
	<header class="main-header">
		<div class="header-container">
			<div class="main-title">
				<h1 class="main-title-h1">Sound Dive</h1>
			</div>
			<nav class="header-nav">
				<ul class="header-nav-ul">
					<li><a href="<%=request.getContextPath()%>/Livehouse_home">HOME</a></li>
					<li><a href="<%=request.getContextPath()%>/Livehouse_mypage">MY
							PAGE</a></li>
					<li class="header-box-li2"><a href="#"
						onclick="logoutAndRedirect();" class="top-logout-btn">LOG OUT</a></li>
				</ul>
			</nav>
		</div>
	</header>

	<main>
		<!-- 予約承認履歴セクション -->
		<section class="application-list-count">
			<h2>予約承認履歴一覧ページ</h2>
			<div class="reservation-date">
				<p>
					日付:
					<c:out value="${param.date}" />
				</p>
				<p>
					総件数:
					<c:out
						value="${fn:length(cogigApplications) + fn:length(soloApplications)}"
						default="0" />
					件
				</p>
			</div>
		</section>

		<!-- ソロのデータ表示 -->
		<div class="application-lists">
			<div class="application-list-solo">
				<c:choose>
					<c:when test="${not empty soloApplications}">
						<c:forEach var="application" items="${soloApplications}">
							<li>
								<div class="application-artist-list-img-containar">
									<c:set var="groupId" value="${application.groupId}" />
									<c:set var="imagePath" value="${pictureImageMap[groupId]}" />
									<img src="${pageContext.request.contextPath}${imagePath}"
										alt="バンドのイラスト" class="application-artist-list-ikon"
										style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
								</div>
								<div class="application-list-solo">
									<ul class="application-artist-list-ul0">
										<li><p>${application.accountName}(ソロ)</p></li>
									</ul>
									<ul class="application-artist-list-ul1">
										<li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
										<li class="application-artist-list-ul1-li2"><p>${application.groupGenre}</p></li>
									</ul>
									<ul class="application-artist-list-ul2">
										<li class="application-artist-list-ul2-li1"><p>活動歴</p></li>
										<li class="application-artist-list-ul2-li2"><p>${application.bandYears}年</p></li>
									</ul>
									<ul class="application-artist-list-ul3">
										<li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
										<li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
									</ul>
									<ul class="application-artist-list-ul4">
										<li class="application-artist-list-ul4-li1"><audio
												class="sound-source" controls src="water.mp3"
												type="audio/mp3">音源
											</audio></li>
									</ul>
									<ul class="application-artist-list-ul5">
										<li class="application-artist-list-ul5-li1"><a
											href="<c:url value='/Livehouse_history_detail' />?id=${application.id}"
											class="application-artist-list-ul5-li1-a">詳細を見る</a></li>
									</ul>
									<ul class="application-artist-list-ul5">
										<li>
											<form
												action="${pageContext.request.contextPath}/Approval_history"
												method="post" onsubmit="return confirm('本当に削除しますか？');">
												<input type="hidden" name="applicationId"
													value="${application.id}">
												<button type="submit" class="delete-button">削除する</button>
											</form>
										</li>
									</ul>
								</div>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li>ソロデータがありません</li>
					</c:otherwise>
				</c:choose>

			</div>




			<!-- 対バンのデータ表示 -->
			<p>対バン</p>
			<div class="application-list-multi">
				<c:choose>
					<c:when test="${not empty cogigApplications}">
						<c:set var="previousReservationId" value="-1" />
						<c:forEach var="application" items="${cogigApplications}">
							<div class="apllicationList-SubFrame">
								<c:if test="${previousReservationId ne application.id}">
									<!-- 対バンデータの開始 -->
									<ul class="cogig-group">
								</c:if>
								<li>
									<div class="application-artist-list-img-containar">
										<c:set var="groupId" value="${application.groupId}" />
										<c:set var="imagePath" value="${pictureImageMap[groupId]}" />
										<img src="${pageContext.request.contextPath}${imagePath}"
											alt="バンドのイラスト"
											style="width: 150px; height: auto; max-height: 150px; object-fit: cover; border-radius: 10px;">
									</div>
									<div>
										<ul class="application-artist-list-ul0">
											<li class="application-artist-list-ul0-li1"><p>${application.accountName}</p></li>
										</ul>
										<ul class="application-artist-list-ul1">
											<li class="application-artist-list-ul1-li1"><p>ジャンル</p></li>
											<li class="application-artist-list-ul1-li2"><p>${application.groupGenre}</p></li>
										</ul>
										<ul class="application-artist-list-ul2">
											<li class="application-artist-list-ul2-li1"><p>バンド歴</p></li>
											<li class="application-artist-list-ul2-li2"><p>${application.bandYears}年</p></li>
										</ul>
										<ul class="application-artist-list-ul3">
											<li class="application-artist-list-ul3-li1"><p>レーティング</p></li>
											<li class="application-artist-list-ul3-li2"><p>評価3.5</p></li>
										</ul>
										<ul class="application-artist-list-ul4">
											<li class="application-artist-list-ul4-li1"><audio
													class="sound-source" controls src="water.mp3"
													type="audio/mp3">音源
												</audio></li>
										</ul>
										<c:if
											test="${previousReservationId ne application.id or fn:length(cogigApplications) eq 1}">
											<ul class="application-artist-list-ul5">
												<li class="application-artist-list-ul5-li1"><a
													href="<c:url value='/Livehouse_history_detail' />?id=${application.id}"
													class="application-artist-list-ul5-li1-a">詳細を見る</a></li>
											</ul>
										</c:if>
										<!-- 削除ボタン追加 -->
										<ul class="application-artist-list-ul5">
											<li>
												<form
													action="${pageContext.request.contextPath}/Approval_history"
													method="post" onsubmit="return confirm('本当に削除しますか？');">
													<input type="hidden" name="applicationId"
														value="${application.id}">
													<button type="submit" class="delete-button">削除する</button>
												</form>
											</li>
										</ul>
									</div>
								</li>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li>データがありません</li>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</main>

</body>
</html>
