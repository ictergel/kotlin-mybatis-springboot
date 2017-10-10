package com.demo.base.entity


/**
 * Author: lfh
 * Version: 1.0
 * Date: 2016/12/30
 * Description: 分页实体
 * Function List:
 */
open class PageEntity(
        var rows: List<BaseVO> = emptyList(),   //返回结果集合
        var total: Long = 0,        //查询结果总数
        var footer: List<BaseVO> = emptyList() //footer总计
)
