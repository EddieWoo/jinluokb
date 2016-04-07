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

    /** ��С���ű���*/
    float minScaleR = 1.0f;
    /** ������ű���*/
    static final float MAX_SCALE = 10f;

    /** ��ʼ״̬*/
    static final int NONE = 0;
    /** �϶�*/
    static final int DRAG = 1;
    /** ����*/
    static final int ZOOM = 2;
    
    /** ��ǰģʽ*/
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;
	private String url=" ";

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ��ȡͼƬ��Դ
		setContentView(R.layout.photoviewer);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yingyang);
		
		 Bundle bundle=this.getIntent().getExtras(); 
	    	 url=bundle.getString("urlsss");
		
          imgView = (ImageView) findViewById(R.id.imageView1);// ��ȡ�ؼ�
          
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
//        //��ʾͼƬ������  
//        DisplayImageOptions options = new DisplayImageOptions.Builder()  
//                .showImageOnLoading(R.drawable.loadingpic)  
//                .showImageOnFail(R.drawable.loadingpic)  
//                .cacheInMemory(true)  
//                .cacheOnDisk(true)  
//                .bitmapConfig(Bitmap.Config.RGB_565)  
//                .build();  
//          
//        ImageLoader.getInstance().displayImage(url, imgView, options);
        
        
        imgView.setOnTouchListener(this);// ���ô�������
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ�ֱ���
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
     * ��������
     */
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // ���㰴��
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        // ���㰴��
        case MotionEvent.ACTION_POINTER_DOWN:
            dist = spacing(event);
            // �����������������10�����ж�Ϊ���ģʽ
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
     * ���������С���ű������Զ�����
     */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
            	//Log.d("", "��ǰ���ż���:"+p[0]+",��С���ż���:"+minScaleR);
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
            	//Log.d("", "��ǰ���ż���:"+p[0]+",������ż���:"+MAX_SCALE);
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**
     * ��С���ű��������Ϊ100%
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
     * �����������
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
            // ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
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
     * ����ľ���
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * ������е�
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    
  
}