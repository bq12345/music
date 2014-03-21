<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>音乐搜索</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Ubuntu+Mono'
	rel='stylesheet' type='text/css'>
<style type="text/css">
body {
	margin: 0 0 0 0;
	padding: 0 0 0 0;
	font-family: 'Ubuntu Mono', 微软雅黑, sans-serif;
	float: left;
}

h1 {
	opacity: 0.5;
}

input[type="text"],input[type="password"],input[type="datetime"],input[type="datetime-local"],input[type="date"],input[type="month"],input[type="time"],input[type="week"],input[type="number"],input[type="email"],input[type="url"],input[type="search"],input[type="tel"],input[type="color"],.uneditable-input
	{
	margin-bottom: 0px;
}

.list {
	margin-top: 50px;
	float: left;
}
</style>

<script type="text/javascript"
	src="http://lib.sinaapp.com/js/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#search").click(function() {
			$('#result').html('');
			search();
		});
		$("#navi").click(function() {
			var page = parseInt($("#page").val());
			$("#page").val(++page);
			search();
		});

		$("#text").keydown(function() {
			if (event.keyCode == "13") {//keyCode=13是回车键
				$('#search').click();
			}
		});
	});

	function urldecode(data) {
		return decodeURIComponent(data);
	}
	function show(id) {
		console.log(id);
		window.location.href="download?id="+id;
	}
	function search() {
		$
				.ajax({
					url : "search?song=" + $('#text').val() + "&page="
							+ $('#page').val(),
					type : "get",
					dataType : "json",
					success : function(data, status) {
						console.log(data);
						$("#list").css('display', 'block');
						if (data.total / 10 != 1) {
							$('#navi').html('<a class="btn btn-info">载入更多</a>');
						}
						$
								.each(
										data.results,
										function(i, item) {
											console
													.log(urldecode(item.song_name)
															+ ":"
															+ urldecode(item.artist_name));
											$(
													'<p style="lead">'
															+ '<h3><a class="btn btn-primary" onclick="show(\''
															+ item.song_id
															+ '\');">歌曲名称:'
															+ urldecode(item.song_name)
															+ '</a></h3>'
															+ '<h4>艺术家:'
															+ urldecode(item.artist_name)
															+ '  '
															+ '专辑:'
															+ urldecode(item.album_name)
															+ '</h4></p>')
													.appendTo('#result');
										});
					},
					error : function() {
						console.log("error");
						$('#list').css('display', 'none');
						$('#result').html('failed');
					}
				});
	}
</script>

</head>
<body>
	<header>
		<div class="container">

			<fieldset style="margin-left: 50px;margin-top: 50px">
				<h1>欢迎您使用白强的音乐搜索</h1>
				<input id="text" type="text" required="required" name="song"
					placeholder="输入歌曲名称" style="width: 50%;" /> <input type="hidden"
					id="page" value="1" />
				<button class="btn btn-primary btn-lg" id="search">搜索</button>
				<div id="list" class="list">
					<div id="result"></div>
					<div id="navi"></div>
				</div>
			</fieldset>
		</div>
	</header>
</body>
</html>
