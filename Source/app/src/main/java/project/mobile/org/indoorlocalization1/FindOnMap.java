package project.mobile.org.indoorlocalization1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by ABHISHEK on 31-03-2015.
 */
public class FindOnMap extends Activity{

    private GetViewOfMap mGetViewOfMap;
    private float scale = 1f;
    private ScaleGestureDetector SGD;
    private GestureDetector SL;
    public float width;
    public float height;
    public static float x;
    public static float y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);

        Intent intent= getIntent();

        String loc= intent.getStringExtra("coordinates");
        String[] coordinates= loc.split(",");
        x= Float.parseFloat(coordinates[0]);
        y= Float.parseFloat(coordinates[1]);


        Log.d("location", "Show_Locaton : The coordinates are :" + loc);

        mGetViewOfMap = (GetViewOfMap) findViewById(R.id.fruit);

        SGD = new ScaleGestureDetector(this,new ScaleListener());
        SL = new GestureDetector(this, new ScrollListener());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        SGD.onTouchEvent(ev);
        SL.onTouchEvent(ev);

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));

            if(width*scale >= 720 && height*scale >= 920){
                mGetViewOfMap.setScaleX(scale);
                mGetViewOfMap.setScaleY(scale);
            }
            else
            {scale = 1;
                mGetViewOfMap.setScaleX(scale);
                mGetViewOfMap.setScaleY(scale);

            }

            return true;
        }

    }

    private class ScrollListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){

            if(e2.getPointerCount()==1)
            {float transXdist = e2.getX()-e1.getX();
                float transYdist = e2.getY()-e1.getY();

                mGetViewOfMap.setTranslationX(transXdist);
                mGetViewOfMap.setTranslationY(transYdist);
            }

            return true;
        }

    }

    int[] getImageDetails(GetViewOfMap img){
        int[] result = new int[2];
        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        img.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = img.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        result[0] = actW;
        result[1] = actH;

        return result;

    }
}
