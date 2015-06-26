package com.bsu.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.commport.CommPortInstance;
import com.bsu.commport.SerialReaderListener;
import com.bsu.system.tool.PLCGameStatus;
import com.bsu.system.tool.U;

/**
 * 接收串口数据的servlce,该servlte随tomcat启动,并在init函数中初始化串口的初始化操作,只执行一次.
 * 其他servlce需要使用串口时只需要获得CommPortInstance的实例就行,不必再对串口进行初始化
 * Servlet implementation class PLC_ReceiveSerial
 */
//@WebServlet(description = "接收串口数据到程序中", urlPatterns = { "/PLC_ReceiveSerial" },loadOnStartup = 5)
public class PLC_ReceiveSerial extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CommPortInstance cpi = null;
	private PLCGameStatus plcgs = null;
	
	private final byte PLC_RECEIVE_BED_VIDEO = 1;									//躺床传第一个视频到手机
	private final byte PLC_RECEIVE_DRAWER_VIDEO = 2;								//床抽屉触发手机视频
	private final byte PLC_RECEIVE_KNOCK_DOOR_VIDEO = 3;							//敲门触发手机视频								
	private final byte PLC_RECEIVE_FLOWER_VIDEO = 4;								//浇花触发手机视频
	private final byte PLC_RECEIVE_PLAY_VIDEO = 5;								//从plc处接到播放视频的指令
	
	private ServletConfig pconfig;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PLC_ReceiveSerial() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//1:创建properties
		InputStream inputStream = null;
		try {
			System.out.println(getClass().getClassLoader());
			inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			U.properties.load(inputStream);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//2:创建PLCGameStatus实例子
		plcgs = PLCGameStatus.getInstance();
		
		//3:初始化串口监听部分
		System.out.println("PLC_ReceiveSerial is init");
		config.getServletContext().log("======================PLC_ReceiveSerial is init");
		cpi = CommPortInstance.getInstance();
		cpi.initCommPort("COM2");
		if(cpi.getSerialReader()==null){
			System.out.println("comm port init fail");
			config.getServletContext().log("======================PLC_ReceiveSerial comm port init fail");
			return;
		}
		
//		final ServletConfig sc = config;
		pconfig = config;
		cpi.getSerialReader().setSerialReaderListener(new SerialReaderListener(){

			@Override
			public void readCompleted(String command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void readCompleted(int command) {
				// TODO Auto-generated method stub
//				System.out.println(command);
			}

			@Override
			public void readCompleted(byte command) {
				pconfig.getServletContext().log("================comm port readCompleted:"+command);
				switch(command){
				case PLC_RECEIVE_BED_VIDEO:									
					//躺床上视频
					plcgs.set_PLC_STATUS_BED(true);
					break;
				case PLC_RECEIVE_DRAWER_VIDEO:
					//抽屉视频
					plcgs.set_PLC_STATUS_DRAWER(true);
					break;
				case PLC_RECEIVE_KNOCK_DOOR_VIDEO:
					//敲门视频
					plcgs.set_PLC_STATUS_KNOCK_DOOR(true);
					break;
				case PLC_RECEIVE_FLOWER_VIDEO:
					//浇花视频
					plcgs.set_PLC_STATUS_WATERING(true);
					break;
				case PLC_RECEIVE_PLAY_VIDEO:
					//要求播放视频命令
					plcgs.set_PLC_STATUS_PLAY_VIDEO(true);
					break;
				default:
					break;
				}
				System.out.println(command);
			}
		});
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		plcgs.set_PLC_STATUS_BED(true);
		System.out.println("PLC_ReceiveSerial is doGet");
	}

}
