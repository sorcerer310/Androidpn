package com.bsu.commport;

import com.bsu.commport.CommPortInstance;
import com.bsu.commport.SerialReaderListener;

import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

/**
 * 串口读取类
 * @author fengchong
 *
 */
public class SerialReader{
	private SerialPort serialPort;
	private InputStream inputStream;
	private Thread readThread;
	private SerialReaderListener listener = null;										//监听对象
	
	public SerialReader(SerialPort sport){
		try{
			serialPort = sport; 
			inputStream = serialPort.getInputStream();									//从串口获得输入流
			serialPort.addEventListener(new SerialPortEventListener(){					//为串口增加事件监听
				@Override
				public void serialEvent(SerialPortEvent event) {
					switch(event.getEventType()) {
			        case SerialPortEvent.BI:
			        case SerialPortEvent.OE:
			        case SerialPortEvent.FE:
			        case SerialPortEvent.PE:
			        case SerialPortEvent.CD:
			        case SerialPortEvent.CTS:
			        case SerialPortEvent.DSR:
			        case SerialPortEvent.RI:
			        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			            break;
			        case SerialPortEvent.DATA_AVAILABLE:								//当有可用数据时
			            byte[] readBuffer = new byte[256];								//初始化256字节数组

			            try {
			                while (inputStream.available() > 0) {
			                    int numBytes = inputStream.read(readBuffer);
			                }
//			                System.out.println(new String(readBuffer));
			                listener.readCompleted(new String(readBuffer));				//通知外部命令读取完成
			                listener.readCompleted(byteArrayToInt(readBuffer));			//通知外部命令读取完成
			                listener.readCompleted(readBuffer[0]);						//通知外部命令读取一个字节完成
			                
			            } catch (IOException e) {
			            	e.printStackTrace();
			            }
			            break;
			        }
				}
			});
			
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(9600,
					SerialPort.DATABITS_7,
					SerialPort.STOPBITS_2,
					SerialPort.PARITY_EVEN);
			
			readThread = new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						Thread.sleep(20000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			});
			readThread.start();
		}catch(IOException e){
			e.printStackTrace();
		}catch(TooManyListenersException e){
			e.printStackTrace();
		}catch(UnsupportedCommOperationException e){
			e.printStackTrace();
		}
	}
	/**
	 * 设置串口读取的监听器
	 * @param l		从外部设置进来的监听器对象
	 */
	public void setSerialReaderListener(SerialReaderListener l){
		listener = l;
	}
	/**
	 * 字节数组转int型
	 * @param b		字节数组数据
	 * @return		返回int型数据
	 */
	private int byteArrayToInt(byte[] b) {  
	    return   b[3] & 0xFF |  
	            (b[2] & 0xFF) << 8 |  
	            (b[1] & 0xFF) << 16 |  
	            (b[0] & 0xFF) << 24;

	}  
	
	public static void main(String[] args){
		CommPortInstance cp = CommPortInstance.getInstance();
		cp.initCommPort("COM2");
		cp.getSerialReader().setSerialReaderListener(new SerialReaderListener(){
			@Override
			public void readCompleted(String command) {
				System.out.println("============read command:"+command);
			}

			@Override
			public void readCompleted(int command) {
				// TODO Auto-generated method stub
				System.out.println("=============read command:"+command);
			}

			@Override
			public void readCompleted(byte command) {
				if(command==1)
					System.out.println("=============read command:"+command);
			}});
	}
}
