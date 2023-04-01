package com.syyang.springbootplus.framework.common.annotationun;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/18 21:15
 * @UpdateDate: 2023/3/18 21:15
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 项目信息修改的日志记录
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogForUpdate {
    String fieldName() default "";

    boolean isCode() default false;
    /**
     * json 码表的
     * @return
     */
    String codeJson() default "";
}
