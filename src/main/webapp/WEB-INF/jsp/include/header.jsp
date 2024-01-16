<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-between align-items-center h-100 p-4">
	<%--logo --%>
	<div>
		<h3 class="font-weight-bold">메모 게시판</h3>
	</div>
	
	<%-- login info --%>
	<div>
	<c:if test="${not empty userId}">
		<span>${userName}님 안녕하세요</span>
		<a href="/user/sign-out">로그아웃</a>
	</c:if>
	<c:if test="${empty userId}">
		<a href="/user/sign-in-view">로그인</a>
	</c:if>
	</div>
</div>