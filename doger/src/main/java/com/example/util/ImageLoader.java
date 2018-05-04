package com.example.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 用来加载并显示图片的一个工具类
 * @author Administrator
 *
 */
public class ImageLoader {

	private Map<String,Bitmap> cacheMap;
	private Context context;
	private int loading_id;
	private int error_id;
	
	public ImageLoader(Map<String, Bitmap> cacheMap, Context context,
			int loading_id, int error_id) {
		super();
		this.cacheMap = cacheMap;
		this.context = context;
		this.loading_id = loading_id;
		this.error_id = error_id;
	}




	/**用来加载并显示图片
	 * @param imageView
	 * @param imagePath
	 */
	public void loadImage(ImageView imageView, String imagePath) {
		imageView.setTag(imagePath);//设置标志，用来再分线程中判断视图是否被复用
		
		//根据URL从一级缓存中取bitmap对象
		Bitmap bitmap=getFromFirstCache(imagePath);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			return;
		}
		//如果一级缓存没有，去二级缓存，sd卡那里找
		bitmap=getFromSecondCache(imagePath);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			//保存到一级缓存中
			cacheMap.put(imagePath, bitmap);
			return;
		}
		//如果一级缓存没有，去三级缓存，服务器
		bitmap=loadimageFromThirdCache(imageView,imagePath);
		
		
	}




	/**在服务器中加载图片
	 * @param imageView
	 * @param imagePath
	 * @return
	 */
	private Bitmap loadimageFromThirdCache(final ImageView imageView, final String imagePath) {
		
		//启动分线程联网得到bitmap对象
		new AsyncTask<Void, Void, Bitmap>() {

			protected void onPreExecute() {
				//主线程显示提示正在加载的图片
				imageView.setImageResource(loading_id);
			};
			
			@Override
			protected Bitmap doInBackground(Void... params) {
				
				Bitmap bitmap=null;
				try {
					//获取最新的需要加载的imagePath
					String newImagePath=(String) imageView.getTag();
					//Log.e("TAG", "7777"+newImagePath);
					//Log.e("TAG", "8888"+imagePath);
					if(newImagePath!=imagePath){
						//证明imageview被复用
						return null;
					}
					bitmap=WebServerUtil.getBitmapFromServer(cacheMap, imagePath, context);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bitmap;
			}
			
			protected void onPostExecute(Bitmap bitmap) {
				//获取最新的需要加载的imagePath
				String newImagePath=(String) imageView.getTag();
//				Log.e("TAG", "777"+newImagePath);
//				Log.e("TAG", "888"+imagePath);
				if(newImagePath!=imagePath){
					//证明imageview被复用,主线程不需要显示
					return ;
				}
				
				if(bitmap!=null){
					imageView.setImageBitmap(bitmap);
					//bitmap.recycle();
				}else{
					imageView.setImageResource(error_id);
				}
			};
		}.execute();
		return null;
	}




	/**在二级缓存中寻找
	 * @param imagePath
	 * @return
	 */
	private Bitmap getFromSecondCache(String imagePath) {
		String fileName=imagePath.substring(imagePath.lastIndexOf("/")+1);
		String filePath=context.getExternalFilesDir(null).getAbsolutePath()+"/"+fileName;
		return BitmapFactory.decodeFile(filePath);
	}




	/**根据URL从一级缓存中取bitmap对象
	 * @param imagePath
	 * @return
	 */
	private Bitmap getFromFirstCache(String imagePath) {
		return cacheMap.get(imagePath);
	}

}
