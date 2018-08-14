package me.dannyfeng.target;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View implements View.OnTouchListener, Runnable {
    int scoreCount = 0;
    int ammoCount = 8;
    static final int TARGET_Y = 400;
    float xt = 0.0f;
    float yt = 0.0f;
    float xr = 0.0f;
    Handler timer;
    Bitmap myImage;
    private SoundPool fireSoundPool;
    private int fireSoundID;
    private SoundPool bringoSoundPool;
    private int bringoSoundID;
    private SoundPool reloadSoundPool;
    private int reloadSoundID;
    Boolean moveRight = true;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        myImage = BitmapFactory.decodeResource(getResources(), R.drawable.eric);
        timer = new Handler();
        timer.postDelayed(this, 10);
        fireSoundPool = new SoundPool.Builder().build();
        fireSoundID = fireSoundPool.load(this.getContext(), R.raw.gunshot, 0);
        bringoSoundPool = new SoundPool.Builder().build();
        bringoSoundID = bringoSoundPool.load(this.getContext(), R.raw.headshot, 1);
        reloadSoundPool = new SoundPool.Builder().build();
        reloadSoundID = reloadSoundPool.load(this.getContext(), R.raw.reload, 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setStrokeWidth(10.0f);
        p.setTextSize(50.0f);
        canvas.drawCircle(xt, yt, 19.0f, p);
        canvas.drawBitmap(myImage, xr, TARGET_Y, p);
        canvas.drawText("Score " + String.valueOf(scoreCount), 50, 50, p);
        canvas.drawText("Ammo " + String.valueOf(ammoCount), 50, 200, p);
        canvas.drawLine(0, this.getHeight() - this.getHeight() / 4,
                this.getWidth(), this.getHeight() - this.getHeight() / 4, p);
        canvas.drawText("CLICK THIS AREA TO RELOAD",
                50, this.getHeight() - this.getHeight() / 4 + 60, p);

    }

    public void playOpenFire() {
        fireSoundPool.play(fireSoundID, 1f,
                1f, 0, 0, 1
        );
    }

    public void playBringo() {
        bringoSoundPool.play(bringoSoundID, 1f,
                1f, 1, 0, 1
        );
    }

    public void playReloadSound() {
        reloadSoundPool.play(reloadSoundID, 1f,
                1f, 0, 0, 1
        );
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                && (motionEvent.getY() < this.getHeight() - this.getHeight() / 4)
                && ammoCount != 0) {
            playOpenFire();
            ammoCount--;
            xt = motionEvent.getX();
            yt = motionEvent.getY();
            this.invalidate();
        }

        if(ammoCount == 0){
            playReloadSound();
        }


        if (Math.abs(motionEvent.getX() - (xr + 100)) <= 40
                && Math.abs(motionEvent.getY() - (TARGET_Y + 60)) <= 50
                && motionEvent.getAction() == MotionEvent.ACTION_DOWN
                && ammoCount != 0) {
            scoreCount++;
            playBringo();
        }

        if (motionEvent.getY() >= this.getHeight() - this.getHeight() / 4) {
            scoreCount = 0;
            ammoCount = 8;
            playReloadSound();
        }
        return true;
    }

    @Override
    public void run() {
        if (moveRight) {
            xr += 5.0f;
            this.invalidate();
            timer.postDelayed(this, 10);
        }

        if (!moveRight) {
            xr -= 5.0f;
            this.invalidate();
            timer.postDelayed(this, 10);
        }


        if (xr == this.getWidth()) {
            moveRight = false;
        }

        if (xr == 0) {
            moveRight = true;
        }

    }
}