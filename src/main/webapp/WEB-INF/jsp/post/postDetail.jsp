<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요." value="${post.subject}">
		<textarea id="content" class="form-control" placeholder="내용을 입력하세요." rows="10" value="${post.content}"></textarea>
		
		<c:if test="${not empty post.imagePath}">
		<div class="my-3">
			<img src="${post.imagePath}" alt="업로드 된 이미지" width="300">
		</div>
		</c:if>
		
		<div class="d-flex justify-content-end">
			<input type="file" id="file" class="my-3" accept=".jpg, .png, .jpeg, .gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<a href="/post/post-list-view" class="btn btn-dark">목록</a>
			<div>
				<button type="button" id="deleteBtn" class="btn btn-secondary">삭제</button>
				<button type="button" id="saveBtn" class="btn btn-info">수정</button>
			</div>
		</div>
	</div>
</div>

