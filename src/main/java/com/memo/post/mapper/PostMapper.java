package com.memo.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {
	
	public List<Post> selectPostList();
	
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId, 
			@Param("standardId") Integer standardId, 
			@Param("direction") String direction, 
			@Param("limit") int limit);
	
	public Post selectPostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId);
	
	public int selectPostIdByUserIdSort(
			@Param("userId") int userId,
			@Param("sort") String sort);
	
	public void insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public void updatePostByPostId(
			@Param("postId") int postId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public int deletePostByPostId(int postId);
}
