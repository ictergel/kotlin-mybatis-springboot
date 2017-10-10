package com.demo.web.utils


import com.demo.common.consts.CommonConsts
import java.io.IOException
import net.sf.json.JSONObject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object RedirectHelper {

    fun isAjaxSubmit(request: HttpServletRequest): Boolean = CommonConsts.XML_HTTP_REQUEST.equals(
            request.getHeader(CommonConsts.X_REQUESTED_WITH), ignoreCase = true)

    /**
     * 方法writeResponse功能描述
     * @Author demo
     * @Description
     * @Date 10:12 2017/6/12
     * @param isAjax 是否AJAX异步
     * @param isLogin 是否跳转到登陆地址
     * @param redirectUrl 登陆地址
     * @return
     * @version 1.0
     */
    @Throws(IOException::class)
    fun writeResponse(isAjax: Boolean, isLogin: Boolean, redirectUrl: String, response: HttpServletResponse) {
        val json = JSONObject()
        json.put("isAjax", isAjax)
        json.put("isLogin", isLogin)
        json.put("redirectUrl", redirectUrl)
        json.put("total", 0)
        json.put("rows", "[]")
        response.contentType = "text/json; charset=utf-8"
        val pw = response.writer
        pw.print(json.toString())
        pw.flush()
        pw.close()
    }

}