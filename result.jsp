<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<c:if test="${dto.fileid != 0 }">
<h3>파일 업로드가 되었습니다.</h3>
파일번호 : ${dto.fileid} <br>
파일명 : ${dto.name } <br>
파일 사이즈 : ${dto.filesize }
업로드 된 파일이름 : ${dto.path }<br>
<img src="${dto.path }" alt="test">
</c:if>
<c:if test="${dto.fileid == 0 }">
파일을 선택하지 않았습니다. <br>
<span onclick="history.go(-1)" style="text-decoration: underline;">다시 선택</span>하세요 

</c:if>
</body>
</html>
