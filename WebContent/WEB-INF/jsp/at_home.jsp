<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">

    <title>アーティストホーム画面</title>
</head>

<body class="artist_home">
    <header class="main-header">
        <div class="header-container">
            <div class="main-title">
                <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="" class="main-logo">
            </div>
            <nav class="header-nav">
                <ul class="header-nav-ul">
                    <li><a href="<%= request.getContextPath() %>/At_Mypage">MY PAGE</a></li>
                    <li><a href="#">000</a></li>
                    <li><a href="#">000</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <div><img src="<%= request.getContextPath() %>/assets/img/key-visual.jpg" alt=""></div>
        <div class="booking-title">
            <h2 class="booking-title-h2">Booking</h2>
        </div>
        <section class="booking-nav-section">
            <form action="<%= request.getContextPath() %>/At_Home" method="post">
			    <div class="booking-button">
			        <div class="booking-solo-button">
			            <!-- SOLO LIVE ボタン -->
			            <button type="submit" name="action" value="solo" class="solo-button">SOLO LIVE</button>
			        </div>
			        <div class="booking-multi-button">
			            <!-- MULTI LIVE ボタン -->
			            <button type="submit" name="action" value="multi" class="multi-button">MULTI LIVE</button>
			        </div>
			    </div>
			</form>

        </section>
        
        <c:forEach var="app" items="${applications}">
        	<div>
        		<c:choose>
	                 <c:when test="${not empty app.livehouse_information}">
	            		${app.livehouse_information.livehouse_name}
	                 </c:when>
	                 <c:otherwise>
	                    ライブハウス情報なし
	                 </c:otherwise>
         	 	</c:choose>
        		<div>
        			<ul>
        				<li>
        					<p>
        						<c:choose>
				                    <c:when test="${app.true_False == false}">
				                        予約申請待ち
				                    </c:when>
				                    <c:when test="${app.true_False == true}">
				                        予約完了
				                    </c:when>
				             	</c:choose>
				             </p>
				        </li>
        			</ul>
        			<ul>
        				<li>
        					<p>
        						<c:choose>
							        <c:when test="${not empty app.date_time}">
							            ${app.formattedDateTime}
							        </c:when>
							        <c:otherwise>
							            <!-- 空欄を表示 -->
							        </c:otherwise>
							    </c:choose>
        					</p>
        				</li>
        			</ul>
        		</div>
   				
        	</div>
        	
        </c:forEach>
        

<<<<<<< HEAD
        <c:if test="${not empty applications}">
	        <table border="1">
	            <tr>
	                <th>申請ID</th>
	                <th>ライブハウスID</th>
	                <th>申請日</th>
	                <th>開始時間</th>
	                <th>終了時間</th>
	                <th>コギグorソロ</th>
	                <th>アーティストグループID</th>
	            </tr>
	            <!-- applicationsリストをループして表示 -->
	            <c:forEach var="application" items="${applications}">
				    <tr>
				        <td>${application.id}</td>
				        <td>${application.livehouse_information_id}</td>
				        <td>${application.date_time != null ? application.date_time : '未設定'}</td>
				        <td>${application.start_time}</td>
				        <td>${application.finish_time}</td>
				        
				        <!-- true_falseの値に基づいて表示を変更 -->
				        <td>
				            <c:choose>
							    <c:when test="${application.isTrue_False() == false}">
							        予約申請待ち
							    </c:when>
							    <c:when test="${application.isTrue_False() == true}">
							        予約完了
							    </c:when>
							    <c:otherwise>
							        状態不明
							    </c:otherwise>
							</c:choose>
				        </td>
				
				        <td>${application.artist_group_id}</td>
				    </tr>
				</c:forEach>
	            
	            
	        </table>
    	</c:if>
=======
        <div class="main-calendar-button">
            <div class="calendar-next-button">
                <button id="prev" type="button">前の月</button>
            </div>
            <div class="calendar-back-button">
                <button id="next" type="button">次の月</button>
            </div>
        </div>
        
        <div class="home-calendar-div">
            <div id="calendar"></div>
        </div>
        
        <script src="<%= request.getContextPath() %>/assets/js/artist_home.js"></script>
        <script>
            // ボタンがクリックされたときにlivehouse_typeを設定
            function setLivehouseType(type) {
                document.getElementById("livehouse_type").value = type;
            }
        </script>
>>>>>>> branch 'main' of https://github.com/22jz0119/SoundDive.git
    </main>
    <c:if test="${not empty applications}">
            <table border="1">
                <thead>
                    <tr>
                        <th>申請ID</th>
                        <th>ライブハウス名</th>
                        <th>申請日</th>
                        <th>開始時間</th>
                        <th>終了時間</th>
                        <th>コギグ/ソロ</th>
                        <th>予約状態</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="app" items="${applications}">
                        <tr>
                            <td>${app.id}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty app.livehouse_information}">
                                        ${app.livehouse_information.livehouse_name}
                                    </c:when>
                                    <c:otherwise>
                                        ライブハウス情報なし
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${app.date_time != null ? app.date_time : '未設定'}</td>
                            <td>${app.start_time != null ? app.start_time : '未設定'}</td>
                            <td>${app.finish_time != null ? app.finish_time : '未設定'}</td>
                            <td>${app.cogig_or_solo == 1 ? 'ソロ' : 'コギグ'}</td>
                            <td>
                                <c:choose>
				                    <c:when test="${application.true_False == false}">
				                        予約申請待ち
				                    </c:when>
				                    <c:when test="${application.true_False == true}">
				                        予約完了
				                    </c:when>
				                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    
</body>
</html>
