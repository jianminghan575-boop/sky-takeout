package com.sky.service;/**
 * @author Hjm
 * @date 2026/2/5 19:06
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author Hjm
 * @date 2026/2/5 19:06
 * @description
 */

public interface DishService
{
	/**
	 * 新增菜品和对应的口味
	 * @param dishDTO
	 */
	void saveWithFlavor(DishDTO dishDTO);
	
	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
