package com.example.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.bean.Address;
import com.example.bean.Cart;
import com.example.bean.CartItem;
import com.example.bean.Customer;
import com.example.bean.Product;
import com.example.bean.Stock;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 联网请求服务器相关操作的工具类
 * 
 * @author Administrator
 *
 */
public class WebServerUtil {
	// 请求服务器的ip地址和端口号和web项目名
	private static final String URL_PREFIX = "http://192.168.1.103:8888/petDog";

	// 获取连接对象，并返回
	public static HttpURLConnection openConnection(String urlPath)
			throws Exception {
		URL url = new URL(urlPath);
		// 创建连接对象
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置连接
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		return connection;
	}

	// 关闭连接
	public static void closeConnection(HttpURLConnection connection) {
		if (connection != null) {
			connection.disconnect();
		}
	}

	/**
	 * 发送密码和用户名到服务器，返回信息
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static Customer doLogin(final String customerNum,
			final String password) {

		HttpURLConnection connection = null;
		String message = null;
		try {
			String urlPath = URL_PREFIX + "/doLogin";
			connection = WebServerUtil.openConnection(urlPath);
			// 连接
			connection.setRequestMethod("POST");
			connection.connect();
			// 获取输出流，传输数据
			OutputStream out = connection.getOutputStream();
			String data = "customerNum=" + customerNum + "&password="
					+ password;
			out.write(data.getBytes("utf-8"));
			out.flush();
			// 获取响应码
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				InputStream in = connection.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = in.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				message = bos.toString();
				in.close();
				bos.close();
			}
			// 关闭连接
			WebServerUtil.closeConnection(connection);
		} catch (Exception e) {
			WebServerUtil.closeConnection(connection);
			e.printStackTrace();
		}

		Customer customer = new Gson().fromJson(message,
				new TypeToken<Customer>() {
				}.getType());

		return customer;
	}

	// 发送注册消息到服务器
	public static void sendRegisterToServer(Customer customer) throws Exception {
		String urlPath = URL_PREFIX + "/register";
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		// 连接
		connection.setRequestMethod("POST");
		connection.connect();
		// 获取输出流，传输数据
		OutputStream out = connection.getOutputStream();
		String data = "loginNum=" + customer.getLoginNum() + "&customerName="
				+ customer.getCustomerName() + "&password="
				+ customer.getPassword() + "&email=" + customer.getEmail()
				+ "&phonenum=" + customer.getPhonenum();
		out.write(data.getBytes("utf-8"));
		out.flush();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			connection.getInputStream().close();
		}
		// 关闭连接
		WebServerUtil.closeConnection(connection);
	}

	/**
	 * 根据条件联网请求服务器获取所有产品信息，并以json字符串的格式返回
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String requestToDate(String productName, Integer categoryId)
			throws Exception {
		String result = null;
//		String urlPath = URL_PREFIX + "/front_product?productName="
//				+ productName + "&categoryId=" + categoryId;
		String urlPath = URL_PREFIX + "/front_product";
		// 连接
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		connection.setRequestMethod("POST");

		connection.connect();
		// 获取输出流，传输数据
		OutputStream out = connection.getOutputStream();
		String data = "productName=" + productName + "&categoryId="+categoryId;
		out.write(data.getBytes("utf-8"));
		out.flush();
		// 获取响应码
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			// 流读写
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			result = bos.toString();
			// Log.e("TAG", result);
			// 关闭流
			in.close();
			bos.close();
		}
		// 关闭连接
		connection.disconnect();
		return result;
	}

	/**
	 * 请求服务器获取图片资源，并返回bitmap对象
	 * 
	 * @param cacheMap
	 * @param imagePath
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getBitmapFromServer(Map<String, Bitmap> cacheMap,
			String imagePath, Context context) throws Exception {
		Bitmap bitmap = null;
		imagePath = URL_PREFIX + "/" + imagePath;
		HttpURLConnection connection = WebServerUtil.openConnection(imagePath);
		// 连接
		connection.connect();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			// 获取输入流
			InputStream in = connection.getInputStream();
			// 将输入流压缩成bitmap对象
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap != null) {
				// 缓存到一级缓存中
				String noPrefixPath = "images/"
						+ imagePath.substring(imagePath.lastIndexOf("/") + 1);
				cacheMap.put(noPrefixPath, bitmap);
				// 缓存到二级缓存中,压缩成jpg格式到SD卡中
				// Log.e("TAG", "compress="+imagePath);
				String fileName = imagePath.substring(imagePath
						.lastIndexOf("/") + 1);
				String filePath = context.getExternalFilesDir(null)
						.getAbsolutePath() + "/" + fileName;
				Log.e("TAG", "Sd卡路径：=" + filePath);
				// Log.e("TAG",
				// "Sd2卡路径：="+Environment.getExternalStorageDirectory().getAbsolutePath());

				bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
						filePath));
			}
		}

		return bitmap;
	}

	/**
	 * 提交订单信息到服务器
	 * 
	 * @param cart
	 * @throws Exception
	 */
	public static MSG submitOrderToServer(Cart cart) throws Exception {
		String result = null;
		String json = new Gson().toJson(cart);
		Log.e("TAG", "json数据：=" + json);

		String urlPath = URL_PREFIX + "/makeOrder";
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/json;charset=utf-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		// 连接
		connection.connect();
		OutputStream out = connection.getOutputStream();
		// 传输数据
		BufferedOutputStream bo = new BufferedOutputStream(out);
		bo.write(json.getBytes("utf-8"));
		bo.flush();
		out.close();
		bo.close();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			InputStream in = connection.getInputStream();
			// 接收数据
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			result = bos.toString();
			// Log.e("TAG", result);
			// 关闭流
			in.close();
			bos.close();
		}
		MSG msg = new Gson().fromJson(result, MSG.class);
		Log.e("TAG", "返回值=" + msg.toString());
		Log.e("TAG", "返回值123=" + msg.getExtend().get("resultMessage"));
		return msg;
	}

	/**
	 * 发送地址信息到服务器,返回地址主键id
	 * 
	 * @param name
	 * @param location
	 * @param cellphone
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public static int addAddress(String name, String location,
			String cellphone, int customerSid) throws Exception {

		int addressId = 0;
		String urlPath = URL_PREFIX + "/addAddress";
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		connection.setRequestMethod("POST");
		// 连接
		connection.connect();
		OutputStream out = connection.getOutputStream();
		// 传输数据
		String data = "name=" + name + "&location=" + location + "&cellphone="
				+ cellphone + "&customerSid=" + customerSid;
		BufferedOutputStream bo = new BufferedOutputStream(out);
		bo.write(data.getBytes("utf-8"));
		bo.flush();
		out.close();
		bo.close();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			InputStream in = connection.getInputStream();
			// 接收数据
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			addressId = Integer.parseInt(bos.toString());
			Log.e("TAG", addressId + "");
			// 关闭流
			in.close();
			bos.close();
		}
		// 关闭连接
		WebServerUtil.closeConnection(connection);
		return addressId;
	}

	/**
	 * 根据客户id获取所有地址信息
	 * 
	 * @param customerSid
	 * @return
	 * @throws Exception
	 */
	public static String getAllAddress(int customerSid) throws Exception {
		String result = null;
		String urlPath = URL_PREFIX + "/getAddress?customerSid=" + customerSid;
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		// 连接
		connection.connect();
		// 获取响应码
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			// 流读写
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			result = bos.toString();
			// Log.e("TAG", result);
			// 关闭流
			in.close();
			bos.close();
		}
		// 关闭连接
		WebServerUtil.closeConnection(connection);
		return result;
	}

	/**
	 * 发送编辑更新过后的地址信息到服务器
	 * 
	 * @param name
	 * @param location
	 * @param cellphone
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public static void editAddress(int locationId, String name,
			String location, String cellphone, int customerSid)
			throws Exception {
		String urlPath = URL_PREFIX + "/editAddress";
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		connection.setRequestMethod("POST");
		// 连接
		connection.connect();
		OutputStream out = connection.getOutputStream();
		// 传输数据
		String data = "locationId=" + locationId + "&name=" + name
				+ "&location=" + location + "&cellphone=" + cellphone
				+ "&customerSid=" + customerSid;
		BufferedOutputStream bo = new BufferedOutputStream(out);
		bo.write(data.getBytes("utf-8"));
		bo.flush();
		out.close();
		bo.close();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			connection.getInputStream().close();
		}
		// 关闭连接
		WebServerUtil.closeConnection(connection);
	}

	/**
	 * 根据id获取删除地址信息
	 * 
	 * @param locationId
	 * @throws Exception
	 */
	public static void deleteAddress(int locationId) throws Exception {
		String urlPath = URL_PREFIX + "/deleteAddress?locationId=" + locationId;
		HttpURLConnection connection = WebServerUtil.openConnection(urlPath);
		// 连接
		connection.setRequestMethod("GET");
		connection.connect();
		// 传输数据
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			connection.getInputStream().close();
		}
		// 关闭连接
		WebServerUtil.closeConnection(connection);
	}

}
