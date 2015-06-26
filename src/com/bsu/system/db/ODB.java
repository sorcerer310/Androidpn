package com.bsu.system.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bsu.system.db.data.*;
import com.sun.rowset.CachedRowSetImpl;
/**
 * 数据库操作类,用于数据库的各类操作,查询、新建、更新、删除. 操作类约定以O开头
 * @author fengchong
 *
 */
public class ODB {
	
	/**
	 * 查询方法,用于不带条件的sql语句查询
	 * @param sql	要查询的sql语句
	 * @return			返回CachedRowSetImpl形式的数据
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public static CachedRowSetImpl query(String sql) throws SQLException, ParseException {
		return query(sql,new ArrayList<ColumnData>());
	}
	
	public static CachedRowSetImpl query(String sql,String[] cs) throws SQLException,ParseException{
		Connection conn = null;
		ResultSet rs = null;
		CachedRowSetImpl crs = null;
		try{
			conn = DB.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			
		}catch(SQLException e){
			throw e;
		}finally{
			if(rs!=null)
				rs.close();
			if(conn!=null)
				conn.close();
		}
		return crs;
	}
	
	/**
	 * 查询方法,用于带条件的sql语句查询
	 * @param sql	要查询的sql语句
	 * @return			返回CachedRowSetImpl形式的数据
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public static CachedRowSetImpl query(String sql,List<ColumnData> ldata) throws SQLException, ParseException{
		Connection conn = null;
		ResultSet rs = null;
		CachedRowSetImpl crs = null;
		try{
			conn = DB.getConnection();											//获得数据库连接
			PreparedStatement stmt = conn.prepareStatement(sql);	//建立预编译Statement
			setParams(stmt,ldata);													//为sql设置参数
			rs = stmt.executeQuery();												//查询获得rowset
			crs = new CachedRowSetImpl();							
			crs.populate(rs);																//将rowset数据放入CachedRowSetImpl中
		
		}catch(SQLException e){
			throw e;
		}
		finally{
			if(rs!=null)																//关闭rs
				rs.close();
			if(conn!=null)															//关闭连接
				conn.close();					
		}
		return crs;
	}
	/**
	 * 更新操作,带入连接对象,用于控制事务的提交或回滚
	 * @param sql		sql语句
	 * @param ldata		参数列表
	 * @param conn		数据库连接
	 * @return			返回成功操作的条数
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public  static int update(String sql,List<ColumnData> ldata,Connection conn) throws ParseException, SQLException{
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			setParams(stmt,ldata);
			return stmt.executeUpdate();													//查询获得rowset
		}catch(SQLException e){
			throw e;
		}
		finally{
//			if(conn!=null)															//关闭连接
//				conn.close();					
		}
	}
	
	/**
	 * 更新操作,不带连接对象,更新后直接提交
	 * @param sql		sql语句
	 * @param ldata		参数列表
	 * @return				成功返回操作的条数
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public  static int update(String sql,List<ColumnData> ldata) throws ParseException, SQLException{
		Connection conn = DB.getConnection();
		conn.setAutoCommit(true);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			setParams(stmt,ldata);
			return stmt.executeUpdate();													//查询获得rowset
		}catch(SQLException e){
			throw e;
		}
		finally{
			if(conn!=null)															//关闭连接
				conn.close();					
		}
	}
	
	/**
	 * 插入新建数据
	 * @param sql		要执行的sql
	 * @param ldata		带入的参数
	 * @return			返回刚刚新建数据的id
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static int insert(String sql,List<ColumnData> ldata) throws ParseException,SQLException{
		Connection conn = null;
		try{
			conn = DB.getConnection();
			//此处一定要这么设置,否则不会返回新生成的id
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); 
			setParams(stmt,ldata);
			stmt.execute();
			
			ResultSet rs = stmt.getGeneratedKeys();  
			int id=0;//保存生成的ID  
			if (rs != null&&rs.next()) {  
			    id=rs.getInt(1);
			}  
			return id;
		}
		finally{
			if(conn!=null)															//关闭连接
				conn.close();					
		}
	}
	
	/**
	 * 通过表名获得列类型
	 * @param tname			要查询的表的名称
	 * @return						返回hashmap形式的表名与列类型的值
	 * @throws Exception
	 */
//	public static HashMap<String,Integer> getMetaData(String tname) throws Exception{
//		HashMap<String,Integer> reth = new HashMap<String,Integer>();
//		//ResultSet rs = DB.metaData.getColumns("BSUGameServer001", "hrmsbsu", tname, "%");
//		return DB.tableInfo.get(tname);
//	}
	
	/**
	 * 为PreparedStatement设置参数.
	 * @param ps	PreparedStatement对象
	 * @param ld		参数对象
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	private  static void setParams(PreparedStatement ps,List<ColumnData> ld) throws ParseException, SQLException{
		int count = ld.size();
		//循环为SQL赋参数
		for(int i=1;i<=count;i++)
		{
			ColumnData cd = ld.get(i-1);
			if(cd.getType()==Types.DATE)
			{ 
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null;
				//如果日期发生异常,设置为0000年00月00日
				try{
					d = format.parse(cd.getValue());
					ps.setTimestamp(i, new java.sql.Timestamp(d.getTime()));
				}catch (ParseException e){
					ps.setTimestamp(i, null);
				}

			}
			else if(cd.getType()==Types.TIME || cd.getType()==Types.TIMESTAMP)
			{
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = format.parse(cd.getValue());
				ps.setTimestamp(i, new java.sql.Timestamp(d.getTime()));
			}
			else if(cd.getType() == Types.VARCHAR || cd.getType()==Types.CHAR)
				ps.setString(i, cd.getValue());
			else if(cd.getType() == Types.INTEGER)
				ps.setInt(i, Integer.parseInt(cd.getValue()));
			else if(cd.getType() == Types.DOUBLE)
				ps.setDouble(i, Double.parseDouble(cd.getValue()));
			else if(cd.getType() == Types.FLOAT)
				ps.setFloat(i, Float.parseFloat(cd.getValue()));
			else if(cd.getType() == Types.REAL)
				ps.setFloat(i, Float.parseFloat(cd.getValue()));
		}
	}
	
	/**
	 * 通知执行一系列数据库事务
	 * @param dbte						数据库事务事件对象
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void moreTransactionOperate(DBTransactionEvent dbte)throws SQLException,Exception{
		Connection conn = null;
		try{
			conn = DB.getNoCommitConnection();
			dbte.operate(conn);
			conn.commit();
		}catch(SQLException sqle){
			if(conn!=null)
				conn.rollback();
			throw sqle;
		}catch(Exception e){
			if(conn!=null)
				conn.rollback();
			throw e;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
	
}
