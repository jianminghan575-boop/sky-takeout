package com.sky.mapper;/**
 * @author Hjm
 * @date 2026/3/8 13:19
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description 
 */

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * @author Hjm
 * @date 2026/3/8 13:19
 * @description 
 */
@Mapper
public interface ShoppingCartMapper {
	
	/**
	 * 根据用户id菜品id套餐id口味分类，查询 购物车
	 * @param shoppingCart
	 * @return
	 */
	List<ShoppingCart> list(ShoppingCart shoppingCart);
	
	
	/**
	 * 根据id修改购物车数量
	 * @param cart
	 */
	@Update("update shopping_cart set number=#{number} where id=#{id}")
	void updateNumberById(ShoppingCart cart);
	
	
	/**
	 * 新增套餐
	 *
	 */
	@Insert("insert into shopping_cart (id, name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
		"values (#{id},#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
	void insert(ShoppingCart shoppingCart);
	
	/**
	 * 根据当前用户id清空购物车`
	 * @param userId
	 */
	@Delete("delete from shopping_cart where user_id = #{userId}")
	void deleteByUserId(Long userId);
	
	@Delete("delete from shopping_cart where id = #{id}")
	void deleteById(ShoppingCart cart);
	
	void insertBatch(List<ShoppingCart> shoppingCartList);
}