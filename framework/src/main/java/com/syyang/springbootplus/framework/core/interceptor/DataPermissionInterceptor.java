package com.syyang.springbootplus.framework.core.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.syyang.springbootplus.framework.common.annotationun.ProjectDataPermission;
import com.syyang.springbootplus.framework.shiro.cache.LoginRedisService;
import com.syyang.springbootplus.framework.shiro.service.LoginToken;
import com.syyang.springbootplus.framework.shiro.util.JwtTokenUtil;
import com.syyang.springbootplus.framework.shiro.util.JwtUtil;
import com.syyang.springbootplus.framework.shiro.vo.LoginSysUserRedisVo;
import com.syyang.springbootplus.framework.util.HttpServletRequestUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: yyangs
 * @CreateDate: 2023/3/9 21:54
 * @UpdateDate: 2023/3/9 21:54
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 部门注解数据权限
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataPermissionInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);

    @Autowired
    private LoginRedisService loginRedisService;

    //扫描的包路径（根据自己的项目路径来），这里是取的配置里的包路径
    @Value("${spring-boot-plus.interceptor.permission.package-path}")
    private String packagePath;

    private final static String DEPARTMENT_ID = "department_id";

    private final static String CREATE_USER = "create_user";

    private static List<String> classNames;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
            if (StrUtil.isEmpty(username)){
                logger.info("获取到当前用户的username为空，不进行数据权限隔离");
                return invocation.proceed();
            }
            // 获取登录用户角色权限信息
            LoginSysUserRedisVo loginSysUserRedisVo = loginRedisService.getLoginSysUserRedisVo(username);
            if (loginSysUserRedisVo == null){
                return invocation.proceed();
            }

            Long deptId = loginSysUserRedisVo.getDepartmentId();
            if (deptId == null){
                deptId = 0L;
            }
            //反射扫包会比较慢，这里做了个懒加载
            if (classNames == null) {
                synchronized (LazyInit.class){
                    if (classNames == null){
                        //扫描指定包路径下所有包含指定注解的类
                        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(packagePath, ProjectDataPermission.class);
                        if (classSet == null && classSet.size() == 0){
                            classNames = new ArrayList<>();
                        } else {
                            //取得类全名
                            classNames =  classSet.stream().map(Class::getName).collect(Collectors.toList());
                        }
                    }
                }
            }

            // 拿到mybatis的一些对象
            StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

            // mappedStatement.getId()为执行的mapper方法的全路径名,newId为执行的mapper方法的类全名
            String newId = mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
            // 如果不是指定的方法，直接结束拦截
            if (!classNames.contains(newId)) {
                return invocation.proceed();
            }
            String newName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1, mappedStatement.getId().length());
            //是否开启数据权限
            boolean isDepartmentPermi = false;
            boolean isCreateUserPermi = false;
            Class<?> clazz = Class.forName(newId);
            //遍历方法
            for (Method method : clazz.getDeclaredMethods()) {
                //方法是否含有DataPermission注解，如果含有注解则将数据结果过滤
                if (method.isAnnotationPresent(ProjectDataPermission.class) && newName.equals(method.getName())) {
                    ProjectDataPermission dataPermission =  method.getAnnotation(ProjectDataPermission.class);
                    if (dataPermission != null) {
                        //不验证
                        isDepartmentPermi = dataPermission.isDepartmentPermi();
                        isCreateUserPermi = dataPermission.isCreateUserPermi();
                    }
                }
            }

            if (isDepartmentPermi || isCreateUserPermi){
                // 获取到原始sql语句
                String sql = statementHandler.getBoundSql().getSql();

                // 解析并返回新的SQL语句，只处理查询sql
                if (mappedStatement.getSqlCommandType().toString().equals("SELECT")) {
                    //                    String newSql = getNewSql(sql,deptIds,user.getUserId());
                    sql = getSql(sql,deptId,loginSysUserRedisVo.getId().toString(),isDepartmentPermi,isCreateUserPermi);
                }
                // 修改sql
                metaObject.setValue("delegate.boundSql.sql", sql);
            }
            return invocation.proceed();
        } catch (Exception e){
            logger.error("数据权限隔离异常：", e);
            return invocation.proceed();
        }

    }


    /**
     * 解析SQL语句，并返回新的SQL语句
     * 注意，该方法使用了JSqlParser来操作SQL，该依赖包Mybatis-plus已经集成了。如果要单独使用，请先自行导入依赖
     *
     * @param sql 原SQL
     * @return 新SQL
     */
    private String getSql(String sql,Long deptId,String userId,boolean isDepartmentPermi,boolean isCreateUserPermi) {

        try {
            String condition = "";
            if (isDepartmentPermi){
                // 修改原语句
                condition += "," + DEPARTMENT_ID +" = " + deptId;
            }
            if(isCreateUserPermi) {
                condition += "," + CREATE_USER +" = " + userId;
            }

            if (StrUtil.isBlank(condition)){
                return sql;
            }else{
                condition = condition.substring(1);
            }
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            PlainSelect plainSelect = (PlainSelect)select.getSelectBody();
            //取得原SQL的where条件
            final Expression expression = plainSelect.getWhere();
            //增加新的where条件
            final Expression envCondition = CCJSqlParserUtil.parseCondExpression(condition);
            if (expression == null) {
                plainSelect.setWhere(envCondition);
            } else {
                AndExpression andExpression = new AndExpression(expression, envCondition);
                plainSelect.setWhere(andExpression);
            }
            return plainSelect.toString();
        } catch (JSQLParserException e) {
            logger.error("解析原SQL并构建新SQL错误：" + e);
            return sql;
        }
    }
}