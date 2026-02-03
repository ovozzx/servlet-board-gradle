<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modify.css">
</head>
<body>
    <h1>게시판 - 수정</h1>
    <form action="${pageContext.request.contextPath}/board/modify" method="post">
		<input type="hidden" name="boardId" value="${board.boardId}">
		<div class="form-row">
			<label for="category">카테고리</label>
			<input type="text" id="category" name="categoryName" value="${board.categoryName}" readonly/>
		</div>
		<div class="form-row">
			<label for="category">등록 일시</label>
			<input type="text" id="create-date" name="createDate" value="${board.createDate}" readonly/>
		</div>
		<div class="form-row">
			<label for="category">수정 일시</label>
			<input type="text" id="modify-date" name="modifyDate" value="${board.modifyDate}" readonly/>
		</div>
		<div class="form-row">
			<label for="category">조회수</label>
			<input type="text" id="view-count" name="viewCount" value="${board.viewCount}" readonly/>
		</div>
		<div class="form-row">
			<label for="create-user">작성자</label>
	    	<input type="text" id="create-user" name="createUser" value="${board.createUser}"></input>
		</div>
		<div class="form-row">
			<label for="password">비밀번호</label>
	    	<input type="password" id="password" name="password" value=""></input>
		</div>
		<div class="form-row">
			<label for="title">제목</label>
	    	<input type="text" id="title" name="title" value="${board.title}"></input>
		</div>
		<div class="form-row">
			<label for="content">내용</label>
			<textarea type="text" id="content" name="content">${board.content}</textarea>
		</div>
		<!-- 첨부파일 -->
		<div class="attachment-area">파일첨부</div>
	
		<div class="button-group">
			<a href="${pageContext.request.contextPath}/board/view?id=${board.boardId}">취소</a>
			<button type="submit">저장</button>
		</div>
	</form>
</body>
</html>

