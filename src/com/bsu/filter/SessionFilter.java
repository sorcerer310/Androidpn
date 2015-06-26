package com.bsu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet Filter implementation class SessionFilter
 */
@WebFilter(filterName="SessionFilter",urlPatterns="*")
public class SessionFilter implements Filter {

    /** 
     * Default constructor. 
     */
    public SessionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		//将输出文字设置为utf-8编码
		res.setCharacterEncoding("utf-8");
		//res.setContentType("text/html; charset=utf-8");
		res.setHeader("Cache-Control", "no-cache, must-revalidate");
		
		String uri =req.getRequestURI();
		String ctx_path = req.getContextPath();
		String urlpath = uri.substring(ctx_path.length());

		//如果要验证用户,此句一定要注释掉
		chain.doFilter(request, response);
		//如果为登陆界面不进行session验证直接向下执行并返回
//		if(urlpath.equals("/login") || urlpath.equals("/ugametime") || urlpath.contains("/js/") || urlpath.contains("/css/") || urlpath.contains("/img/")
//				|| urlpath.equals("/register") || urlpath.equals("/fastloginregist") || urlpath.equals("/info_study.html") || urlpath.equals("/info_notice.html") || urlpath.equals("/infostudy")
//				|| urlpath.equals("/gmAddDummyData"))
//		{
//			chain.doFilter(request, response);
//			return;
//		}
//		
//		//如果session为空直接返回1001错误
//		if(req.getSession(false)==null){
//			U.p(res, JSONMsg.info(1001));
//			return;
//		}
//		
//		String sessionid = req.getSession(false).getId();
//		String jsessionid = null;
//		Cookie[] cookies = req.getCookies();
//		//如果cookies不为空，去验证客户端传来的cookie中的sessionid
//		//不相等返回1001
//		if(cookies!=null)
//		{
//        	for (int i = 0; i < cookies.length; i++) 
//	        {            
//	        	Cookie c = cookies[i];            
//	        	if(c.getName().equals("JSESSIONID")){
//	        			jsessionid = c.getValue();
//	        			break;
//	        	}
//	        }
//		}else{
////			U.p(res, JSONMsg.info(1001));
//			RequestDispatcher dis = request.getRequestDispatcher("/login.html");
//			dis.forward(request, response);
//			return;
//		}
//		
//		//判断sessionid与客户端cookieid是否相等
//		if(sessionid.equals(jsessionid))
//			chain.doFilter(request, response);
//		else{
////			U.p(res,JSONMsg.info(1001));
//			RequestDispatcher dis = request.getRequestDispatcher("/login.html");
//			dis.forward(request, response);
//		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
}
