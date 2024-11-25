<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ライブハウス詳細</title>
</head>
<body>
<h1>ライブハウス詳細</h1>

<%
    Livehouse_information livehouse = (Livehouse_information) request.getAttribute("livehouse");
%>

<table>
    <tr>
        <th>オーナー名</th>
        <td><%= livehouse.getOner_name() %></td>
    </tr>
    <tr>
        <th>機材情報</th>
        <td><%= livehouse.getEquipment_information() %></td>
    </tr>
    <tr>
        <th>ライブハウス説明情報</th>
        <td><%= livehouse.getLivehouse_explanation_information() %></td>
    </tr>
    <tr>
        <th>ライブハウス詳細情報</th>
        <td><%= livehouse.getLivehouse_detailed_information() %></td>
    </tr>
    <tr>
        <th>ライブハウス名</th>
        <td><%= livehouse.getLivehouse_name() %></td>
    </tr>
    <tr>
        <th>住所</th>
        <td><%= livehouse.getLive_address() %></td>
    </tr>
    <tr>
        <th>電話番号</th>
        <td><%= livehouse.getLive_tel_number() %></td>
    </tr>
    <tr>
        <th>作成日時</th>
        <td><%= livehouse.getCreateDate() %></td>
    </tr>
    <tr>
        <th>更新日時</th>
        <td><%= livehouse.getUpdateDate() %></td>
    </tr>
</table>

<a href="index.jsp">戻る</a>

</body>
</html>
