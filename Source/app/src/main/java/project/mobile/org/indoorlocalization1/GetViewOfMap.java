package project.mobile.org.indoorlocalization1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class GetViewOfMap extends ImageView {
    private Paint p = new Paint();
    final float ref_width = 1920;
    final float ref_height = 1104;
    private float height;
    private float width;

    public GetViewOfMap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaint();
    }

    public GetViewOfMap(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        super.onSizeChanged(w, h, old_w, old_h);

        width = (float)w;
        height = (float)h;
        Log.d("fruit","value of w: "+ w);
        Log.d("fruit", "value of h: " + h);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minMapSize = getResources().getDimensionPixelSize(R.dimen.min_map_size);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(minMapSize + getPaddingLeft() + getPaddingRight(),
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(minMapSize + getPaddingTop() + getPaddingBottom(),
                                heightMeasureSpec)));
    }

    public void initPaint(){
        p.setColor(Color.BLUE);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = FindOnMap.x;
        float y = FindOnMap.y;
        Log.d("X","Value of x: " +x);
        Log.d("Y","Value of y: " +y);
        float scale_x = width/ref_width;
        float scale_y = height/ref_height;
        Log.d("fruit","value of scale_x: "+ scale_x);
        Log.d("fruit","value of scale_y: "+ scale_y);
        x*= scale_x;
        y*= scale_y;
        Log.d("fruit","value of x: "+ x);
        Log.d("fruit","value of y: "+ y);
        canvas.drawCircle(x, y, 10, p);
        //canvas.drawPoint(x, y, p);

    }

}
