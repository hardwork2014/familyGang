package com.jzb.family.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Created by hqm on 14-5-12.
 */
public class HttpClientUtils {
	
	private static Logger LOG = Logger.getLogger(HttpClientUtils.class);
	
	private static final String DEFAULT_CHARSET = "utf-8";
	
	 /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map<String, String> convertBean(Object bean) {
        try{
        	Class<? extends Object> type = bean.getClass();
            Map<String, String> map = new HashMap<String, String>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                    	map.put(propertyName, result.toString());
                    } else {
                    	map.put(propertyName, "");
                    }
                }
            }
            return map;
        } catch(IntrospectionException e) {
        	LOG.info(">>>>>>>>>>分析类属性失败");
        	e.printStackTrace();
        } catch (IllegalAccessException e) {
        	LOG.info(">>>>>>>>>>实例化 JavaBean 失败");
        	e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.info(">>>>>>>>>>调用属性的 setter 方法失败");
        	e.printStackTrace();
		}
    	return null;
    }
	
    public static String doPost(String url, Object params) throws IOException {
    	return doPost(url, params, "");
    }
    
    public static String doPost(String url, Object params, String charset) throws IOException {
    	return invokePost(url,params,charset);
    }
    
    /**
	 * since: 1.0.2
	 *
	 * @param url
	 * @param params,参数map，默认使用utf-8编码
	 * @return <p>
	 * 响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 * 所有请求共享一个httpclient
	 * </p>
	 */
	public static String doPost(String url, Map<String, String> params) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET);
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException <p>
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     所有请求共享一个httpclient
	 *                     </p>
	 * @since: 1.0.2
	 */
	public static String doPost(String url) throws IOException {
		return doPost(url, null, DEFAULT_CHARSET);
	}

	/**
	 * @param url
	 * @param params        请求参数
	 * @param encodeCharset 参数编码格式
	 * @return
	 * @throws IOException <p>
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     post request,所有请求共享一个httpclient
	 *                     </p>
	 * @since: 1.0.2
	 */
	public static String doPost(String url, Map<String, String> params, String encodeCharset) throws IOException {
		return invokePost(url, params, encodeCharset);
	}

	/**
	 * @param url
	 * @param params
	 * @param charset    参数字符编码方式
	 * @param httpClient
	 * @return
	 * @throws IOException <p>
	 *                     调用http post
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     </p>
	 * @since: 1.0.2
	 */
    
	private static String invokePost(String url, Object params, String charset) throws IOException {
        if (charset == null || charset.isEmpty()) {
			charset = DEFAULT_CHARSET;
		}
        LOG.info(">>httpclient post url:" + url);
        Map<String, String> map = convertBean(params);
		HttpPost postMethod = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		try {
			if (map != null) {
				List<NameValuePair> nvs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> pair : map.entrySet()) {
					nvs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
				}
				postMethod.setEntity(new UrlEncodedFormEntity(nvs, charset));
			}
			//创建响应处理器处理服务器响应内容
			HttpResponse response = client.execute(postMethod);
			HttpEntity entity = response.getEntity(); 
			String res = EntityUtils.toString(entity, charset);
			LOG.info(">>httpclient post 返回值:" + res);
			return res;
		} catch (IOException e) {
			postMethod.abort();
			throw new IOException("httpclint post 请求失败，url=" + url, e);
		} finally {
			postMethod.releaseConnection();
		}
	}
	
	/**
	 * @param url
	 * @return
	 * @throws java.io.IOException <p>
	 *                             响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                             所有请求共享一个个httpclient
	 *                             </p>
	 * @since: 1.0.2
	 */
	public static String doGet(String url) throws IOException {
		return doGet(url, "");
	}
	
	/**
	 * @param url
	 * @throws IOException <p>
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     所有请求共享一个httpclient
	 *                     </p>
	 * @since: 1.0.2
	 */
	public static String doGet(String url, String params) throws IOException {
		return invokeGet(url, params);
	}
	
	/**
	 * @param url
	 * @param params        请求参数
	 * @param encodeCharset 参数编码格式
	 * @return
	 * @throws IOException <p>
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     post request,所有请求共享一个httpclient
	 *                     </p>
	 * @since: 1.0.2
	 */
	public static String doGet(String url, Map<String, String> params) throws IOException {
		if (params != null && !params.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> pair : params.entrySet()) {
				sb.append(pair.getKey() + "=" + pair.getValue() + "&");
			}
			sb.deleteCharAt(sb.length() - 1);
			return doGet(url, sb.toString());
		}
		return doGet(url, "");
	}
	
	
	/**
	 * @param url        url
	 * @param param      请求参数
	 * @param httpClient httpclient
	 * @return
	 * @throws IOException <p>
	 *                     调用httpget
	 *                     响应数据根据Server端返回的content-type自动进行转码,如果server端没有返回charset,则使用ISO-8859-1
	 *                     </p>
	 * @since: 1.0.2
	 */
	private static String invokeGet(String url, String param) throws IOException {
		if (param != null && !param.isEmpty()) {
			url += (param.startsWith("?") ? "" : "?") + param;
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			return client.execute(httpGet, new BasicResponseHandler());
		} catch (Exception e) {
			//取消请求
			httpGet.abort();
			throw new IOException("httpclint 请求失败，url=" + url, e);
		} finally {
			httpGet.releaseConnection();
		}
	}
	
}
