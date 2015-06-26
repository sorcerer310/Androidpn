package com.bsu.commport;
/**
 * 串口读取监听器，用来监听串口是否读到了可用数据
 * @author fengchong
 *
 */
public interface SerialReaderListener {
	public void readCompleted(String command);
	public void readCompleted(int command);
	public void readCompleted(byte command);
}
