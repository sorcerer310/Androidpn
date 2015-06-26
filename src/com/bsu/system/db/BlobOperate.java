/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bsu.system.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.bsu.system.db.data.ColumnData;
import com.bsu.system.tool.U;

/**
 *
 * @author Administrator
 */
public class BlobOperate {
    //public Connection conn = null;
    public BlobOperate(){}

    /**
     *  读取blob数据
     * @param table     	表名
     * @param column    字段名
     * @return          		返回保存数据的行id
     * @throws Exception
     */
    public long writeData(String table,String column,byte[] bytes) throws Exception { 
        Connection conn = DB.getConnection(); 
        PreparedStatement ps = null; 
        InputStream in = null;
        try { 
                String sql = "insert into "+table+" ("+column+") values (?)"; 
                ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS); 
                //设置二进制参数 
                in = new BufferedInputStream(new ByteArrayInputStream(bytes)); 
                ps.setBinaryStream(1, in, (int)bytes.length); 
                ps.execute();

    			ResultSet rs = ps.getGeneratedKeys();  
    			int id=-1;//保存生成的ID  
    			if (rs != null&&rs.next()) {  
    			    id=rs.getInt(1);
    			}  
    			return id;
                
                
        }finally { 
                if(conn!=null)
                	conn.close();
                if(in != null)
                	in.close();
        } 
    } 
    /**
     * 对指定id的数据更新2进制数据字段
     * @param table			要更新的表名
     * @param column			要更新的列名		
     * @param bytes			要更新的2进制字节数组
     * @param idvalue			指定id值，用于确定更新的行
     * @return
     * @throws Exception
     */
    public long updateData(String table,String column,byte[] bytes,String idvalue) throws Exception { 
        Connection conn = DB.getConnection(); 
        PreparedStatement ps = null; 
        InputStream in = null;
        try { 
        	String sql = "update "+table+" set "+column+"=? where id=?";
            ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS); 
            //设置二进制参数 
            in = new BufferedInputStream(new ByteArrayInputStream(bytes)); 
            ps.setBinaryStream(1, in, (int)bytes.length); 													//设置第一个参数为2进制数据流
            ps.setInt(2, Integer.parseInt(idvalue));															//设置第二个参数为条件id
            ps.execute();

			int c = ps.getUpdateCount();  
			return c;
        }finally { 
                if(conn!=null)
                	conn.close();
                if(in != null)
                	in.close();
        } 
    } 
    
    /**
     * 根据id获得blob数据
     * @param table
     * @param column
     * @param id
     * @throws Exception
     */
    public byte[] queryBlob(String table,String column,long id) throws Exception { 
        Connection conn = null;
        PreparedStatement ps = null; 
        ResultSet rs = null; 
        InputStream instream = null;
        
        byte[] retbytes = null;
        
        try { 
        		conn = DB.getConnection(); 
                String sql = "select "+column+" from "+table+" where id = ?"; 
                ps = conn.prepareStatement(sql);
                ps.setLong(1, id);
                rs = ps.executeQuery(); 
                if (rs.next()) { 
                	java.sql.Blob blob = (java.sql.Blob) rs.getBlob(column);
	                retbytes = new byte[(int)blob.length()];
	                instream = new BufferedInputStream(blob.getBinaryStream());
		            int pos = 0;
	                int c = 0;
	                while((c=instream.read())!=-1){
	                      retbytes[pos] = (byte)c;
	                      pos++;
	                 }
                }
        }
        finally{
        	if(conn != null)
        		conn.close();
        }
        return retbytes;
    } 
    
    public static void main(String[] args){
        //提交数据测试
        try{
            BlobOperate blob = new BlobOperate();
            byte[] bytes = {1,2,3,4,5,6,7,8,9,0};
            blob.writeData("sysblob", "blobdata",  bytes);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        //查询数据测试
//        try{
//            BlobOperate blob = new BlobOperate();
//            byte[] b = blob.queryBlob("sysblob", "blobdata", 842674499);
//            for(int i=0;i<b.length;i++){
//                System.out.print(String.valueOf(b[i]));
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
