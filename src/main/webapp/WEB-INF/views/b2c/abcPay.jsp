<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>银行支付请求</title>
</head>
<body>
	<c:if test='${abcB2CRequest.returnCode ne "0000" }'>
	<table style="width: 100%;">
		<tr>
			<td width="100px;">错误编码：</td>
			<td><c:out value="${abcB2CRequest.returnCode }" /> </td>
		</tr>
		<tr>
			<td width="100px;">错误信息：</td>
			<td><c:out value="${abcB2CRequest.errorMessage }" /> </td>
		</tr>
	</table>
	</c:if>
	<c:if test='${abcB2CRequest.returnCode eq "0000" }'>
		<h1>页面跳转中，请稍候...</h1>
		<form id="requestForm" action="${abcB2CRequest.paymentUrl }" method="post">
		</form>
		<script type="text/javascript" src="${path }/assets/scripts/jquery-1.8.0.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$('#requestForm').submit();
			});
		</script>
	</c:if>
</body>
</html>