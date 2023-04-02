package com.syyang.springbootplus.framework.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.syyang.springbootplus.framework.common.annotationun.LogForUpdate;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/18 21:09
 * @UpdateDate: 2023/3/18 21:09
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 获取两个对象的不同属性的描述信息
 */
public class BeanUtils {
    /**
     * 获取变更内容
     * @param newBean 更改前的Bean
     * @param oldBean 更改后的Bean
     * @param <T>
     * @return
     */
    public static <T> String getChangedFields(T newBean, T oldBean){
        Field[] fields = newBean.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        for(Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(LogForUpdate.class)) {
                try {
                    Object newValue = field.get(newBean);
                    Object oldValue = field.get(oldBean);
                    if(!Objects.equals(newValue, oldValue) && StrUtil.isNotBlank(newValue.toString()) && StrUtil.isNotBlank(oldValue.toString())) {
                        builder.append(field.getAnnotation(LogForUpdate.class).fieldName()); //获取字段名称
                        if(!field.getAnnotation(LogForUpdate.class).isCode()) {
                            builder.append(": 【更改前：");
                            builder.append(null == newValue?"":newValue);
                            builder.append(", 更改后：");
                            builder.append(null == oldValue?"":oldValue);
                            builder.append("】,");
                        }else{
                            Map<String,String> codeMap = Maps.newHashMap();
                            try {
                                codeMap = JSONUtil.toBean(field.getAnnotation(LogForUpdate.class).codeJson(), Map.class);
                            }catch (Exception e){
                            }
                            builder.append(": 【更改前：");
                            builder.append(null == newValue?"":codeMap.getOrDefault(newValue.toString(),""));
                            builder.append(", 更改后：");
                            builder.append(null == oldValue?"":codeMap.getOrDefault(oldValue.toString(),""));
                            builder.append("】,");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return builder.toString();
    }
}
