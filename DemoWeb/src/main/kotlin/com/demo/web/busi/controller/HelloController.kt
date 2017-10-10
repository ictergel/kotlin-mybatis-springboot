package com.demo.web.busi.controller

import com.demo.base.entity.BaseVO
import com.demo.common.service.CommonDBService
import com.demo.web.busi.entity.UserVO
import com.demo.web.busi.service.HelloService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(val helloService: HelloService, val commonDBService: CommonDBService){
    private val logger = LoggerFactory.getLogger(this.javaClass)

//    @CrossOrigin
    @GetMapping("/imageCode")
    fun helloSpringBoot():String{
        logger.info("===================================================")
        logger.info("===================================================")
        logger.info("===================================================")
        logger.info("===================================================")
        logger.info("===================================================")
        return helloService.getHello()
    }

    @GetMapping("/hello2")
    fun helloSpringBoot2():List<BaseVO>{
        logger.info("===================================================")
        return commonDBService.selectEntities(UserVO())
    }

    @GetMapping("/hello3")
    fun helloSpringBoot3():String{
        logger.info("===================================================")
        return commonDBService.selectTotalCount(UserVO()).toString()
    }
}