package com.bsu.servlet;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * 注册用户使用
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		U.p(response, JSONMsg.info(1002, "请使用post方式注册用户"));
	} 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = U.getRS(request, "no");
		String pwd = U.getRS(request, "pwd");
		String gameid = U.getRS(request, "gameid");
		String zoneid = U.getRS(request, "zoneid");
		List<ColumnData> ldata = new ArrayList<ColumnData>();
		ldata.add(new ColumnData("no",Types.VARCHAR,no));							//设置no参数保存用户名
		ldata.add(new ColumnData("pwd",Types.VARCHAR,pwd));						//设置pwd参数保存密码
		ldata.add(new ColumnData("gameid",Types.INTEGER,gameid));				//设置游戏id参数
		ldata.add(new ColumnData("zoneid",Types.INTEGER,zoneid));				//设置游戏分区id参数
		
		try {
			int rc = ODB.update("insert into user (no,pwd,gameid,zoneid) values (?,PASSWORD(?),?,?)", ldata);
			if(rc>0)
				response.getWriter().print(JSONMsg.info(3002));
			else
				response.getWriter().print(JSONMsg.info(1002));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(JSONMsg.info(1002,e.getMessage()));
		} 
	}
}
