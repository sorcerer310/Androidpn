package com.bsu.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bsu.system.db.ODB;
import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.DataHelper;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;
import com.sun.rowset.CachedRowSetImpl;

/**
 * 登陆并自动注册,如果登陆成功则正常登陆,否则返回信息为用户未找到则自动注册该用户
 * 自动登陆注册不需要密码,只需要设备唯一编号既可
 */
@WebServlet(description = "自动登陆注册", urlPatterns = { "/fastloginregist" })
public class FastLoginRegist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FastLoginRegist() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = U.getRS(request, "no");
		String pwd = "ugame123456";
		String gameid = U.getRS(request, "gameid");
		String zoneid = U.getRS(request, "zoneid");
		List<ColumnData> ldata = new ArrayList<ColumnData>();
		//设置查询参数
		ldata.add(new ColumnData("no",Types.VARCHAR,no));
		ldata.add(new ColumnData("pwd",Types.VARCHAR,pwd));
		ldata.add(new ColumnData("gameid",Types.INTEGER,gameid));				//设置游戏id参数
		ldata.add(new ColumnData("zoneid",Types.INTEGER,zoneid));				//设置游戏分区id参数

		HttpSession s =  request.getSession(true);
		CachedRowSetImpl rs = null;
		try {
			rs =  ODB.query("select * from user where no=? and pwd=PASSWORD(?) and gameid=? and zoneid=?", ldata);
			//如果验证不成功则注册，最后执行登陆成功操作
			int id = 0;
			if(!rs.next()){
				id = ODB.insert("insert into user (no,pwd,gameid,zoneid) values (?,PASSWORD(?),?,?)", ldata);
				if(id==0){
					response.getWriter().print(JSONMsg.info(1002));
					return;
				}

				s.setAttribute("no", no);										//将用户名加入session
				s.setAttribute("pwd", pwd);										//将密码加入session
				s.setAttribute("userid", String.valueOf(id));					//获得用户id
				s.setAttribute("nickname", "");									//用户昵称
				s.setAttribute("gameid", gameid);								//游戏id
				s.setAttribute("zoneid", zoneid);								//大区id

			} else{
				id = rs.getInt("id");
				s.setAttribute("no", no);										//将用户名加入session
				s.setAttribute("pwd", pwd);										//将密码加入session
				s.setAttribute("userid", rs.getInt("id"));						//获得用户id
				s.setAttribute("nickname", rs.getString("nickname"));			//用户昵称
				s.setAttribute("gameid", rs.getInt("gameid"));					//游戏id
				s.setAttribute("zoneid", rs.getInt("zoneid"));					//大区id
			}
				
			//session写入成功后,向session中记录下用户现在的名次与成绩
			CachedRowSetImpl rsUser = DataHelper.getInstance().getUserRankData(gameid, zoneid, String.valueOf(rs.getInt("id")), 1);
			int rank = 0;
			int pkvalue = 0;
			rsUser.beforeFirst();
			while(rsUser.next()){
				if(rsUser.getInt("userid")==id){
					rank = rsUser.getInt("rank");
					pkvalue = rsUser.getInt("pkvalue");
					break;
				}
			}
			
			s.setAttribute("rank", rank);
			s.setAttribute("pkvalue", pkvalue);

			response.getWriter().print(JSONMsg.info(3001));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(JSONMsg.info(1001,e.getMessage()));
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
