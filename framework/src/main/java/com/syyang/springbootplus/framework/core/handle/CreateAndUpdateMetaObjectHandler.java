package com.syyang.springbootplus.framework.core.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.syyang.springbootplus.framework.common.entity.BaseEntity;
import com.syyang.springbootplus.framework.shiro.cache.LoginRedisService;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MP注入处理器
 *
 * @author dsg
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private LoginRedisService loginRedisService;
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            //继不继承 BaseEntity 也不继承 SuperEntity
            if (metaObject.hasSetter("createTime") && Objects.isNull(metaObject.getValue("createTime"))) {
                metaObject.setValue("createTime", LocalDateTime.now());
            }
            if (metaObject.hasSetter("updateTime") && Objects.isNull(metaObject.getValue("updateTime"))) {
                metaObject.setValue("updateTime", LocalDateTime.now());
            }
            if (metaObject.hasSetter("create_user") && Objects.isNull(metaObject.getValue("create_user"))) {
                metaObject.setValue("create_user", JwtUtil.getUsername(JwtTokenUtil.getToken()));
            }
            if (metaObject.hasSetter("department_id") && Objects.isNull(metaObject.getValue("department_id"))) {
                metaObject.setValue("department_id", loginRedisService.getLoginSysUserRedisVo(JwtUtil.getUsername(JwtTokenUtil.getToken())).getDepartmentId());
            }
        } catch (Exception e) {
            throw new RuntimeException("自动填充属性异常 => " + e.getLocalizedMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            //继不继承 BaseEntity 也不继承 SuperEntity
            if (metaObject.hasSetter("updateTime")) {
                metaObject.setValue("updateTime", LocalDateTime.now());
            }
        } catch (Exception e) {
            throw new RuntimeException("自动填充属性异常 => " + e.getLocalizedMessage());
        }
    }

}
