package com.bsu.system.parser;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.rowset.CachedRowSetImpl;
/**
 * 解吸数据库返回的CachedrowSetImpl对象数据返回json格式数据
 * @author fengchong
 *
 */
public class JSONParser {
	protected static java.text.SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/**
	 * 将key数据与value数据解析成只有一个字段的简单json数据
	 * @param k		key
	 * @param v		value
	 * @return
	 * @throws JSONException
	 */
	public static String parseSimpleJson(String k,String v) throws JSONException{
		JSONObject jo = new JSONObject();
		jo.put(k, v);
		return jo.toString();
	}
	/**
	 * 将key数据与value数据解析成只有一个字段的简单json数据
	 * @param k		
	 * @param v
	 * @return
	 * @throws JSONException
	 */
	public static String parseSimpleJson(String k,int v)throws JSONException{
		JSONObject jo = new JSONObject();
		jo.put(k,v);
		return jo.toString();
	}
	
	/**
	 * 将数据库返回的CachedRowSetImpl数据转化为json数据
	 * @param rs							CachedRowSetImpl数据
	 * @return								转换完的json数据
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static String parseJson(CachedRowSetImpl rs) throws SQLException, JSONException
	{
		ResultSetMetaData rsmd = rs.getMetaData();															//获得结果集的结构数据
		JSONArray ja = new JSONArray();																		//json数组对象
		rs.beforeFirst();
		while(rs.next()){
			JSONObject jo = new JSONObject();																//建立json数据对象
			int cc = rsmd.getColumnCount();
			for(int i=1;i<=cc;i++){
				String colname = rsmd.getColumnName(i);														//获得第i位置的列名
				int type = rsmd.getColumnType(i);															//获得第i位置的列类型
				if(type==java.sql.Types.TIMESTAMP){			
					jo.put(colname, dateTime2Str(rs.getTimestamp(colname)));								//如果类型为时间类型单独处理数据
				}
				else if(type== java.sql.Types.DATE){
					jo.put(colname, date2Str(rs.getTimestamp(colname)));
				}
				else{
					jo.put(colname, rs.getString(colname));													//如果为其他类型返回字符串
				}
			}
			ja.put(jo);																										//将json数据对象放入json数组
		}
		rs.beforeFirst();																									//恢复结果集的游标状态
		return ja.toString();																							//返回json格式数据
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