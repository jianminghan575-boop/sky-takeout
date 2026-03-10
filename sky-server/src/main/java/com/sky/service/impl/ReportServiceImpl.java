package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Hjm
 * @date 2026/3/9 21:40
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */
@Service
public class ReportServiceImpl implements ReportService
{
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ReportMapper reportMapper;
	@Autowired
	private WorkspaceService workspaceService;
	/**
	 * 根据时间区间统计营业额
	 * @param begin
	 * @param end
	 * @return
	 */
	public TurnoverReportVO getTurnover(LocalDate begin,LocalDate end) {
		List<LocalDate> dateList = new ArrayList<>();
		dateList.add(begin);
		
		while (!begin.equals(end)){
			begin = begin.plusDays(1);//日期计算，获得指定日期后1天的日期
			dateList.add(begin);
		}
		
		List<Double> turnoverList = new ArrayList<>();
		for (LocalDate date : dateList) {
			LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
			LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
			Map map = new HashMap();
			map.put("status", Orders.COMPLETED);
			map.put("begin",beginTime);
			map.put("end", endTime);
			Double turnover = orderMapper.sumByMap(map);
			turnover = turnover == null ? 0.0 : turnover;
			turnoverList.add(turnover);
		}
		
		//数据封装
		return TurnoverReportVO.builder()
							   .dateList(StringUtils.join(dateList,","))
							   .turnoverList(StringUtils.join(turnoverList,","))
							   .build();
	}
	/**
	 * 用户统计
	 * @param begin
	 * @param end
	 * @return
	 */
	public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
		List<LocalDate> dateList = new ArrayList<>();
		dateList.add(begin);
		while (!begin.equals(end)){
			begin = begin.plusDays(1);
			dateList.add(begin);
		}
		
		List<Integer> newUserList = new ArrayList<>();
		List<Integer> totalUserList = new ArrayList<>();
		for (LocalDate date : dateList) {
			Map map = new HashMap();
			// 获取当天时间最大值
			LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
			map.put("end", endTime);
			Integer totalUsers = reportMapper.getUsersByTime(map);
			totalUsers = totalUsers == null ? 0 : totalUsers;
			totalUserList.add(totalUsers);
			
			// 获取当天时间最小值
			LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
			map.put("begin", beginTime);
			Integer newUsers = reportMapper.getUsersByTime(map);
			newUsers = newUsers == null ? 0 : newUsers;
			newUserList.add(newUsers);
		}
		
		return UserReportVO.builder()
						   .dateList(StringUtil.join(",", dateList))
						   .newUserList(StringUtil.join(",", newUserList))
						   .totalUserList(StringUtil.join(",", totalUserList))
						   .build();
	}
	/**
	 * 订单统计接口
	 * @param begin
	 * @param end
	 * @return
	 */
	public OrderReportVO ordersStatistics(LocalDate begin,LocalDate end) {
		List<LocalDate> dateList = new ArrayList<>();
		dateList.add(begin);
		while (!begin.equals(end)){
			begin = begin.plusDays(1);
			dateList.add(begin);
		}
		
		List<Integer> orderCountList = new ArrayList<>();
		List<Integer> validOrderCountList = new ArrayList<>();
		for (LocalDate date : dateList) {
			// 获取当天时间最大值
			LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
			LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
			
			Map map = new HashMap();
			map.put("begin", beginTime);
			map.put("end", endTime);
			Integer orderCountTemp = reportMapper.getOrdersByTime(map);
			orderCountList.add(orderCountTemp);
			
			map.put("status", Orders.COMPLETED);
			Integer validOrderCountTemp = reportMapper.getOrdersByTime(map);
			validOrderCountList.add(validOrderCountTemp);
			
		}
		
		Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
		Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
		Double orderCompletionRate = 0.0;
		if(totalOrderCount != 0){
			orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
		}
		
		
		return OrderReportVO.builder()
							.dateList(StringUtil.join(",", dateList))
							.orderCountList(StringUtil.join(",", orderCountList))
							.validOrderCountList(StringUtil.join(",", validOrderCountList))
							.totalOrderCount(totalOrderCount)
							.validOrderCount(validOrderCount)
							.orderCompletionRate(orderCompletionRate)
							.build();
	}
	/**
	 * 销量排名top10
	 * @param begin
	 * @param end
	 * @return
	 */
	public SalesTop10ReportVO top10(LocalDate begin,LocalDate end) {
		LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
		LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
		
		Map map = new HashMap();
		map.put("begin", beginTime);
		map.put("end", endTime);
		List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(map);
		System.out.println("goodsSalesDTOList" + goodsSalesDTOList);
		
		String nameList = StringUtil.join(",", goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()));
		String numberList = StringUtil.join(",", goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()));
		
		return SalesTop10ReportVO.builder()
								 .nameList(nameList)
								 .numberList(numberList)
								 .build();
	}
	/**
	 * 导出运营数据报表
	 * @param response
	 */
	@Override
	public void exportBusinessData(HttpServletResponse response) {
		//1.查询数据库，获取营业数据----查询最近30天运营数据
		LocalDate dateBegin = LocalDate.now().minusDays(30);
		LocalDate dateEnd = LocalDate.now().minusDays(1);
		//查询概览数据
		BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));
		
		//2.通过POI将数据写入到Excel文件中
		//获得这个类对象，获得类加载器，从类路径下读取资源返回一个输入流对象
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
		
		try {
			//基于模板文件创建一个新的Excel文件
			XSSFWorkbook excel=new XSSFWorkbook(in);
			//获取表格文件的Sheet页
			XSSFSheet sheet = excel.getSheet("Sheet1");
			//填充数据--时间
			sheet.getRow(1).getCell(1).setCellValue("时间:"+dateBegin+"至"+dateEnd);
			//获得第4行
			XSSFRow row = sheet.getRow(3);
			row.getCell(2).setCellValue(businessDataVO.getTurnover());
			row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
			row.getCell(6).setCellValue(businessDataVO.getNewUsers());
			
			//获得第5行
			row= sheet.getRow(4);
			row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
			row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
			
			
			//填充明细数据
			for(int i=0;i<30;i++){
				LocalDate date =dateBegin.plusDays(i);
				//查询某一天的营业数据
				BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
				
				//获得某一行
				row = sheet.getRow(7 + i);
				row.getCell(1).setCellValue(date.toString());
				row.getCell(2).setCellValue(businessData.getTurnover());
				row.getCell(3).setCellValue(businessData.getValidOrderCount());
				row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
				row.getCell(5).setCellValue(businessData.getUnitPrice());
				row.getCell(6).setCellValue(businessData.getNewUsers());
			}
			
			//3.通过输出流将Excel下载到客户端浏览器
			ServletOutputStream out = response.getOutputStream();
			excel.write(out);
			
			//关闭资源
			out.close();
			excel.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
