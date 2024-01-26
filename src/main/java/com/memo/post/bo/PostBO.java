package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostBO {
//	private Logger logger = LoggerFactory.getLogger(PostBO.class);
//	private Logger logger = LoggerFactory.getLogger(this.class);
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 페이징 필드
	private static final int POST_MAX_SIZE = 3;
	
	/**
	 * 로그인 된 사용자의 메모만 조회
	 * @param userId
	 * @return
	 */
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		//  10 9 8 | 7 6 5 | 4 3 2 | 1
		// 4 3 2 페이지에서
		// - 다음 = 2보다 작은 세 포스트 desc = select * from `post` where `id` < 2 order by `id` desc limit 3 = 1
		// - 이전 = 4보다 큰 세 포스트 = select * from `post` where `id` > 4 order by `id` limit 3 = 5 6 7 -> list reverse = 7 6 5
		// - 페이징 정보 x = 최신순 3개 desc
		
		Integer standardId = null; // 기준이 되는 postId
		String direction = null; // 방향
		if (prevId != null) { // 이전
			standardId = prevId;
			direction = "prev";
			
			List<Post> postlist = postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
			Collections.reverse(postlist); // 뒤집고 저장
			return postlist;
		}
		else if (nextId != null) { // 다음
			standardId = nextId;
			direction = "next";			
		}
		
		// 페이징 정보 없음
		return postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
	}
	
	// 이전 페이지의 마지막인가?
	public boolean isPrevLastPageByUserId(int userId, int prevId) {
		int postId = postMapper.selectPostIdByUserIdSort(userId, "DESC");
		return postId == prevId;
	}
	
	// 다음 페이지의 마지막인가?
	public boolean isNextLastPageByUserId(int userId, int nextId) {
		int postId = postMapper.selectPostIdByUserIdSort(userId, "ASC");
		return postId == nextId;
	}
	
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
	}
	
	/**
	 * 
	 * @param userId
	 * @param userLogindId
	 * @param subject
	 * @param content
	 * @param imagePath
	 */
	public void addPost(
			int userId, String userLogindId, 
			String subject, String content, 
			MultipartFile file) {
		
		String imagePath = null;
		
		// 업로드할 이미지가 있을 때 업로드
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLogindId, file);
		}
		
		postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	/**
	 * update post
	 * @param userId
	 * @param userLogindId
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 */
	public void updatePost(
			int userId, String userLogindId, 
			int postId, String subject, String content, 
			MultipartFile file) {
		
		// 기존 글 가져오기 (이미지 교체 시 삭제 + 업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			log.info("[updatePost] post is null. postId:{} userId:{}", postId, userId);
			return;
		}
		
		// 파일이 있음
		// 1) 새 이미지 업로드
		// 2) 1번 성공 시 기존 이미지 제거(기존 이미지가 있다면)
		String imagePath = null;
		if (file != null) {
			// 업로드
			imagePath = fileManagerService.saveFile(userLogindId, file);
			
			// 업로드 성공 & 기존 이미지가 있다면
			if (imagePath != null && post.getImagePath() != null) {
				// 기존 이미지 제거
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		// DB update
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
	}
	
	public void deletePostByPostId(int userId, String userLoginId, int postId) {
		// 기존 글 가져오기
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			log.info("[deletePost] post is null. postId:{} userId:{}", postId, userId);
			return;
		}
		
		// DB delete
		int deletedRowCount = postMapper.deletePostByPostId(postId);
		if (deletedRowCount > 0 && post.getImagePath() != null) {
			// 기존 이미지 존재 확인 + 있을 시 삭제
			if (post.getImagePath() != null) {
				fileManagerService.deleteFile(post.getImagePath());
			}			
		}
		
	}
	
}
