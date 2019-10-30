package com.yangxiaoyu;

import com.yangxiaoyu.ClassPathXml.ClassPathXmlApplicationContext;
import com.yangxiaoyu.service.IocService;

public class Ioc {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("src/bean.xml");
        IocService ids=(IocService)applicationContext.getBean("iocService");
        ids.sayhello();
    }
}
