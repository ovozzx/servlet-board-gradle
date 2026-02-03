<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">
</head>
<body>
	<div class="wrapper">
    	<h1>자유 게시판 - 목록</h1>
		<form action="${pageContext.request.contextPath}/board/list" method="get">
			<ul class="top-bar">
				<input name="page" type="hidden" />
				<li>등록일</li>
				<li>
					<input name="startDate" type="date" value="${param.startDate}"/>
					~
					<input name="endDate" type="date" value="${param.endDate}"/>
				</li>
				<li>
					<!-- TODO : db 읽어서 -->
					<select name="categoryId">
						<option value="0">전체 카테고리</option>
						<option value="1" ${param.categoryId == '1' ? 'selected' : ''} >Database</option>
						<option value="2" ${param.categoryId == '2' ? 'selected' : ''} >JAVA</option>
						<option value="3" ${param.categoryId == '3' ? 'selected' : ''}>Javascript</option>
					</select>
					<input name="keyword" type="text" class="search-input"
						   placeholder="검색어를 입력해 주세요. (제목+작성자+내용)"
						   value="${param.keyword}" />
				</li>
				<li>
					<button type="submit" class="search-btn">검색</button>
				</li>
			</ul>
		</form>
		<div class="count-bar">총  ${allBoardCnt} 건</div>
		<table class="list" border="1">
			<thead>
				<tr>
					<th>카테고리</th>
					<th>제목</th>
					<th>작성자</th>
					<th>조회수</th>
					<th>등록 일시</th>
					<th>수정 일시</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td>${board.categoryName}</td>
						<td><a class="title" href="view?id=${board.boardId}">${board.title}</a></td>
						<td>${board.createUser}</td>
						<td>${board.viewCount}</td>
						<td>${board.createDate}</td>
						<td>${board.modifyDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- 페이지네이션 : 전체 데이터 수에 따라 유동적을 바뀜 -->

		<c:forEach begin="1" end="${pageCnt}" var="i">
			<a href="${pageContext.request.contextPath}/board/list?page=${i}&startDate=${param.startDate}&endDate=${param.endDate}&categoryId=${param.categoryId}&keyword=${param.keyword}">${i}</a>
		</c:forEach>
		<div class="pagination">

		</div>
		<div class="bottom-bar">
			<a class="write" href="write">등록</a>
		</div>
	</div>
</body>
</html>

