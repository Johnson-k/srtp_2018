package com.briup.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.client.Client;
import com.briup.client.Gather;
import com.briup.logger.Log;
import com.briup.server.DBStore;
import com.briup.server.Server;

public class ConfigurationImpl implements Configuration {
	/**
	 * map 键作为每个模块名字  值每个模块对应的对象
	 */
	private static Map<String, ModuleInit> map = new HashMap<>();
	static {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.
					read("src/com/briup/config/config.xml");
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements();
			for(Element e:elements) {
				Properties properties = new Properties();
				
				//拿到每个模块的全限定名
				String value = e.attributeValue("class");
				Class<?> clazz = Class.forName(value);
				//创建改模块对应的实例对象
				Object object = clazz.newInstance();
				if(object instanceof ModuleInit) {
					ModuleInit module = (ModuleInit) object;
					map.put(e.getName(), module);
					@SuppressWarnings("unchecked")
					List<Element> elements2 = e.elements();
					for(Element e2:elements2) {
						properties.setProperty(e2.getName(), e2.getText());
					}
					module.init(properties);
				}
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public Client getClient() {
		return (Client) map.get("client");
	}

	@Override
	public Server getServer() {
		return (Server) map.get("server");
	}

	@Override
	public Gather getGather() {
		return (Gather)map.get("gather");
	}

	@Override
	public DBStore getDBStore() {
		return (DBStore)map.get("dbStore");
	}

	@Override
	public Log getLog() {
		return (Log)map.get("log");
	}
	
}
