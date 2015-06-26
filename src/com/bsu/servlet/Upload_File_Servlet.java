package com.bsu.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.db.BlobOperate;
import com.bsu.system.parser.JSONParser;
import com.bsu.system.tool.JSONMsg;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class Upload_File_Servlet
 */
@WebServlet("/upload_file_servlet")
public class Upload_File_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload_File_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String optype = request.getParameter("type").toString();
        if(optype.equals("read")){
            try {
                readBlobData(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if(optype.equals("write")){
            try {
                writeBlobData(request, response);
            } catch (Exception ex) {
            	U.p(response, JSONMsg.info(1111, ex.getMessage()));
                ex.printStackTrace();
            }
        }
        else{
        	U.p(response, JSONMsg.info(1011));
        }
	}
	/**
	 * 从数据库中读取blob数据
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 */
	private void readBlobData(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //判断参数id是否正确
        if(request.getParameter("id")==null){
            U.p(response, JSONMsg.info(1012));
            return;
        }
        String id = request.getParameter("id").toString();
        String fname = "blob"+id+".dat";
       if(request.getParameter("fname")!=null)
        	fname = request.getParameter("fname").toString();
       fname = new String(fname.getBytes("gb2312"), "ISO_8859_1");  

        BlobOperate bo = new BlobOperate();
        byte[] bytes = bo.queryBlob("sysblob", "blobdata", Integer.parseInt(id));//获得2进制数据字节数组
        response.setContentType("application/octer-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+fname);
        response.getOutputStream().write(bytes);
    }
	/**
	 * 向数据库写数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void writeBlobData(HttpServletRequest request,HttpServletResponse response) throws Exception{
        PrintWriter out = response.getWriter();                                 //获得输出对象
        String contentType = request.getContentType();                    //内容类型
        String boundary = "";                                                   		//分隔符
        String[] fields = null;

        if(contentType==null){
            U.p(response, JSONMsg.info(1013));
            return;
        }

        int pos = contentType.indexOf("boundary=");
        if(pos!=-1)
        {
            pos+="boundary=".length();
            boundary="--"+contentType.substring(pos);
        }
        else{
            U.p(response, JSONMsg.info(1014));
            return;
        }

        int dataCount = request.getContentLength();                        //获得数据流长度
        byte[] b = new byte[dataCount];                                         //定义数据长度

        //获得输入数据流
        DataInputStream in = new DataInputStream(request.getInputStream());
        in.readFully(b);
        in.close();
        String reqContent = new String(b);
        //根据分隔符拆分字符串
        fields = reqContent.split(boundary);
        for(int i=0;i<fields.length;i++){
            //如果为空或空字符串跳过该次，继续循环
            if(fields[i].equals("") || fields[i] == null)
                continue;
            String fieldname = U.r_gm("name=([^;\r\n]+)?",fields[i]);
            String fieldfile = U.r_gm("filename=(.+)[;|\r\n]",fields[i]);
            String[] field = fields[i].split("\r\n\r\n");                      //内容拆分成头数据与主体数据
            //当fieldfile不为空时，获得文件数据
            if(!fieldfile.equals("")){
                int bytespos = U.byteIndexOf(b,field[0].getBytes(),0);             //post头数据在字节数组中位置
                bytespos+=field[0].getBytes().length;                            //加上自post头自身长度
                bytespos+="\r\n\r\n".getBytes().length;                         //加上两个换行字符

                //长度为总长度去掉头部数据、与结束标志长度
                int bytelength = b.length
                        - bytespos                                              //减掉头数据长度
                        - "\r\n".getBytes().length                              //去掉回车长度
                        - boundary.getBytes().length                            //去掉分割符长度
                        - "--\r\n".getBytes().length;                           //去掉分割符结束符号
                byte[] desBytes = new byte[bytelength];                         //字节长度2是结尾处\r\n长度
                System.arraycopy(b, bytespos, desBytes, 0, desBytes.length);    //copy出字节数组

                BlobOperate bo = new BlobOperate();
                long id = -1;
                try{
                    id = bo.writeData("sysblob", "blobdata", desBytes);             //将2进制数据导入
                }
                catch(Exception e){
                    throw e;
                }
                //插入成功后返回该blob数据的id
                U.p(response, JSONParser.parseSimpleJson("id", Long.toString(id)));
            }
            else{
                /**
                 * do something......
                 */
            }
        }
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
        String optype = req.getParameter("type").toString();
        if(optype.equals("read")){
            try {
                readBlobData(req, resp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
        	U.p(resp, JSONMsg.info(1011));
        }
	}

}
