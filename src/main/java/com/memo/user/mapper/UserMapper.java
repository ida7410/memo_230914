package com.memo.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.memo.user.domain.User;

@Mapper
public interface UserMapper {
	
	public User selectUserByLoginId(String logindId);
	
}
