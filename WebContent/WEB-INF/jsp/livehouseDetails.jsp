<% Livehouse_information livehouseInfo = (Livehouse_information) request.getAttribute("livehouseInfo"); %>
<html>
<body>
    <h1>ライブハウス情報詳細</h1>
    <p>オーナー名: <%= livehouseInfo.getOner_name() %></p>
    <p>機材情報: <%= livehouseInfo.getEquipment_information() %></p>
    <p>ライブハウス説明情報: <%= livehouseInfo.getLivehouse_explanation_information() %></p>
    <p>ライブハウス詳細情報: <%= livehouseInfo.getLivehouse_detailed_information() %></p>
    <p>ライブハウス名: <%= livehouseInfo.getLivehouse_name() %></p>
    <p>住所: <%= livehouseInfo.getLive_address() %></p>
    <p>電話番号: <%= livehouseInfo.getLive_tel_number() %></p>
    <p>作成日時: <%= livehouseInfo.getCreateDate() %></p>
    <p>更新日時: <%= livehouseInfo.getUpdateDate() %></p>
    <a href="index.jsp">戻る</a>
</body>
</html>
