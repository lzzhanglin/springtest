<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/6/22
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Hello World!</h2>
<div>
    <h3>springmvc_upload_file</h3>
</div>
<%--enctype="multidata/form-data"--%>
<form name="upload" action="/manage/product/upload.do" method="post" enctype="multipart/form-data" >
    <input type="file" name="upload_file"/>
    <input type="submit" value="upload file"/>
</form>
<div>
    <h3>富文本</h3>
</div>
<form name="upload1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="richtext_upload file"/>
</form>
</body>
</html>
