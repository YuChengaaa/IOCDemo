package com.cmsz.upay.ioc.context.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;

/**
 * <b>实现类</b><br>
 * 参考spring-framework，加载并解析配置文件，完成对象实例化，实现依赖注入<br>
 * 完成具体的功能，可自己添加需要的属性和方法。<br>
 * 需要完成的功能点大致如下：<br>
 * <ol>
 * 	<li>配置文件加载、解析</li>
 * 	<li>Bean实例化</li>
 * 	<li>属性注入/构造器注入</li>
 * 	<li>依赖、循环依赖</li>
 * 	<li>懒加载</li>
 * 	<li>Bean实例的作用域</li>
 * </ol>
 * @author 
 * 
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {
	
	private Map<String , Object> beans = new HashMap<String, Object>();
	
	public ClassPathXmlApplicationContext() throws Exception {
		this("applicationContext.xml");// 默认加载applicationContext.xml
	}
	
	public ClassPathXmlApplicationContext(String configLocation) throws Exception, Exception {	
		SAXBuilder sb=new SAXBuilder();  
	    Document doc=sb.build(this.getClass().getClassLoader().getResourceAsStream("applicationContext.xml")); //读取xml文件
	    Element root=doc.getRootElement(); //获取文件当中的根节点
	    List list=root.getChildren("bean");//获取所有为关健值为“bean”的节点
	    //获取所有bean中属性值为id的对象，并且将将对象和关健值存入map中
//	    for(int i=0;i<list.size();i++){
	       Element element=(Element)list.get(0);
	       String id=element.getAttributeValue("id");
	       String clazz=element.getAttributeValue("class");
	       Object o = Class.forName(clazz).newInstance();
	       System.out.println(id);
	       System.out.println(clazz);
	       beans.put(id, o);	       
	       for(Element propertyElement : (List<Element>)element.getChildren("property")) {
	    	   String name = propertyElement.getAttributeValue("name"); //属性名
	    	   String price = propertyElement.getAttributeValue("value"); //属性名
	    	   String value = propertyElement.getValue();
	    	   Object beanObject = beans.get(id);//获取对象
	    	   //生成方法名
	    	   String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	    	   System.out.println("method name = " + methodName);   
	    	   if(price == null){
	    		   //利用反射获取相应的set方法
	    		   Method m = o.getClass().getMethod(methodName, value.getClass());
	    		   m.invoke(o, value);
	    	   }else{
	    		   int s = Integer.parseInt(price);
	    		   //利用反射获取相应的set方法
	    		   Method m = o.getClass().getMethod(methodName,int.class);
	    		   m.invoke(o, s);
	    	   }
	       }
	       
	       
	    }  
//	}

	@Override
	public Object getBean(String name) throws BeansException {
		// TODO Auto-generated method stub
		
		return beans.get(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

}
