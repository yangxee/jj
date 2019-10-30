package com.yangxiaoyu.ClassPathXml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory{
    //定义map集合来存放bean.xml里的bean的id跟其对应的实例化对象
    //<bean id="i" class="com.hck.dao.impl.IocDaoImpl"/>
    //那么类似的存放bean.put("i",new IocDaoImpl());这样子.
    Map<String, Object> beans=new HashMap<String,Object>();
    public ClassPathXmlApplicationContext(String xmlPath){
        try {
            //创建SAXBuilder对象解析文档
            SAXBuilder saxBuilder = new SAXBuilder();
            //解析build里的参数是一个文件路径.
            Document document = saxBuilder.build(xmlPath);
            //document.getRootElement().getChildren("bean")获取所有<bean>标签内容
            List elements = document.getRootElement().getChildren("bean");
            //遍历<bean>对象
            for (int i = 0; i < elements.size(); i++) {
                //获取第一个<bean>标签elements.get(0);
                Element element = (Element) elements.get(i);
                //获取<bean>标签里的<id>属性，
                //<bean id="i" class="com.hck.dao.impl.IocDaoImpl"/>
                //即String beanName="i";
                String beanName = element.getAttributeValue("id");
                //同上String clazz="com.hck.dao.impl.IocDaoImpl";
                String clazz = element.getAttributeValue("class");
                //加载类对象并且实例化.Object object=new IocDaoImpl();
                Object object = Class.forName(clazz).newInstance();//object是IocDaoServiceImpl
                //将他们添加在map集合里,后面可以根据beanName直接获取到实例化对象.
                beans.put(beanName, object);
                //遍历<bean>标签下的<property>字标签.
                //第一个标签没有字标签所以直接跳过.已第二个为例子
                //<bean id="iocService" class="com.hck.service.impl.IocDaoServiceImpl">
                //<property name="iocDao" ref="i"></property></bean>
                List elements2 = element.getChildren("property");
                for (int j = 0; j < elements2.size(); j++) {
                    //此处我们将获得<property name="iocDao" ref="i"></property></bean>
                    Element element2 = (Element) elements2.get(j);
                    //相当于String propertyName="iocDao";
                    String propertyName = element2.getAttributeValue("name");
                    //相当于String refBean="i";
                    String refBean = element2.getAttributeValue("ref");
                    //相当于String propertyName="IocDao";
                    //目的是为了得到一个方法的名字setIocDao,用于反射调用
                    propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    //这里的methodName="setIocDao";
                    String methodName = "set" + propertyName;
                    //获取Map集合里Key="i"的值;i对应的是IocDaoImpl的实例化对象
                    //相当于 Object object2 =IocDaoImpl;
                    Object object2 = beans.get(refBean);
                    //获取IocDaoServiceImpl方法里的setIocDao方法.
                    //第一个方法是方法名，第二个参数是方法的参数类型.
                    Method method = object.getClass().getDeclaredMethod(methodName,
                            object2.getClass().getInterfaces());
                    //调用方法，并传入参数，完成依赖注入.
                    method.invoke(object, object2);
                }
            }
            //      String beanName=document.getElementById(id).attributes().get("class");
            //      Object object=Class.forName(beanName).newInstance();
            //      return object;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    /**
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return beans.get(beanName);
    }




}
