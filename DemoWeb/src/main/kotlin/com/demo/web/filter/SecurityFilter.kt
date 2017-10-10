package com.demo.web.filter

import com.demo.base.entity.BaseVO
import com.demo.web.busi.entity.UserVO
import com.demo.common.service.CommonDBService
import com.demo.web.utils.RedirectHelper
import org.apache.oro.text.perl.Perl5Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.core.annotation.Order
import java.io.IOException
import javax.annotation.Resource
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 过滤器
 * @author demo
 * @version 1.0
 * @date 创建时间：2015年4月16日 下午1:50:16
 * @parameter
 * @return
 */
@Order(1)
//重点
@WebFilter(filterName = "securityFilter", urlPatterns = arrayOf("/*"))
@PropertySource("classpath:config/server.properties")
class SecurityFilter : Filter {

    @Resource
    private lateinit var commonDBService: CommonDBService

    private var filterConfig: FilterConfig? = null

    @Value("\${servlet.url.permissionSkipPattern}")
    private val permissionSkipPattern: String? = null

    private var servletContext: ServletContext? = null

    @Value("\${servlet.loginUrl}")
    private var loginUrl: String? = null

    private var perl: Perl5Util? = null

    init {
        filterConfig = null
    }

    override fun destroy() {
        filterConfig = null
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(_request: ServletRequest, _response: ServletResponse, chain: FilterChain) {
        val request = _request as HttpServletRequest
        val response = _response as HttpServletResponse
        val requestUrl = request.requestURI
        val contextPath = request.contextPath
        updateLoginUrl(contextPath)
        if (perl!!.match(permissionSkipPattern, requestUrl)) {
            chain.doFilter(request, response)
            return
        }
        if (validateUrl(request, response, commonDBService)) {
            return
        }
        chain.doFilter(request, response)
        return
    }

    private fun validateUrl(request: HttpServletRequest, response: HttpServletResponse, commonDBService: CommonDBService):
            Boolean {
        var retFlag = false
        val isAjax = RedirectHelper.isAjaxSubmit(request)
        val userVO = request.session.getAttribute("user") as? UserVO
        if (userVO == null) {
            retFlag = true
            redirectUrl(isAjax, loginUrl ?: request.contextPath + "/login/index", response)
        } else {
            val users:List<BaseVO> = commonDBService.selectEntities(userVO)
            if(users.isNotEmpty()){
                val user = users.first() as UserVO
                if (user.password != userVO.password) {
                    retFlag = true
                    redirectUrl(isAjax, loginUrl ?: request.contextPath + "/login/index", response)
                }
            }
            if (request.requestURI.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 2) {
                retFlag = true
                redirectUrl(false, request.contextPath + "/main/index", response)
            }
        }
        return retFlag
    }

    private fun redirectUrl(isAjax: Boolean, redirectUrl: String, response: HttpServletResponse) {
        if (isAjax) {
            RedirectHelper.writeResponse(true, true, redirectUrl, response)
        } else {
            val out = response.writer
            out.println("<html>")
            out.println("<script>")
            out.println("parent.location.href = '$redirectUrl'")
            out.println("</script>")
            out.println("</html>")
        }
    }

    private fun updateLoginUrl(contextPath: String) {
        if (!(loginUrl ?: "").startsWith(contextPath)) {
            loginUrl = contextPath + loginUrl
        }
    }

    @Throws(ServletException::class)
    override fun init(config: FilterConfig) {
        perl = Perl5Util()
        filterConfig = config
        servletContext = config.servletContext
    }

}