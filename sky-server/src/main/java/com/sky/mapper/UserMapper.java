package com.sky.mapper;/**
 * @author Hjm
 * @date 2026/3/4 20:51
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Hjm
 * @date 2026/3/4 20:51
 * @description
 */
@Mapper
public interface UserMapper
{
	/**
	 * 根据openid查询用户
	 * @param openid
	 * @return
	 */
	@Select("select * from user where openid = #{openid}")
	User getByOpenid(String openid);
	/**
	 * 插入数据
	 * @param user
	 */
	void insert(User user);
}
