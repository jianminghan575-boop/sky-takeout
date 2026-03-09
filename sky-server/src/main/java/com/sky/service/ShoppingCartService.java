package com.sky.service;/**
 * @author Hjm
 * @date 2026/3/8 13:13
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import java.util.List;

/**
 * @author Hjm
 * @date 2026/3/8 13:13
 * @description
 */
public interface ShoppingCartService
{
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO
	 */
	void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
	/**
	 *查看购物车
	 * @return
	 */
	List<ShoppingCart> showShoppingCart();
	/**
	 * 清空购物车
	 *
	 */
	void cleanShoppingCart();
	/**
	 * 删除购物车中一个菜品
	 */
	void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
