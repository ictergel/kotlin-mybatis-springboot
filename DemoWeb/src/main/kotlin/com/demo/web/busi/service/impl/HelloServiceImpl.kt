package com.demo.web.busi.service.impl

import com.demo.web.busi.entity.UserVO
import com.demo.web.busi.service.HelloService
import com.demo.common.dao.CommonDBDao
import org.springframework.stereotype.Service

@Service
class  HelloServiceImpl(val commonDBDao: CommonDBDao) : HelloService {
    override
    fun getHello():String{
        return commonDBDao.selectTotalCount(UserVO()).toString()
    }
}