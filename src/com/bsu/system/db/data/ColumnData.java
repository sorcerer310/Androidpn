package com.bsu.system.db.data;

import java.sql.Types;

/**
 * 保存列相关的数据
 * @author fengchong
 *
 */
public class ColumnData {
	private String name = "";											//列名
	//private ColumnType type = ColumnType.STRING;		//列数据类型
	private int type = Types.VARCHAR;								//列数据类型
	private String value = "";											//列值
	
	/**
	 * 构造一个ColumnData对象
	 * @param n
	 * @param t
	 * @param v
	 */
	public ColumnData(String n,int t,String v)
	{
		name = n;
		type = t;
		value = v;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
