package com.sky.mapper;/**
 * @author Hjm
 * @date 2026/3/10 09:17
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

/**
 * @author Hjm
 * @date 2026/3/10 09:17
 * @description
 */
@Mapper
public interface ReportMapper
{
	/**
	 * 用户统计接口
	 * @param map
	 * @return
	 */
	Integer getUsersByTime(Map map);
	/**
	 * 订单统计接口
	 * @param map
	 * @return
	 */
	Integer getOrdersByTime(Map map);
	
}
