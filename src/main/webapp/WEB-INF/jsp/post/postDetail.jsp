<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요." value="${post.subject}">
		<textarea id="content" class="form-control" placeholder="내용을 입력하세요." rows="10">${post.content}</textarea>
		
		<c:if test="${not empty post.imagePath}">
		<div class="my-3">
			<img src="${post.imagePath}" alt="업로드 된 이미지" width="300">
		</div>
		</c:if>
		
		<div class="d-flex justify-content-end">
			<input type="file" id="file" class="my-3" accept=".jpg, .png, .jpeg, .gif" value="${post.imagePath}">
		</div>
		
		<div class="d-flex justify-content-between">
			<a href="/post/post-list-view" class="btn btn-dark">목록</a>
			<div>
				<button type="button" id="deleteBtn" class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
				<button type="button" id="saveBtn" class="btn btn-info" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>


<script>
	$(document).ready(function() {
		$("#deleteBtn").on("click", function() {
			let postId = $(this).data("post-id");
			
			$.ajax({
				type:"DELETE"
				,url:"/post/delete"
				,data:{"postId":postId}
				
				,success:function(data) {
					if (data.code == 200) {
						alert("글을 삭제했습니다.");
						location.href = "/post/post-list-view";
					}
					else {
						alert(data.error_message);
					}
				}
				,error:function(request, status, error) {
					alert("글 삭제에 실패했습니다. 관리자에게 문의해주세요.");
				}
			});
		});
		
		$("#saveBtn").on("click", function() {
			let postId = $(this).data("post-id");
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let fileName = $("#file").val();
			
			// validation check
			if (!subject) {
				alert("제목을 입력해주세요.");
				return;
			}
			if (!content) {
				alert("내용을 입력해주세요.");
				return;
			}
			if (fileName) {
				// 확장자만 뽑은 후 소문자롤 변경해서 검사
				let extension = fileName.split(".").pop().toLowerCase();
				if ($.inArray(extension, ["png", "jpg", "jpeg", "gif"]) == -1) {
					alert("사진만 저장할 수 있습니다.");
					$("#file").val("");
					return;					
				}
			}
			
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다.
			let formData = new FormData();
			formData.append("postId", postId);
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $("#file")[0].files[0]);
			
			$.ajax({
				type:"PUT"
				,url:"/post/update"
				,data:formData
				,enctype:"multipart/form-data"
				,processData:false
				,contentType:false
				
				,success:function(data) {
					if (data.code == 200) {
						alert("글을 수정했습니다.");
						location.reload();
					}
					else {
						alert(data.error_message);
					}
				}
				,error:function(request, status, error) {
					alert("수정에 실패했습니다. 관리자에게 문의해주세요.");
				}
			});
			
		});
	});
</script>