package com.example.e_burst;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;



public class PowerCurve extends FrameLayout {


    Paint mPaint = new Paint();

    View point1, point2, point3, point4, point5, graph;

    public PowerCurve(Context context) {
        this(context, null);
    }

    public PowerCurve(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PowerCurve(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        point1 = (View) findViewById(R.id.point1);
        point2 = (View) findViewById(R.id.point2);
        point3 = (View) findViewById(R.id.point3);
        point4 = (View) findViewById(R.id.point4);
        point5 = (View) findViewById(R.id.point5);

        point1.setOnLongClickListener(longclicklistener);
        point2.setOnLongClickListener(longclicklistener);
        point3.setOnLongClickListener(longclicklistener);
        point4.setOnLongClickListener(longclicklistener);
        point5.setOnLongClickListener(longclicklistener);

        graph = this;
        graph.setOnDragListener(dragListener);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(10);

        int[] x = new int[5];
        int[] y = new int[5];
        x[0]=(int) point1.getX()+point1.getWidth()/2;
        y[0]=(int) point1.getY()+point1.getHeight()/2;

        x[1]=(int) point2.getX()+point2.getWidth()/2;
        y[1]=(int) point2.getY()+point2.getHeight()/2;

        x[2]=(int) point3.getX()+point3.getWidth()/2;
        y[2]=(int) point3.getY()+point3.getHeight()/2;

        x[3]=(int) point4.getX()+point4.getWidth()/2;
        y[3]=(int) point4.getY()+point4.getHeight()/2;

        x[4]=(int) point5.getX()+point5.getWidth()/2;
        y[4]=(int) point5.getY()+point5.getHeight()/2;

        float[][] spline = splineInterpolation(x,y);


        for (float i=spline[0][4]; i<spline[1][4];i++) {
            float dy = spline[0][0]+spline[0][1]*(i-spline[0][4])+spline[0][2]*((i-spline[0][4])*(i-spline[0][4]))+spline[0][3]*((i-spline[0][4])*(i-spline[0][4])*(i-spline[0][4]));
            canvas.drawPoint(i,dy,mPaint);
        }
        for (float i=spline[1][4]; i<spline[2][4];i++) {
            float dy = spline[1][0]+spline[1][1]*(i-spline[1][4])+spline[1][2]*((i-spline[1][4])*(i-spline[1][4]))+spline[1][3]*((i-spline[1][4])*(i-spline[1][4])*(i-spline[1][4]));
            canvas.drawPoint(i,dy,mPaint);
        }
        for (float i=spline[2][4]; i<spline[3][4];i++) {
            float dy = spline[2][0]+spline[2][1]*(i-spline[2][4])+spline[2][2]*((i-spline[2][4])*(i-spline[2][4]))+spline[2][3]*((i-spline[2][4])*(i-spline[2][4])*(i-spline[2][4]));
            canvas.drawPoint(i,dy,mPaint);
        }
        for (float i=spline[3][4]; i<x[4];i++) {
            float dy = spline[3][0]+spline[3][1]*(i-spline[3][4])+spline[3][2]*((i-spline[3][4])*(i-spline[3][4]))+spline[3][3]*((i-spline[3][4])*(i-spline[3][4])*(i-spline[3][4]));
            canvas.drawPoint(i,dy,mPaint);
        }

    }

    View.OnLongClickListener longclicklistener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new DragShadowBuilder(v);
            v.startDragAndDrop(data,myShadowBuilder,v,0);
            return false;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();
            switch (dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setVisibility(INVISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setVisibility(VISIBLE);
                    break;
                case DragEvent.ACTION_DROP:
                    view.setVisibility(VISIBLE);
                    float dx = event.getX();
                    float dy = event.getY();
                    view.setX(dx-view.getWidth()/2);
                    view.setY(dy-view.getHeight()/2);
                    break;
            }
            return true;
        }
    };

    public float[][] splineInterpolation(int[] x, int[] y) {
        int n = y.length-1;

        //1
        float[] a = new float[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = y[i];
        }
        //2
        float[] b = new float[n];
        float[] d = new float[n];
        //3
        float[] h = new float[n];
        for (int i = 0; i <= n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }
        //4
        float[] alpha = new float[n];
        for (int i = 1; i <= n - 1; i++) {
            alpha[i] = (3 / h[i]) * (a[i + 1] - a[i]) - (3 / h[i - 1]) * (a[i] - a[i - 1]);
        }
        //5
        float[] c = new float[n+1];
        float[] l = new float[n+1];
        float[] u = new float[n+1];
        float[] z = new float[n+1];
        //6
        l[0] = 1;
        u[0] = 0;
        z[0] = 0;
        //7
        for (int i = 1; i <= n - 1; i++) {
            l[i] = 2 * (x[i + 1] - x[i - 1]) - h[i - 1] * u[i - 1];
            u[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / (l[i]);
        }
        //8
        l[n] = 1;
        z[n] = 0;
        c[n] = 0;
        //9
        for (int j = n-1; j >= 0; j--) {
            c[j]=z[j]-u[j]*c[j+1];
            b[j]=(a[j+1]-a[j])/(h[j])-(h[j]*(c[j+1]+2*c[j]))/(3);
            d[j]=(c[j+1]-c[j])/(3*h[j]);
        }
        //10
        float[][] splines = new float[n][5];
        //11
        for (int i = 0; i <= n - 1; i++) {
            splines[i][0]=a[i];
            splines[i][1]=b[i];
            splines[i][2]=c[i];
            splines[i][3]=d[i];
            splines[i][4]=x[i];
        }
        return splines;
    }
}

