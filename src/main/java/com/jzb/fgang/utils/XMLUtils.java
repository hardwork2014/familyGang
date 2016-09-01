package com.jzb.fgang.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Xml工具类
 * 
 * @author XUYANG104
 * 
 */
public class XMLUtils {
	private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

	/**
	 * 创建一个新的Document
	 * 
	 * @return Document
	 */
	public static Document buildDocument() {
		return DocumentHelper.createDocument();
	}

	/**
	 * 从XML字符串构建 dom4j document 对象
	 * 
	 * @param xmlString
	 *            String XML字符串
	 * @return Document
	 */
	public static Document buildDocFromXML(String xmlString) {
		try {
			return DocumentHelper.parseText(xmlString);
		} catch (DocumentException ex) {
			logger.error("XMLUtils buildStirngToDocument error:" + xmlString,
					ex);
			return null;
		}
	}

	/**
	 * 从文件构建dom4j document 对象
	 * 
	 * @param fileName
	 *            String 文件名(包含路径)
	 * @return Document
	 */
	public static Document buildDocFromFile(String fileName) {
		try {
			SAXReader sb = new SAXReader();
			return sb.read(new File(fileName));
		} catch (Exception ex) {
			logger.error(
					"XMLUtils build document from " + fileName + "error =", ex);
			return null;
		}
	}

	/**
	 * 从输入流构建dom4j document对象
	 * 
	 * @param in
	 *            InputStream
	 * @return Document
	 */
	public static Document buildDocFromInputStream(InputStream in) {
		try {
			SAXReader sb = new SAXReader();
			return sb.read(in);
		} catch (Exception ex) {
			logger.error("XMLUtils build document from inputStream error:", ex);
			return null;
		}
	}

	/**
	 * 从Reader流构建dom4j document对象
	 * 
	 * @param rd
	 *            Reader
	 * @return Document
	 */
	public static Document buildDocFromReader(Reader rd) {
		try {
			SAXReader sb = new SAXReader();
			return sb.read(rd);
		} catch (Exception ex) {
			logger.error("XMLUtils build document from Reader error:", ex);
			return null;
		}
	}

	/**
	 * 从URL流构建dom4j document对象
	 * 
	 * @param url
	 *            URL
	 * @return Document
	 */
	public static Document buildDocFromURL(URL url) {
		try {
			SAXReader sb = new SAXReader();
			return sb.read(url);
		} catch (Exception ex) {
			logger.error("XMLUtils build document from url error:", ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parserXML(String xml) {
		ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
		decoder.close();
		return (T) decoder.readObject();
	}

	public static <T> String formatXML(T entity) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(out));
		encoder.writeObject(entity);
		encoder.close();
		return out.toString();
	}

	/**
	 * 从request里面获取xml并封装成map
	 * 
	 * @return
	 */
	public static Map<String, String> paseXmlFromRequest(
			HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		
		Document document = XMLUtils.buildDocFromInputStream(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}
	
	/**
	 * 从request里面获取xml并封装成map
	 * 
	 * @return
	 */
	public static SortedMap<String, String> paseXmlToSortedMapFromRequest(
			HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		SortedMap<String, String> map= new TreeMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		
		Document document = XMLUtils.buildDocFromInputStream(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}
}
