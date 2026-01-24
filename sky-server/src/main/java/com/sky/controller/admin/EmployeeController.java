package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }
    /**
     * 新增员工
     * 当前端提交的数据和实体类中对应的属性差别比较大时，建议使用DTO来封装数据
     * 比如这个登录方法 ,前端发来的数据不用Employee封装，而要用EmployeeDTO来封装
     * 前端提交什么数据，对应的DTO对象就封装什么属性。
     */
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO)
    {
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }
    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    /**
     * 分页查询
     * @param employeePageQuery
     * @return 分页查询，前端提交的参数，页码，页数，还可能有员工姓名，请求参数不是JSON格式的而是Query，也就是地址栏？进行传参的。
	 * 分页查询的返回给前端的，统一封装成PageResult对象，之后再统一封装成Result对象。
	 * 当请求参数是Query不是JSON时，不用@RequestBody注解，下面这样就可以接受参数。
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQuery)
    {
        log.info("员工分页查询：{}",employeePageQuery);
        //分页查询的返回给前端的，统一封装成PageResult对象，之后再统一封装成Result对象。
        PageResult pageResult = employeeService.pageQuery(employeePageQuery);
        // *** 返回的日期数据，在前端展示的是一串数字 需要在WebMvcConfig.java中添加一个转换器统一对日期类型进行格式话处理
        return Result.success(pageResult);
    }
}
