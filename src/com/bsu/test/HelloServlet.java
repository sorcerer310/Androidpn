package com.bsu.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/helloservlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		//response.getWriter().print("hello BsuGameServer001");

		//c.setValue("h=aaa");
//		Cookie c = new Cookie("m","bbb");
		//response.addCookie(new Cookie("m","bbb"));
		//response.addCookie(new Cookie("n","aaa"));
		//response.addCookie(new Cookie("x","ccc"));
		//request.getSession().setAttribute("hello", "world");
//		DataSource ds = null;
//		try{
//			Connection conn = DB.getConnection();
//			Statement stmt = conn.createStatement();
//			String sql = "select * from city";
//			ResultSet rs = stmt.executeQuery(sql);  
//			while(rs.next()){
//				response.getWriter().print("name: "+rs.getString(1));
//			}
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1/*");
		//response.setContentType("text/javascript; charset=utf-8");
		String json = "[{\"id\":1,\"text\":\"My Documents\"},{\"id\":2,\"text\":\"fc3\"},{\"id\":3,\"text\":\"fc4\"}]";
		
		response.getWriter().print(json);
//    },{  
//        "text":"Books",  
//        "state":"open",  
//        "attributes":{  
//            "url":"/demo/book/abc",  
//            "price":100  
//        },  
//        "children":[{  
//            "text":"PhotoShop",  
//            "checked":true  
//        },{  
//            "id": 8,  
//            "text":"Sub Bookds",  
//            "state":"closed"  
//        }]  
//    }]  
//},{  
//    "text":"Languages",  
//    "state":"closed",  
//    "children":[{  
//        "text":"Java"  
//    },{  
//        "text":"C#"  
//    }]  
//}]  ";
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().println("BsuGameServer001 post");
//		String[] p = request.getParameterValues("no");
//		response.getWriter().println("no:"+p[0]); 
		
		//String json = "[{\"id\":1,\"text\":\"Folder1\",\"iconCls\":\"icon-save\"}]";
		//String json = "[{\"id\": 8,\"text\":\"Sub Bookds\"}]";
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1/*");
		//response.setContentType("text/javascript; charset=utf-8");
		String json = "[{\"id\":1,\"text\":\"My Documents\"},{\"id\":2,\"text\":\"fc3\"},{\"id\":3,\"text\":\"fc4\"}]";
		
		 
		response.getWriter().print(json);
	}

}
