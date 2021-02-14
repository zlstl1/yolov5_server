<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Test</title>
</head>

<div>Road</div>
<form method="post" action="${pageContext.request.contextPath}/road" enctype="multipart/form-data">
	<input type="file" id="image" name="image">
	<input type="submit">
</form>

<div>Sign</div>
<form method="post" action="${pageContext.request.contextPath}/sign" enctype="multipart/form-data">
	<input type="file" id="image" name="image">
	<input type="submit">
</form>

<script>
</script>
</body>
</html>
 

