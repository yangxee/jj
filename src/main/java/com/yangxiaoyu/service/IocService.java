package com.yangxiaoyu.service;

import com.yangxiaoyu.bean.IocDao;

public class IocService implements IocInterfei{
    private IocDao iocDao;  //创建一个接口.
    public void setIocDao(IocDao iocDao) {
        this.iocDao = iocDao;
    }
    public IocDao getIocDao() {
        return iocDao;
    }
    public void sayhello() {
        iocDao.sayhello();//调用接口方法
    }
}
