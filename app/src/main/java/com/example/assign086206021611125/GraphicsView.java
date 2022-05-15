package com.example.assign086206021611125;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.util.Random;
import javax.xml.transform.Result;

public class GraphicsView extends View implements View.OnTouchListener{
    private Paint p;

    private Bitmap img;
    private int score, time, imageWidth, imageHeight;
    private int[] speed = new int[5];
    private final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    private int boom;
    private float[] x = new float[5];
    private float[] y = new float[5];

    private boolean finish = false;
    private boolean[] shoot = new boolean[5];

    private CountDownTimer timer1,timer2;
    private Random random = new Random();
    private SoundPool soundPool;

    private Bitmap ufo = BitmapFactory.decodeResource(getResources(),R.drawable.ufo);
    private Bitmap ufoboom = BitmapFactory.decodeResource(getResources(),R.drawable.ufo_bm);

    public GraphicsView(Context context) {
        super(context);

        setBackgroundColor(Color.BLACK);
        p = new Paint();
        score =0;
        time = 0;
        setOnTouchListener(this);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        boom = soundPool.load(context, R.raw.music1,1);

        imageWidth = ufo.getWidth();
        imageHeight = ufo.getHeight();

        for (int i=0; i<5; i++){
            x[i] = random.nextInt(width - imageWidth);
            speed[i] = random.nextInt(10) + 10;
        }

        timer1 = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                time++;
                invalidate();
            }

            @Override
            public void onFinish() {
                finish = true;
                invalidate();
            }
        };

        timer2 = new CountDownTimer(30000, 50) {
            @Override
            public void onTick(long l) {
                for (int i = 0; i<5; i++){
                    y[i] += speed[i];
                    if (y[i] >= height + imageHeight){
                        y[i] = 0 - imageHeight;
                        x[i] = random.nextInt(width - imageWidth);

                    }
                }
                invalidate();
            }

            @Override
            public void onFinish() {

            }
        };
        timer1.start();
        timer2.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (finish){
            finish = false;
            timer1.start();
            timer2.start();
            time = 0;
            score = 0;
            invalidate();
        }
        else {
            float X = event.getX();
            float Y = event.getY();

            for (int i=0 ; i < 5 ; i++) {
                if (X > x[i] && X < x[i] + imageWidth) {
                    if (Y > y[i] && Y <y[i] + imageHeight) {
                        soundPool.play(boom,1,1,1,0,1);
                        score++;
                        shoot[i] = true;
                        invalidate();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if (finish){
            p.setColor(Color.GREEN);
            p.setTextSize(65);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("T I M E O U T", width/2, height/2 - 200, p);
            canvas.drawText("Press for play again or back to exit", width/2, height/2 -100, p);
        }
        else {
            p.setColor(Color.CYAN);
            p.setTextSize(50);
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score : " + score,20,60, p);
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Time : " + time, width-20, 60, p);

            for (int i=0 ; i < 5 ; i++){
                if (shoot[i]){
                    canvas.drawBitmap(ufoboom, x[i], y[i], null);
                    y[i] = 0 - imageHeight;
                    x[i] = random.nextInt(width - imageWidth);
                    shoot[i] = false;
                }
                canvas.drawBitmap(ufo, x[i], y[i], null);
            }
        }
    }
}
