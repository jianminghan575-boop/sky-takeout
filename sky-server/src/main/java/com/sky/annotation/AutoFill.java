package com.sky.annotation;/**
 * @author Hjm
 * @date 2026/2/5 09:58
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */

import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解  用于标注某个方法需要进行公共字段填充
 */
// 指定注解的位置
@Target(ElementType.METHOD)
// 指定注解的保留策略
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill
{
	// 填充数据的操作类型
	OperationType value();
}
