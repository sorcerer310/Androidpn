package com.bsu.system.db;

import java.sql.*;
import java.util.HashMap;

import javax.sql.*;
import javax.naming.*;

/**
 * 管理数据库连接
 * @author fengchong
 *
 */
public class DB {
//	private static String url = "java:comp/env/jdbc/mysql";
	private static String url = "jdbc:sqlite:data.db";
	private static DataSource ds = null;
	public static DatabaseMetaData metaData = null;
	public static String catalog = null;
	//保存指定表的列信息
	public static HashMap<String,HashMap<String,Integer>> tableInfo = new HashMap<String,HashMap<String,Integer>>();
	//在静态代码中实现数据源头
	static{
		try{
			InitialContext ctx = new InitialContext();
			ds = (DataSource)ctx.lookup(url);
			Connection conn = ds.getConnection();
			metaData = conn.getMetaData();
			catalog  =  conn.getCatalog();
			
			ResultSet rs = metaData.getTables(catalog, "hrmsbsu", "%", new String[]{"TABLE"});
			while(rs.next()){
				addTableInfo(rs.getString("TABLE_NAME"));	
			}
			
			tableInfo.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 检索指定表的所有列类型
	 * @param tname
	 */
	private static void addTableInfo(String tname){
		 try {
			ResultSet rs = metaData.getColumns(catalog, "hrmsbsu", tname, "%");
			tableInfo.put(tname, new HashMap<String,Integer>());
			while(rs.next()){
				String cn = rs.getString("COLUMN_NAME");						//列名
				tableInfo.get(tname).put(cn,rs.getInt("DATA_TYPE"));			//增加指定表的列的类型
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得数据库连接
	 * @return 返回一个可以操作数据的连接
	 * @throws SQLException 发生SQL异常时向外抛出
	 */
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	/**
	 * 获得数据库连接,并设置不自动提交事务数据
	 * @return		返回一个可以操作数据库的连接,但该连接不会自动提交事务性操作
	 * @throws SQLException
	 */
	public static Connection getNoCommitConnection() throws SQLException{
		Connection conn = ds.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}
}
