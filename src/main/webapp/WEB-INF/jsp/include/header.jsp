<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-between align-items-center h-100 p-4">
	<%--logo --%>
	<div>
		<h3 class="font-weight-bold">메모 게시판</h3>
	</div>
	
	<%-- login info --%>
	<div>
		<span>${userName}님 안녕하세요</span>
		<a href="/user/sign-out">로그아웃</a>
	</div>
</div>