package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	/**
	 * API checking whether id is duplicated 
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// DB 조회 - select
		UserEntity user = userBO.getUserByLoginId(loginId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		if (user != null) {
			result.put("is_duplicated_id", true);			
		}
		else {
			result.put("is_duplicated_id", false);						
		}
		
		return result;
	}
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return Map<String,Object> result
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId
			,@RequestParam("password") String password
			,@RequestParam("name") String name
			,@RequestParam("email") String email) {
		
		// md5 알고리즘 => password hashing (복구화 알고리즘 x, 같은 문자로 hashing 하면 결과는 동일)
		String hashedPassword = EncryptUtils.md5(password);
		
		// DB insert
		Integer userId = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (userId != null) {
			result.put("code", 200);
			result.put("result", "success");
		}
		else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}
		
		return result;
	}
	
	/**
	 * 로그인 API
	 * @param loginId
	 * @param password
	 * @return
	 */
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		
		// 비밀번호 hashing
		String hashedPassword = EncryptUtils.md5(password);
		
		// DB select (loginId, hashedPassword)
		UserEntity user = userBO.getUserByLoginIdPassword(loginId, hashedPassword);
		
		// response
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // 로그인 성공
			// 로그인 처리
			// 로그인 정보를 세션에 담는다.(사용자마다)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "success");
			
		}
		else { // 로그인 실패
			result.put("code", 300);
			result.put("error_message", "존재하지 않는 사용자입니다.");			
		}
		return result;
	}
	
}
