package com.memo.post.bo;

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
	
	/**
	 * 로그인 된 사용자의 메모만 조회
	 * @param userId
	 * @return
	 */
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
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
	
	public void deletePost(int userId, String userLoginId, int postId) {
		// 기존 글 가져오기
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			log.info("[deletePost] post is null. postId:{} userId:{}", postId, userId);
		}
		
		// 기존 이미지 존재 확인 + 있을 시 삭제
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
		
		// DB delete
		postMapper.deletePostByPostId(postId);
	}
	
}
