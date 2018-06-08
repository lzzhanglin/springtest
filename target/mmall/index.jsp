<html>
<body>
<h2>Hello World!</h2>
<div>
    <h3>springmvc上传文件</h3>
</div>
<form name="upload" action="/manage/product/upload.do" method="post" enctype="multidata/form-data">
<input type="file" name="upload_file"/>
<input type="submit" value="upload file"/>
</form>
<div>
    <h3>富文本</h3>
</div>
<form name="upload1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multidata/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="richtext_upload file"/>
</form>
</body>
</html>
