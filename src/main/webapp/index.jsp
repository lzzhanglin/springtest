<html>
<meta charset='utf-8'>
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
