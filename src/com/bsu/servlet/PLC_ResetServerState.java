package com.bsu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsu.system.tool.PLCGameStatus;
import com.bsu.system.tool.U;

/**
 * Servlet implementation class PLC_ResetServerState
 */
//@WebServlet(description = "重置一些服务器参数，可以通过手机访问重置", urlPatterns = { "/PLC_ResetServerState" })
public class PLC_ResetServerState extends HttpServlet {


	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PLC_ResetServerState() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PLCGameStatus.getInstance().set_PLC_STATUS_PLAY_VIDEO(false);
		U.p(response, "PLC_ResetServerState is successed");
		this.getServletContext().log("=================PLC_ResetServerState is successed");
	}
}
