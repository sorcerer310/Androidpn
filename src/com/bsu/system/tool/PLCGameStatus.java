package com.bsu.system.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PLCGameStatus {
	private boolean PLC_STATUS_BED = false;				//躺床播放状态
	private boolean PLC_STATUS_DRAWER = false;			//抽屉播放状态
	private boolean PLC_STATUS_KNOCK_DOOR = false;		//敲门播放状态
	private boolean PLC_STATUS_WATERING = false;			//浇花播放状态
	private boolean PLC_STATUS_PLAY_VIDEO = false;		//播放视频状态
	
//	private final static String url = "http://127.0.0.1:8080/notification.do";
//	private final static String user = "daa0dc1a3fac4ab5898e496ecac386a9";
	private String url,user;
	
//	private static StringBuffer sb = new StringBuffer();
	
	private static PLCGameStatus instance = null;
	private PLCGameStatus(){
		url = U.properties.getProperty("androidpnUrl");
		user = U.properties.getProperty("androidpnUser");	//此处一定要指定用户
	}
	public static PLCGameStatus getInstance(){
		if(instance==null)
			instance = new PLCGameStatus();
		return instance;
	}
	
	
	public synchronized void set_PLC_STATUS_BED(boolean b){
		PLC_STATUS_BED = b;

		try {
			sendPostRequestByForm(url, setParams(user, "", U.properties.getProperty("UrlParamV001")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean get_PLC_STATUS_BED(){
		return PLC_STATUS_BED;
	}
	
	
	public synchronized void set_PLC_STATUS_DRAWER(boolean b){
		PLC_STATUS_DRAWER = b;
		try {
			sendPostRequestByForm(url, setParams(user, "", U.properties.getProperty("UrlParamV002")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean get_PLC_STATUS_DRAWER(){
		return PLC_STATUS_DRAWER;
	}
	
	
	public synchronized void set_PLC_STATUS_KNOCK_DOOR(boolean b){
		PLC_STATUS_KNOCK_DOOR = b;
		try {
			sendPostRequestByForm(url, setParams(user, "", U.properties.getProperty("UrlParamV003")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public boolean get_PLC_STATUS_KNOCK_DOOR(){
		return PLC_STATUS_KNOCK_DOOR;
	}
	
	
	
	public synchronized void set_PLC_STATUS_WATERING(boolean b){
		PLC_STATUS_WATERING = b;
		try {
			sendPostRequestByForm(url, setParams(user, "", U.properties.getProperty("UrlParamV004")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public boolean get_PLC_STATUS_WATERING(){
		return PLC_STATUS_WATERING;
	}
	
	/**
	 * 设置播放视频的状态PLC_STATU_PLAY_VIDEO状态为true.
	 * 此处只设置状态,设置完状态用于识别是否在指定视频观看结束后发送指令到PLC
	 * 只有当PLC_STATUS_PLAY_VIDEO为true时才会发送指令到plc,让女鬼复位玩家成功脱出
	 * @param b
	 */
	public synchronized void set_PLC_STATUS_PLAY_VIDEO(boolean b){
		PLC_STATUS_PLAY_VIDEO = b;
//		try {
//			sendPostRequestByForm(url, setParams(user, "", U.properties.getProperty("UrlParamV005")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
	
	public boolean get_PLC_STATUS_PLAY_VIDEO(){
		return PLC_STATUS_PLAY_VIDEO;
	}
	
	/**
	 * 模拟post方式发送表单数据
	 * @param path			url路径
	 * @param params		url参数
	 * @return				返回服务器响应的数据
	 * @throws Exception
	 */
    private byte[] sendPostRequestByForm(String path, String params) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// 提交模式
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        byte[] bypes = params.toString().getBytes();
        conn.getOutputStream().write(bypes);// 输入参数
        InputStream inStream=conn.getInputStream();
        return readInputStream(inStream);
    }
    
    /**
     * 从数据流中读取数据
     * @param inStream		输入数据流
     * @return				返回数据流数据
     * @throws Exception
     */
    private byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
    
    /**
     * 设置请求androidpn的参数
     * @param user	要发送的手机的唯一编码
     * @param msg	发送的消息
     * @param uri	发送的uri,如没有uri可填入空字符串
     * @return
     */
    private String setParams(String user,String msg,String uri){
    	uri = (uri==null)?"":uri;								//如果uri为null设置uri为空字符串
		StringBuffer sb = new StringBuffer();
		sb.append("action=send&broadcast=N&username=").append(user)
			.append("&title=title&message=").append(msg)
			.append("&uri=").append(uri);
		return sb.toString();	
    }
}
