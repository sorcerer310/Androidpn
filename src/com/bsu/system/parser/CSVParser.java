package com.bsu.system.parser;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import com.sun.rowset.CachedRowSetImpl;

public class CSVParser {
	protected static java.text.SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/**
	 * 将数据库返回的CachedRowSetImpl数据转化为csv数据
	 * @param rs							CachedRowSetImpl数据
	 * @return								转换完的csv数据
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static String parseCsv(CachedRowSetImpl rs,HashMap<String,String> colinfo) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();														//获得结果集的结构数据
		StringBuffer sbcsv = new StringBuffer();																//建立csv数据字符串对象
		int cc = rsmd.getColumnCount();																			//列数量
		//增加列数据,如果需要中文列,在数据库列备注中添加中文描述
//		for(int i=1;i<=cc;i++){
//			String colname = rsmd.getColumnName(i);
//			sbcsv.append(colname).append(",");
//		}
//		sbcsv.replace(sbcsv.length()-1, sbcsv.length(), "\n");												//将最后一个逗号转换为换行符	
		
		for(int i=1;i<=cc;i++){
			String colname = rsmd.getColumnName(i);														//获得数据库列名
			//如果该列名不存在带入的列数据中,继续下一列
			if(!colinfo.containsKey(colname))
				continue;
			sbcsv.append(colinfo.get(colname)).append(",");												//记录下该列的中文备注
		}
		sbcsv.replace(sbcsv.length()-1, sbcsv.length(), "\n");												//将最后一个逗号转换为换行符
		
		
		//收集数据
		rs.beforeFirst();
		while(rs.next()){
			for(int i=1;i<=cc;i++){
				String colname = rsmd.getColumnName(i);													//获得第i位置的列名
				//如果列为id不展示给用户
				if(colname.equals("id") || !colinfo.containsKey(colname))
					continue;
				int type = rsmd.getColumnType(i);																//获得第i位置的列类型
				if(type==java.sql.Types.TIMESTAMP){			
					sbcsv.append(dateTime2Str(rs.getTimestamp(colname))).append(",");
				}
				else if(type== java.sql.Types.DATE){
					sbcsv.append(date2Str(rs.getTimestamp(colname))).append(",");
				}
				else{
					sbcsv.append(rs.getString(colname)).append(",");
				}
			}
			sbcsv.replace(sbcsv.length()-1, sbcsv.length(), "\n");												//将最后一个逗号转换为换行符
		}
		rs.beforeFirst();																									//恢复结果集的游标状态
		return sbcsv.toString();
	}

	/**
	 * 将Timestamp类型数据转化为日期时间格式字符串类型
	 * @param ts
	 * @return
	 * @throws SQLException
	 */
	protected static String dateTime2Str(Timestamp ts) throws SQLException {
	    String value = "";
	    if (ts != null) {
	        value = dateTimeFormat.format(ts);
	    }
	    return value;
	}
	/**
	 * 将Timestamp类型数据转化为日期格式字符串类型
	 * @param ts
	 * @return
	 * @throws SQLException
	 */
	protected static String date2Str(Timestamp ts) throws SQLException{
		String value = "";
		if(ts!=null)
			value = dateFormat.format(ts);
		return value;
	}
}
