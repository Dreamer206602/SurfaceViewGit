package com.mx.lb.surfaceviewdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback ,Runnable{

    private SurfaceView surfaceView;
    private Thread thread;
    private boolean running;
    private TextPaint txtPaint;
    private SurfaceHolder surfaceHolder;
    private int textY;

    private float cx,cy;//随机圆形的位置
    private float cr;//随机圆形的半径
    private int color;//随机的颜色
    private Random random;
    private Paint cirClePaint;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random=new Random();
        initView();
    }

    private void initView() {
        surfaceView= (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);




    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder=holder;//用于线程的使用
        thread=new Thread(this);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running=false;
        surfaceHolder=null;
        thread.interrupt();
    }

    @Override
    public void run() {
        running=true;
        txtPaint=new TextPaint();
        cirClePaint=new Paint();
        //cirClePaint.setColor();


        txtPaint.setColor(Color.RED);
        txtPaint.setTextSize(50);
        try{

            while(surfaceHolder!=null && running){
                if(surfaceHolder.getSurface()!=null){

                    Rect frame = surfaceHolder.getSurfaceFrame();
//                   if(textY>=frame.bottom){
//                       textY=0;
//                   }else{
//                       textY+=10;
//                   }

                    cx=random.nextInt(frame.right);//水平方向随机
                    cy=random.nextInt(frame.bottom);//垂直方向随机
                     cr=random.nextInt(20)+30;
                    int r=random.nextInt(255);
                    int g=random.nextInt(255);
                    int b=random.nextInt(255);

                    color=Color.rgb(r,g,b);
                    cirClePaint.setColor(color);

                    //更新内容
                    updateRender();
                }
                Thread.sleep(200);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateRender() {
        //开始绘制
        //获取当前屏幕切出的部分
        //通过Canvas 就可以画内容了
        //绘制的内容就可以显示在屏幕上
        Canvas canvas = surfaceHolder.lockCanvas();

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(cx,cy,cr,cirClePaint);

       // canvas.drawText("HelloWorld",10, textY,txtPaint);

        //SurfaceView绘制的时候，需要调用unlockCanvasAndPost（Canvas）
        //实际上采用了   绘制双缓冲技术  TODO 双缓冲技术
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}
