<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>银行支付请求</title>
</head>
<body>
	<h1>
		Hello world!  
	</h1>
	<P>  The time on the server is ${serverTime}. </P>
	<hr />
	<span>提示信息：<span id="message"></span></span>
	<form action="${path }/pay" method="post" id="detailForm">
		<input type="hidden" name="sign" value="">
		<div style="margin: 5px">
			<table style="width: 100%;border-collapse: 0;border-spacing: 0;">
				<tr>
					<td style="width: 120px;text-align: right;">选择银行：</td>
					<td>
						<input type="radio" name="bankType" id="abcBank" value="abc" checked="checked">
						<label for="abcBank">农业银行</label>
						<input type="radio" name="bankType" id="bocomBank" value="bocom">
						<label for="bocomBank">交通银行</label>
					</td>
				</tr>
				<tr>
					<td style="width: 140px;text-align: right;">是否为WAP支付：</td>
					<td>
						<input type="radio" name="iswap" id="wapYse" value="1" checked="checked">
						<label for="wapYse">是</label>
						<input type="radio" name="iswap" id="wapNo" value="0">
						<label for="wapNo">否</label>
					</td>
				</tr>
				<tr>
					<td style="width: 120px;text-align: right;">订单号：</td>
					<td>
						<input type="text" name="orderNumber" style="width: 95%;" value="${orderNumber }">
					</td>
				</tr>
				<tr>
					<td style="width: 120px;text-align: right;">金额：</td>
					<td>
						<input type="text" name="amount" style="width: 95%;" value="0.01">
					</td>
				</tr>
			</table>
		</div>
		<div style="margin: 0 5px;text-align: center;">
			<div>
				<button style="width: 120px;" type="button" id="makeorder" onclick="makeOrder();">生成订单</button>
				<button style="width: 120px;" type="submit" id="payment">提交支付</button>
				<button style="width: 120px;" type="reset">重置</button>
			</div>
		</div>
	</form>
	
	<hr/>
	<h1>人工对账</h1>
	<form action="javascript:;" method="post" id="detailAccountForm">
		<div style="margin: 5px">
			<table style="width: 100%;border-collapse: 0;border-spacing: 0;">
				<tr>
					<td style="width: 120px;text-align: right;">选择银行：</td>
					<td>
						<input type="radio" name="bankType" id="ctr-abc-bank" value="abc" checked="checked">
						<label for="ctr-abc-bank">农业银行</label>
						<input type="radio" name="bankType" id="ctr-bocom-bank" value="bocom">
						<label for="ctr-bocom-bank">交通银行</label>
					</td>
				</tr>
				<tr>
					<td style="width: 140px;text-align: right;">查询日期：</td>
					<td>
						<input type="text" name="orderDate" style="width: 95%;" value="${orderDate }">
					</td>
				</tr>
			</table>
		</div>
		<div style="margin: 0 5px;text-align: center;">
			<div>
				<button style="width: 120px;" type="submit" id="query">查询</button>
				<button style="width: 120px;" type="reset">重置</button>
			</div>
		</div>
	</form>
	
	<script type="text/javascript" src="${path }/assets/scripts/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="${path }/assets/scripts/jquery.md5.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#payment').prop('disabled', true);
		});
		
		function makeOrder() {
			$('#message').html('订单生成中，请稍后.....');
			$('#payment').prop('disabled', true);
			var url = '${path}/order/make';
			var data = {};
			data.bankType = $('input[name="bankType"]:checked').val();
			data.iswap = $('input[name="iswap"]:checked').val();
			data.orderNumber = $('input[name="orderNumber"]').val();
			data.totalMoney = $('input[name="amount"]').val();
			
			$.post(url, data, function(json){
				if (!json.success) {
					$('#message').html(json.msg);
					return;
				}
				$('#message').html('订单生成成功!');
				$('#payment').prop('disabled', false);
				$('#makeorder').prop('disabled', true);
				
				var str = data.bankType + data.orderNumber + data.totalMoney + 'csey0512';
				$('input[name="sign"]').val($.md5(str));
				
			}, 'json');
		}
	</script>

</body>
</html>
