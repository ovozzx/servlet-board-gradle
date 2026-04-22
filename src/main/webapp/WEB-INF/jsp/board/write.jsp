<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/write.css">
</head>
<body>
	<form action="${pageContext.request.contextPath}/board/write" method="post" enctype="multipart/form-data">
		<div class="form-row">
			<select name="categoryId">
				<c:forEach var="category" items="${categoryList}">
					<option value="${category.categoryId}"> ${category.categoryName}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-row">
			<label for="create-user">작성자</label>
			<input type="text" id="create-user" name="createUser" />
		</div>
		<div class="form-row">
			<label for="password">비밀번호</label>
			<input type="password" id="password" name="password" />
			<label for="password">비밀번호 확인</label>
			<input type="password" name="password-confirm" />
		</div>
		<div class="form-row">
			<label for="title">제목</label>
			<input type="text" id="title" name="title" />
		</div>
		<div class="form-row">
			<label for="content">내용</label>
			<textarea type="text" id="content" name="content" placeholder="내을 입력해 주세요."></textarea>
		</div>
		<div class="form-row">
			<label for="attachment">파일 첨부</label>
			<div class="form-column">
				<input type="file" class="attachment" name="attachment-one" placeholder="내을 입력해 주세요." />
				<input type="file" class="attachment" name="attachment-two" placeholder="내을 입력해 주세요." />
				<input type="file" class="attachment" name="attachment-three" placeholder="내을 입력해 주세요." />
			</div>

		</div>
		<div class="button-group">
			<a class="move-list" href="${pageContext.request.contextPath}/board/list">취소</a>
			<button type="submit">등록</button>
		</div>
	</form>
</body>
</html>