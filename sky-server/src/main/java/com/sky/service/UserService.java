package com.sky.service;/**
 * @author Hjm
 * @date 2026/3/4 20:36
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author Hjm
 * @date 2026/3/4 20:36
 * @description
 */
public interface UserService
{
	/**
	 * 微信登录
	 * @param userLoginDTO
	 * @return
	 */
	User wxLogin(UserLoginDTO userLoginDTO);
}
