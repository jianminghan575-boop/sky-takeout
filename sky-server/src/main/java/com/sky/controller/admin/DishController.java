package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private DishService dishService;
	@RequestMapping
	public Result save(@RequestBody DishDTO dishDTO)
	{
		log.info("新增菜品：{}",dishDTO);
		dishService.saveWithFlavor(dishDTO);
		return Result.success();
	}
	
	

}
