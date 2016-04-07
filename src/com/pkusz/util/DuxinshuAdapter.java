package com.pkusz.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.example.jinluokb.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class DuxinshuAdapter extends BaseAdapter {

	private List<DuxinshuItem> list;
	private ListView listview;
	public static LruCache<String, BitmapDrawable> mImageCache;

	public DuxinshuAdapter(List<DuxinshuItem> list) {
		super();
		this.list = list;

		int maxCache = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxCache/5;
		mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
			@Override
			protected int sizeOf(String key, BitmapDrawable value) {
				return value.getBitmap().getByteCount();
			}
		};
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (listview == null) {
			listview = (ListView) parent;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.duxinshu_item, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.summary = (TextView) convertView.findViewById(R.id.summary);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DuxinshuItem news = list.get(position);
		holder.title.setText(news.getTitle());
		holder.summary.setText(news.getSummary());
		holder.iv.setTag(news.getImageUrl());
       
		String pic=news.getImageUrl();
		int n=pic.split("/").length;
		String path=Environment.getExternalStorageDirectory()+"/jinluo/"+pic.split("/")[n-1];
 
		if (getDiskBitmap(path)!=null) {
			System.out.println("本地找到"+news.getImageUrl());
			Drawable drawable = new BitmapDrawable(getDiskBitmap(path));
			holder.iv.setImageDrawable(drawable);
		} else {
			ImageTask it = new ImageTask();
			it.execute(news.getImageUrl());
			}
		return convertView;
	}
	

	private Bitmap getDiskBitmap(String pathString)
	{
		Bitmap bitmap = null;
		try
		{
			File file = new File(pathString);
			if(file.exists())
			{
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		
		
		return bitmap;
	}




	class ViewHolder {
		ImageView iv;
		TextView title, summary;
	}

	class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

		private String imageUrl;

		@Override
		protected BitmapDrawable doInBackground(String... params) {
			imageUrl = params[0];
			Bitmap bitmap = downloadImage();

			BitmapDrawable db = new BitmapDrawable(listview.getResources(),
					bitmap);
			if (mImageCache.get(imageUrl) == null) {
				mImageCache.put(imageUrl, db);
			}
			
			return db;
			
		}

		@Override
		protected void onPostExecute(BitmapDrawable result) {
			ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
			if (iv != null & result != null) {
				iv.setImageDrawable(result);
			}
		}

		/**
		 * 
		 * @return
		 */
		private Bitmap downloadImage() {
			HttpURLConnection con = null;
 
			Resources res=listview.getResources();
			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.loading); 
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(10 * 1000);
				bitmap = BitmapFactory.decodeStream(con.getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
//			 bitmap = BitmapFactory.decodeResource(res, R.drawable.yingyang); 
			saveBitmap(bitmap,imageUrl);
			return bitmap;
		}
		
		public  Bitmap drawableToBitmap(Drawable drawable) {
			// 取 drawable 的长宽
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();

			// 取 drawable 的颜色格式
			Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565;
			// 建立对应 bitmap
			Bitmap bitmap = Bitmap.createBitmap(w, h, config);
			// 建立对应 bitmap 的画布
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, w, h);
			// 把 drawable 内容画到画布中
			drawable.draw(canvas);
			return bitmap;
		}
		
		public void saveBitmap(Bitmap bm,String picName) {
			
			int n=picName.split("/").length;
			
			  String picName1=picName.split("/")[n-1];
			  File file = new File(Environment.getExternalStorageDirectory()+"/jinluo/");  
		        if(!file.exists())  
		            file.mkdirs();  
		            try {  
		            	File myCaptureFile = new File(Environment.getExternalStorageDirectory()+"/jinluo/" + picName1);  
		                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile)); 
		                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
		                bos.flush();
		                bos.close();  
		            } catch (Exception e) {  
		                        e.printStackTrace();  
		        }  

			 }
	}
}
