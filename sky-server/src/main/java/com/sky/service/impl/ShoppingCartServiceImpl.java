package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Hjm
 * @date 2026/3/8 13:14
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService
{
	
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealMapper setmealMapper;
	
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO
	 */
	@Override
	public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		//获取购物车列表
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
		shoppingCart.setUserId(BaseContext.getCurrentId());
		
		//判断购物车中是否存在该商品
		List<ShoppingCart> shoppingCartList  = shoppingCartMapper.list(shoppingCart);
		if (shoppingCartList!=null && shoppingCartList.size()>0){
			//那么购物车中有商品 , 跟新数量加1 即可
			ShoppingCart cart = shoppingCartList.get(0);
			cart.setNumber(cart.getNumber()+1);
			shoppingCartMapper.updateNumberById(cart);
		}else{
			//否则购物车中不存在菜品或套餐：
			//判断添加的是菜品还是套餐
			Long dishId = shoppingCartDTO.getDishId();
			if (dishId != null){
				//那么就是有菜品.否则就是有套餐
				//那么就根据菜品id查询菜品信息，添加菜品
				Dish dish = dishMapper.getById(dishId);
				shoppingCart.setName(dish.getName());
				shoppingCart.setImage(dish.getImage());
				shoppingCart.setAmount(dish.getPrice());
			}else {
				//否则添加套餐
				Long setmealId = shoppingCartDTO.getSetmealId();
				Setmeal setmeal = setmealMapper.getById(setmealId);
				shoppingCart.setName(setmeal.getName());
				shoppingCart.setImage(setmeal.getImage());
				shoppingCart.setAmount(setmeal.getPrice());
			}
			//跟新菜品/套餐 数据
			shoppingCart.setNumber(1);
			shoppingCart.setCreateTime(LocalDateTime.now());
			shoppingCartMapper.insert(shoppingCart);
			
			
		}
		
	}
	/**
	 * 查看购物车
	 * @return
	 */
	@Override
	public List<ShoppingCart> showShoppingCart() {
		//获取当前用户id
		Long userId = BaseContext.getCurrentId();
		ShoppingCart shoppingCart = ShoppingCart.builder()
												.userId(userId)
												.build();
		//使用当前用户id查询当前用户的购物车
		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
		return list;
	}
	/**
	 *
	 * 清空购物车
	 */
	@Override
	public void cleanShoppingCart() {
		//只清空当前用户的购物车
		//获取当前用户id
		Long userId = BaseContext.getCurrentId();
		shoppingCartMapper.deleteByUserId(userId);
		
	}
	/**
	 * 删除购物车中某个菜品
	 * @param shoppingCartDTO
	 */
	@Override
	public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		//1.获取当前用户的购物车中所有菜品，根据用户当前id查询
		Long userId = BaseContext.getCurrentId();
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUserId(userId);
		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
		
		//2.遍历每一个菜品数量，如果大于1 那么数量-1
		if (list == null || list.size() == 0){
			throw new BaseException("购物车中不存在该商品");
		}
		ShoppingCart cart = list.get(0);
		Integer number = cart.getNumber();
		if (number > 1){
			//数量大于1，跟新数量-1
			cart.setNumber(number - 1);
			shoppingCartMapper.updateNumberById(cart);
		}else {
			//数量等于1，删除该商品
			shoppingCartMapper.deleteById(cart);
		}
		//3.否则删除此菜品
	}
	
}
