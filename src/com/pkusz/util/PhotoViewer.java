package com.pkusz.util;

import java.io.File;

import com.example.jinluokb.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;



public class PhotoViewer extends Activity implements OnTouchListener {

	private static final String TAG = "PhotoViewer";
	public static final int RESULT_CODE_NOFOUND = 200;
	
	
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    DisplayMetrics dm;
    ImageView imgView;
    Bitmap bitmap;

    /** 最小缩放比例*/
    float minScaleR = 1.0f;
    /** 最大缩放比例*/
    static final float MAX_SCALE = 10f;

    /** 初始状态*/
    static final int NONE = 0;
    /** 拖动*/
    static final int DRAG = 1;
    /** 缩放*/
    static final int ZOOM = 2;
    
    /** 当前模式*/
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;
	private String url=" ";

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取图片资源
		setContentView(R.layout.photoviewer);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yingyang);
		
		 Bundle bundle=this.getIntent().getExtras(); 
	    	 url=bundle.getString("urlsss");
		
          imgView = (ImageView) findViewById(R.id.imageView1);// 获取控件
          
          BitmapDrawable dd=DuxinshuAdapter.mImageCache.get(url);
	        if(dd!=null){
	        	 imgView.setBackground(dd);
	        }else{
	        	int n=url.split("/").length;
	  	    	String path=Environment.getExternalStorageDirectory()+"/jinluo/"+url.split("/")[n-1];
		        BitmapDrawable dd1=new BitmapDrawable(getDiskBitmap(path));
	        	imgView.setBackground(dd1);
	        }
          
	      
//		ImageLoaderConfiguration configuration = ImageLoaderConfiguration  
//                .createDefault(this);  
//          
//        //Initialize ImageLoader with configuration.  
//        ImageLoader.getInstance().init(configuration);  
//        
//        final ImageView imgView = (ImageView) findViewById(R.id.imageView1);  
//         
//        //显示图片的配置  
//        DisplayImageOptions options = new DisplayImageOptions.Builder()  
//                .showImageOnLoading(R.drawable.loadingpic)  
//                .showImageOnFail(R.drawable.loadingpic)  
//                .cacheInMemory(true)  
//                .cacheOnDisk(true)  
//                .bitmapConfig(Bitmap.Config.RGB_565)  
//                .build();  
//          
//        ImageLoader.getInstance().displayImage(url, imgView, options);
        
        
        imgView.setOnTouchListener(this);// 设置触屏监听
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
//        minZoom();
        center();
        imgView.setImageMatrix(matrix);
        
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
    public void SureOnClick(View v)
    {
    	
    }
    
    /**
     * 触屏监听
     */
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 主点按下
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        // 副点按下
        case MotionEvent.ACTION_POINTER_DOWN:
            dist = spacing(event);
            // 如果连续两点距离大于10，则判定为多点模式
            if (spacing(event) > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            //savedMatrix.set(matrix);
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - prev.x, event.getY()
                        - prev.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float tScale = newDist / dist;
                    matrix.postScale(tScale, tScale, mid.x, mid.y);
                }
            }
            break;
        }
        imgView.setImageMatrix(matrix);
        CheckView();
        return true;
    }

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
            	//Log.d("", "当前缩放级别:"+p[0]+",最小缩放级别:"+minScaleR);
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
            	//Log.d("", "当前缩放级别:"+p[0]+",最大缩放级别:"+MAX_SCALE);
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**
     * 最小缩放比例，最大为100%
     */
    private void minZoom() {
        minScaleR = Math.min(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR < 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
    }

    private void center() {
        center(true, true);
    }

    /**
     * 横向、纵向居中
     */
    protected void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 两点的中点
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    
  
}