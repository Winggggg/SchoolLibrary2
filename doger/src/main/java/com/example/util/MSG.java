package com.example.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个返回json字符串信息的通用工具
 * @author Administrator
 *
 */
public class MSG {
	//100表示成功 200表示失败
	private int statusCode; 
	//自定义返回信息
	private String message;
	//用来放对象
	private Map<String,Object>extend=new HashMap<String,Object>();
	
	//响应码为100时返回成功信息
	public static MSG success(){
		MSG result=new MSG();
		result.setStatusCode(100);
		result.setMessage("处理成功了");
		return result;
	}
	//响应码为200时返回失败信息
	public static MSG fail(){
		MSG result=new MSG();
		result.setStatusCode(200);
		result.setMessage("处理失败了");
		return result;
	}
	
	//定义添加对象的方法
	public MSG add(String key,Object value){
		this.extend.put(key, value);
		return this;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getExtend() {
		return extend;
	}
	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	@Override
	public String toString() {
		return "MSG [statusCode=" + statusCode + ", message=" + message
				+ ", extend=" + extend + "]";
	}
	
	
	
}
