package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 목록 화면
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/post-list-view")
	public String postListView(
			@RequestParam(value = "prevId", required = false) Integer prevIdParam,
			@RequestParam(value = "nextId", required = false) Integer nextIdParam,
			Model model, HttpSession session) {
		
		// login 여부 조회
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) { // not logged in
			// move to login page
			return "redirect:/user/sign-in-view";
		}
		
		// DB select - 글 목록 조회
		List<Post> posts = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		int nextId = 0;
		int prevId = 0;
		if (!posts.isEmpty()) { // posts가 비어 있을 때 = [] 오류를 방지하기 위해
			prevId = posts.get(0).getId(); // 리스트의 첫번쨰 글 번호
			nextId = posts.get(posts.size() - 1).getId(); // 리스트의 마지막 글 번호
			
			// 이전 방향의 끝인가?
			// prevId와 post 테이블의 가장 큰 id값이 같으면 이전 페이지 x
			if (postBO.isPrevLastPageByUserId(userId, prevId)) {
				prevId = 0;
			}
			
			// 다음 방향의 끝인가?
			// nextId와 post 테이블의 가장  id값이 같으면 이전 페이지 x
			if (postBO.isNextLastPageByUserId(userId, nextId)) {
				nextId = 0;
			}
		}
		
		model.addAttribute("viewName", "post/postList");
		model.addAttribute("posts", posts);
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
		return "template/layout";
	}
	
	/**
	 * 글쓰기 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView(Model model) {
		
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("userId");
		// DB select - postId + userId
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		
		model.addAttribute("viewName", "post/postDetail");
		model.addAttribute("post", post);
		return "template/layout";
	}
}
