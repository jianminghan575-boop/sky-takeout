package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author Hjm
 * @date 2026/2/5 19:07
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */
@Service
public class DishServiceImpl implements DishService
{
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private DishFlavorMapper dishFlavorMapper;
	/**
	 * 新增菜品和对应的口味
	 * @param dishDTO
	 * 涉及到多表操作，学加上@Transactional注解
	 */
	@Transactional
	@Override
	public void saveWithFlavor(DishDTO dishDTO)
	{
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		// 保存一条菜品信息
		dishMapper.insert(dish);
		//获取insert语句生成的主键值
		Long id = dish.getId();
		// 保存N条口味信息
		List<DishFlavor> flavors = dishDTO.getFlavors();
		if (flavors != null && flavors.size() > 0)
		{
			for (DishFlavor flavor : flavors)
			{
				flavor.setDishId(id);
			}
			dishFlavorMapper.insertBatch(flavors);
		}
	}
}
