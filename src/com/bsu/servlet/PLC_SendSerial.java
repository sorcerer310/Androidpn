package com.bsu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.commport.CommPortInstance;
import com.bsu.commport.SerialWriter;
import com.bsu.system.tool.PLCGameStatus;
import com.bsu.system.tool.U;

/**
 * 用于向PLC发送串口数据
 * Servlet implementation class PLC_SendSerial
 */
//@WebServlet("/PLC_SendSerial")
public class PLC_SendSerial extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CommPortInstance cpi = null;
    private SerialWriter sw = null;
    
    private final byte SEND_PLC_WATCH_VIDEO_YES = 1;
    private final byte SEND_PLC_WATCH_VIDEO_NO = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PLC_SendSerial() {
        super();
        cpi = CommPortInstance.getInstance();
        sw = cpi.getSerialWriter();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		if(sw==null){
			U.p(response, "comm port init fail");
			return;
		}
		
		//只有PLC_STATUS_PLAY_VIDEO为true的时候才会向plc发送指令命令女鬼回位
		if(PLCGameStatus.getInstance().get_PLC_STATUS_PLAY_VIDEO()){
			String watch = U.getRS(request, "watch");
			byte[] bytes;
			if(watch.equals("yes"))
				bytes = new byte[]{SEND_PLC_WATCH_VIDEO_YES};
			else
				bytes = new byte[]{SEND_PLC_WATCH_VIDEO_NO};
			System.out.println("===========================send"+new String(bytes));
			sw.writeCommand(bytes);
			U.p(response,"data send success");
		}else
			U.p(response,"data send status is not PLC_STATUS_PLAY_VIDEO");
	}

	
	
}
