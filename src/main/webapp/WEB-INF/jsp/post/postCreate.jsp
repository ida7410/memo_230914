<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요.">
		<textarea id="content" class="form-control" placeholder="내용을 입력하세요." rows="10"></textarea>
		
		<div class="d-flex justify-content-end">
			<input type="file" id="file" class="my-3" accept=".jpg, .png, .jpeg, .gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-primary">저장</button>
			</div>
		</div>
	</div>
</div>


<script>
	$(document).ready(function() {
		
		// 목록 버튼 클릭 => 목록 화면 이동
		$("#postListBtn").on("click", function() {
			location.href = "/post/post-list-view";
		});
		
		// 모두 지우기 버튼 클릭
		$("#clearBtn").on("click", function() {
			$("#subject").val("");
			$("#content").val("");
		});
		
		// 저장
		$("#saveBtn").on("click", function() {
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let fileName = $("#file").val(); // C:\fakepath\zzz.png
			
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
			/* if (fileName && !(fileName.endsWith(".png") || fileName.endsWith(".jpg") || 
					fileName.endsWith(".jpeg") || fileName.endsWith(".gif"))) {
				alert("사진만 저장할 수 있습니다.");
				return;
			} */
			
			// form 태그를 js에서 만든다.
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다.
			let formData = new FormData();
			formData.append("subject", subject); // key는 name 속성과 같다 + Request Param
			formData.append("content", content);
			formData.append("file", $("#file")[0].files[0]);
			
			// AJAX
			$.ajax({
				// request
				type:"post"
				, url:"/post/create"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false // 파일 업로드를 위한 필수 설정
				, contentType:false // 파일 업로드를 위한 필수 설정
				
				// response
				, success:function(data) {
					if (data.code == 200) {
						alert("메모를 저장했습니다.");
						location.href = "/post/post-list-view";
					}
					else {
						alert(data.error_message);
					}
				}
				, error:function(request, status, error) {
					alert("글을 저장하는 데 실패했습니다. 관리자에게 문의해주세요.");
				}
			});
		});
	});
</script>