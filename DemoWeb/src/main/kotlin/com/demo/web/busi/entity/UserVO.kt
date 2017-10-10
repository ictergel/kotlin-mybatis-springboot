package com.demo.web.busi.entity

import com.demo.base.entity.BaseVO
import java.io.Serializable

//用户实体
data class UserVO(
        var id: Long? = null,
        var email: String? = null,
        var roleId: Long? = null,
        var status: Long? = null,
        var remark: String? = null,
        var provCode: Long? = null,
        var cityCode: Long? = null,
        var roleName: String? = null,
        var userName: String? = null,
        var realName: String? = null,
        var password: String? = null,
        var provName: String? = null,
        var cityName: String? = null,
        var userLevel: Long? = null,
        var telephone: String? = null,
        var parentIdStr: String? = null,
        var provCityName: String? = null,
        var userLevelName: String? = null) : BaseVO(), Serializable