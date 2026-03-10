package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
	
	/**
	 * 根据时间区间统计营业额
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	TurnoverReportVO getTurnover(LocalDate beginTime, LocalDate endTime);
	/**
	 * 用户统计
	 * @param begin
	 * @param end
	 * @return
	 */
	UserReportVO userStatistics(LocalDate begin,LocalDate end);
	/**
	 * 订单统计接口
	 * @param begin
	 * @param end
	 * @return
	 */
	OrderReportVO ordersStatistics(LocalDate begin,LocalDate end);
	/**
	 * 销量排名top10
	 * @param begin
	 * @param end
	 * @return
	 */
	SalesTop10ReportVO top10(LocalDate begin,LocalDate end);
	
	void exportBusinessData(HttpServletResponse response);
}
