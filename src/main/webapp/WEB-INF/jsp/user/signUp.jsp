<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center w-100 h-100">
	<div class="sign-up-box">
		<h1 class="mb-4">회원가입</h1>
		<form id="signUpForm" method="post" action="/user/sign-up">
			<table class="sign-up-table table table-bordered">
				<tr>
					<th><span class="d-flex justify-content-center">* 아이디</span></th>
					<td>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId" class="form-control col-7">
							<button type="button" id="checkDuplicatedIdBtn" class="btn btn-info ml-2">
								<small>중복확인</small>
							</button>
						</div>
						
						<%-- 아이디 체크 결과 --%>
						<div id="idCheckLength" class="small text-danger d-none">ID를 4자 이상 입력해주세요.</div>
						<div id="idCheckDuplicated" class="small text-danger d-none">이미 사용중인 ID입니다.</div>
						<div id="idCheckOk" class="small text-success d-none">사용 가능한 ID 입니다.</div>
					</td>
				</tr>
				<tr>
					<th><span class="d-flex justify-content-center">* 비밀번호</span></th>
					<td><input type="password" id="password" name="password" class="form-control col-7"></td>
				</tr>
				<tr>
					<th><span class="d-flex justify-content-center">* 비밀번호 확인</span></th>
					<td>
						<input type="password" id="confirmPassword" class="form-control col-7">
						<small id="pw-failed" class="text-danger d-none">비밀번호가 일치하지 않습니다.</small>
					</td>
				</tr>
				<tr>
					<th><span class="d-flex justify-content-center">* 이름</span></th>
					<td><input type="text" id="name" name="name" class="form-control col-7">
					</td>
				</tr>
				<tr>
					<th><span class="d-flex justify-content-center">* 이메일주소</span></th>
					<td><input type="text" id="email" name="email" class="form-control">
					</td>
				</tr>
			</table>
	
			<button type="submit" id="joinBtn" class="btn btn-primary float-right">회원가입</button>
		</form>
	</div>
</div>


<script>
	$(document).ready(function() {
		
		// 아이디 중복 확인
		$("#checkDuplicatedIdBtn").on("click", function() {
			// 경고 문구 초기화
			$("#idCheckLength").addClass("d-none");
			$("#idCheckDuplicated").addClass("d-none");
			$("#idCheckOk").addClass("d-none");
			
			let loginId = $("#loginId").val().trim();
			
			if (loginId.length < 4) {
				$("#idCheckLength").removeClass("d-none");
				return;
			}
			
			// AJAX - 중복 확인
			$.get("/user/is-duplicated-id", {"loginId":loginId}) // request
			.done(function(data) { //response
				if (data.code == 200) {
					if (data.is_duplicated_id) { // 중복
						$("#idCheckDuplicated").removeClass("d-none");
					}
					else { // 사용 가능
						$("#idCheckOk").removeClass("d-none");
					}
				}
				else {
					alert(data.error_message);
				}
			}); 
			
			
		});
		
		// 회원가입을 눌렀을 때
		$("#signUpForm").on("submit", function(e) {
			e.preventDefault(); // submit 기능 막음
	
			// validation check
			let loginId = $("#loginId").val().trim();
			let password = $("#password").val().trim();
			let confirmPassword = $("#confirmPassword").val().trim();
			let name = $("#name").val().trim();
			let email = $("#email").val().trim();
			
			if (!loginId) {
				alert("아이디를 입력하세요.");
				return false;
			}
			if (!password || !confirmPassword) {
				alert("비밀번호를 입력하세요.");
				return false;
			}
			if (password != confirmPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return false;
			}
			if (!name) {
				alert("이름을 입력하세요.");
				return false;
			}
			if (!email) {
				alert("이메일을 입력하세요.");
				return false;
			}
			// 중복 확인 후 사용 가능한 아이디인지 확인
			// => idCheckOk class x have d-none
			if ($("#idCheckOk").hasClass("d-none")){
				alert("아이디 중복 확인을 다시 해주세요.");
				return false;
			}
			
			
			// 1) 서버 전송: submit을 js에서 동작
			// $(this)[0].submit(); // 화면 이동
			
			// 2) AJAX: 화면 이동 x(call back 함수에서), response = json
			let url = $(this).attr("action");
			let params = $(this).serialize(); // form tag에 있는 name attri의 val로 parameter로 구성
			
			$.post(url, params) // requst
			.done(function(data) {
				if (data.code == 200) {
					alert("가입을 환영합니다. 로그인 해주세요.");
					location.href = "/user/sign-in-view";
				}
				else {
					// logic failure
					alert(data.error_message);
				}
			});
		});
		
	});
</script>