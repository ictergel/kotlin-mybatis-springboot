package com.demo.base.entity

/**
 * Author: lfh
 * Version: 1.0
 * Date: 2016/12/30
 * Description: 分页实体
 * Function List:
 */
open class BaseVO(
        var ids: String? = null,    //批量删除的ID
        var rows: List<BaseVO> = emptyList(),   //返回结果集合
        var total: Long = 0,        //查询结果总数
        var sort: String? = null,   //排序字段
        var order: String = "desc", //排序方式
        var pageSize: Int = 100,    //条数
        var pageNumber: Int = 1,    //页数
        var footer: List<BaseVO> = emptyList(), //footer总计
        var queryEndTime: String? = null, //查询结束时间
        var queryStartTime: String? = null, //查询开始时间
        var exportFlag: Int = 0 //导出标志，0是导出 1是正常查询
)