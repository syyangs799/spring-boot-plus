package com.syyang.springbootplus.framework.common.annotationun;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/9 21:52
 * @UpdateDate: 2023/3/9 21:52
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 部门数据权限
 */
@Documented
@Target({METHOD, ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
public @interface ProjectDataPermission {
    /**
     * 是否要进行数据权限隔离 部门
     */
    boolean isDepartmentPermi() default false;
    /**
     * 是否要进行数据权限隔离 创建用户
     */
    boolean isCreateUserPermi() default false;
}
