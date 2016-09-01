package com.jzb.fgang.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jzb.fgang.common.Page;

/**
 * Mybatis的分页查询插件，通过拦截StatementHandler的prepare方法来实现。
 * 只有在参数列表中包括Page类型的参数时才进行分页查询。
 * 在多参数的情况下，只对第一个Page类型的参数生效。
 * 另外，在参数列表中，Page类型的参数可以不用@Param来标注
 *
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})  
public class PagingPlugin implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(PagingPlugin.class);  
	private Dialect dialect;  
	
	/**
	 * 拦截后要执行的方法
	 */
	@SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation) throws Throwable {
		if(!(invocation.getTarget() instanceof RoutingStatementHandler)) 
			return invocation.proceed();
		
		RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        //分析是否含有分页参数，如果没有则不是分页查询
        //注意：在多参数的情况下，只处理第一个分页参数
        Page page = null;
        Object paramObj = boundSql.getParameterObject();
        if (paramObj instanceof Page){ //只有一个参数的情况
        	page = (Page)paramObj;
        }
        else if (paramObj instanceof Map){ //多参数的情况，找到第一个Page的参数
        	for (Map.Entry<String, Object> e : ((Map<String, Object>)paramObj).entrySet()){
        		if (e.getValue() instanceof Page){
        			page = (Page)e.getValue();
        			break;
        		}
        	}
        }
        
        if (page == null) return invocation.proceed();
        
        //查找总记录数，并设置Page的相关参数
        long total = this.getTotal(invocation);
        logger.debug("Total Count: " + total);
        page.setTotalRecord(total);
        //生成分页SQL
        String pageSql = generatePageSql(boundSql.getSql(), page);
        //强制修改最终要执行的SQL
        setFieldValue(boundSql, "sql", pageSql);
		return invocation.proceed();
	}
	
	

	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);	
	}
	

	/**
	 * 获取记录总数
	 */
	private long getTotal(Invocation invocation) throws Exception{
		RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
		/*
		 * 为了设置查找总数SQL的参数，必须借助MappedStatement、Configuration等这些类，
		 * 但statementHandler并没有开放相应的API，所以只好用反射来强行获取。
		 */
        BaseStatementHandler delegate = (BaseStatementHandler)getFieldValue(statementHandler, "delegate");
        MappedStatement mappedStatement = (MappedStatement)getFieldValue(delegate, "mappedStatement");
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        Object param = boundSql.getParameterObject();
        MetaObject metaObject = configuration.newMetaObject(param);
        
		long total = 0;
        String sql = boundSql.getSql();
        String countSql = "select count(1) from (" + sql+ ") t__"; //mysql要求有别名，但别名之前别加as，因为oracle不支持
        try{
            Connection conn = (Connection)invocation.getArgs()[0];
	        PreparedStatement ps = conn.prepareStatement(countSql);
	        int i = 1;
	        for (ParameterMapping pm : boundSql.getParameterMappings()) {
	        	Object value = null;
	        	String propertyName = pm.getProperty();
	            PropertyTokenizer prop = new PropertyTokenizer(propertyName);
	        	if (typeHandlerRegistry.hasTypeHandler(param.getClass())) {
	                value = param;  
	            }
	            else if (boundSql.hasAdditionalParameter(propertyName)) {
	                value = boundSql.getAdditionalParameter(propertyName);  
	            }
	            else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {  
	                value = boundSql.getAdditionalParameter(prop.getName());
	                if (value != null) {  
	                    value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));  
	                }
	            } else {  
	                value = metaObject.getValue(propertyName);
	            }

	        	((BaseTypeHandler)(pm.getTypeHandler())).setParameter(ps, i++, value, pm.getJdbcType());
	        }
	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        total = rs.getLong(1);
	        rs.close();  
	        ps.close();
        }
        catch (Exception e){
        	throw new RuntimeException("分页查询无法获取总记录数", e);
        }
        return total;
	}
	

	/**
	 * 生成分页SQL
	 */
	private String generatePageSql(String sql, Page page){
		StringBuilder pageSql = new StringBuilder();
		switch(this.dialect){
			case MYSQL:
	            pageSql.append(sql);
	            pageSql.append(" limit ").append(page.getRecordbegin()).append(",").append(page.getPageSize());  
	            break;
			case ORACLE:
	            pageSql.append("select * from (select t.*, ROWNUM num__ from (")
	        	.append(sql).append(") t where ROWNUM <= ")
	            .append(page.getRecordbegin() + page.getPageSize())
	            .append(") where num__ > ").append(page.getRecordbegin());
	            break;
			case SYBASE:
				// TODO: Sybase分页
	        default:
	        	throw new RuntimeException("分页插件还不支持数据库类型:" + dialect.toString());
		}
        return pageSql.toString();
    }	
		

		/**
		 *用反射取对象的属性值
		 */
		public static Object getFieldValue(Object obj, String fieldName) {
	        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
				try{
		        	Field field = superClass.getDeclaredField(fieldName);
					field.setAccessible(true);
					return field.get(obj);
				}
				catch(Exception e){}
			}
	        return null;
		}

		/**
		 * 利用反射获取指定对象里面的指定属性
		 */
		private static Field getField(Object obj, String fieldName) {
			Field field = null;
			for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
					.getSuperclass()) {
				try {
					field = clazz.getDeclaredField(fieldName);
					break;
				} catch (NoSuchFieldException e) {
					// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
				}
			}
			return field;
		}

		/**
		 * 用反射设置对象的属性值
		 */
		public static void setFieldValue(Object obj, String fieldName,
				String fieldValue)  throws Exception {
	        Field field = obj.getClass().getDeclaredField(fieldName);
	        field.setAccessible(true);
	        field.set(obj, fieldValue);
		}
	
	/**
	 * 从配置文件中注入数据库方言
	 */
	public void setDialect(String dialect){
		//注意这里不要往外抛异常，否则会死循环。。。
		try{
			this.dialect = Dialect.valueOf(dialect.toUpperCase());
		}
		catch(Exception e){
			logger.error("MyBatis分页插件不支持此数据库言：{}。请检查配置文件。", dialect);
			System.exit(1);
		}
	}
	
	
	/**
	 * 数据库方言枚举
	 *
	 */
	enum Dialect{
		MYSQL, ORACLE, SYBASE
	}


	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub
		
	}	
}
