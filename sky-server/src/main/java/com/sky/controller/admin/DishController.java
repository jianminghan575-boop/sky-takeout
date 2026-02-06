package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hjm
 * @date 2026/2/5 18:58
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController
{
	/**
	 * 新增菜品
	 */
	@Autowired
	private DishService dishService;
	@RequestMapping
	public Result save(@RequestBody DishDTO dishDTO)
	{
		log.info("新增菜品：{}",dishDTO);
		dishService.saveWithFlavor(dishDTO);
		return Result.success();
	}
	
	
	/**
	 * 菜品分页查询
	 */
	@GetMapping("/page")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO)
	{
		log.info("分页查询：{}",dishPageQueryDTO);
		PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}
	

}
