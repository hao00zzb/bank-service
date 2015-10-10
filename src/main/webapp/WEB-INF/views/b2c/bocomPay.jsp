<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%
request.setCharacterEncoding("GBK");
response.setCharacterEncoding("GBK");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>银行支付请求</title>
</head>
<body>
	<h1>页面跳转中，请稍候...</h1>
	<div style="display: none;">
		<c:if test="${not bocomB2CRequest.wap }">
			<form id="requestForm" action="${bocomB2CRequest.orderUrl }" method="post">
				<input type="hidden" name="interfaceVersion" value="${bocomB2CRequest.interfaceVersion }"/>
				<input type="hidden" name="merID" value="${bocomB2CRequest.merID }"/>
				<input type="hidden" name="orderid" value="${bocomB2CRequest.orderNumber }"/>
				<input type="hidden" name="orderDate" value="${bocomB2CRequest.orderDate }"/>
				<input type="hidden" name="orderTime" value="${bocomB2CRequest.orderTime }"/>
				<input type="hidden" name="tranType" value="${bocomB2CRequest.tranType }"/>
				<input type="hidden" name="amount" value="${bocomB2CRequest.amount }"/>
				<input type="hidden" name="curType" value="${bocomB2CRequest.curType }"/>
				<input type="hidden" name="orderContent" value="${bocomB2CRequest.orderContent }"/>
				<input type="hidden" name="orderMono" value="${bocomB2CRequest.orderMono }"/>
				<input type="hidden" name="phdFlag" value="${bocomB2CRequest.phdFlag }"/>
				<input type="hidden" name="notifyType" value="${bocomB2CRequest.notifyType }"/>
				<input type="hidden" name="merURL" value="${bocomB2CRequest.merURL }"/>
				<input type="hidden" name="goodsURL" value="${bocomB2CRequest.goodsURL }"/>
				<input type="hidden" name="jumpSeconds" value="${bocomB2CRequest.jumpSeconds }"/>
				<input type="hidden" name="payBatchNo" value="${bocomB2CRequest.payBatchNo }"/>
				<input type="hidden" name="proxyMerName" value="${bocomB2CRequest.proxyMerName }"/>
				<input type="hidden" name="proxyMerType" value="${bocomB2CRequest.proxyMerType }"/>
				<input type="hidden" name="proxyMerCredentials" value="${bocomB2CRequest.proxyMerCredentials }"/>
				<input type="hidden" name="netType" value="${bocomB2CRequest.netType }"/>
				<input type="hidden" name="merSignMsg" value="${bocomB2CRequest.merSignMsg }"/>
				<input type="hidden" name="issBankNo" value=""/>
			</form>
		</c:if>
		<c:if test="${bocomB2CRequest.wap }">
			<form id="requestForm" action="${bocomB2CRequest.orderUrl }" method="post">
				<input type="hidden" name="merchNo" value="${bocomB2CRequest.merID }"/>
				<input type="hidden" name="orderNo" value="${bocomB2CRequest.orderNumber }"/>
				<input type="hidden" name="orderDate" value="${bocomB2CRequest.orderDate }"/><br/>
			</form>
		</c:if>
	</div>
	<script type="text/javascript" src="${path }/assets/scripts/jquery-1.8.0.min.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#requestForm').submit();
		});
	</script>
</body>
</html>