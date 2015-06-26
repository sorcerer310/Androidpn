package com.bsu.system.tool;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
//import org.json.JSONException;
import org.json.JSONObject;

public class JSONMsg {
	private static Map<Integer,String> infome = new HashMap<Integer,String>();
	static{
		infome.put(1001, "登陆信息验证失败!");
		infome.put(1002, "注册失败");
		infome.put(1003, "未取出功能数据");
		infome.put(1004, "不能获得分区数据");
		infome.put(1005, "获得聊天数据异常");
		infome.put(1006, "发送聊天数据异常");
		infome.put(1007, "排行榜数据异常");
		infome.put(1008, "提交排行榜数据异常");
		infome.put(1009, "请求对手的userid不存在");
		infome.put(1010, "修改昵称异常");
		infome.put(1011, "写入2进制数据失败");
		infome.put(1012, "写入2进制数据成功,但更新数据的userid失败");
		infome.put(1013, "查询2进制数据失败");
		infome.put(1014, "获得投诉电话数据失败");
		infome.put(1015, "用户尚未填写昵称");
		infome.put(1016, "名次未上升");
		
		infome.put(1111, "发生异常");
		infome.put(2222, "请求缺少参数");
		
		
		
		infome.put(3001, "登陆成功");
		infome.put(3002, "注册成功");
		infome.put(3003, "更新操作成功");
		infome.put(3004, "删除操作成功");
		infome.put(3005,"发送聊天数据成功");
		infome.put(3006, "挑战名次数据更新成功");
		infome.put(3007, "修改昵称成功");
		infome.put(3008, "写入2进制数据成功");
		
		infome.put(3020, "数据权限检查通过");
		infome.put(3021, "获得unitid数据");
	}

	/**
	 * 根据信息编号返回对应信息的json格式数据
	 * @param no		信息编号详见infome内容
	 * @return				转换为json数据的信息
	 */
	public static String info(Integer no) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("_no", no);
			jo.put("_msg", infome.get(no));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo.toString();
	}
	/**
	 * 根据信息编号返回对应信息的json格式数据,并附带额外信息
	 * @param no		信息编号见informe内容
	 * @param em		带入的额外信息.
	 * @return				转换为json数据信息
	 */
	public static String info(Integer no,String em)
	{
		JSONObject jo = new JSONObject();
		try{
			jo.put("_no", no);
			jo.put("_msg", infome.get(no));
			jo.put("_extramsg", em);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return jo.toString();
	}
}
