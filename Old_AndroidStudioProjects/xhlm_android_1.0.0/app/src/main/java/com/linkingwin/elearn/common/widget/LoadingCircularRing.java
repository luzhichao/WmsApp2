package com.linkingwin.elearn.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 加载进度动画
 */
public class LoadingCircularRing extends View {
    private ValueAnimator valueAnimator;
    private Paint mPaint;
    private float startAngle = 0f;
    private int mWidth;
    private int mPadding;
    public LoadingCircularRing(Context context) {
        this(context,null);
    }

    public LoadingCircularRing(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingCircularRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(8);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(100,100);
        mWidth = getMeasuredWidth();
        mPadding = 5;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#88FFFFFF"));
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint);
        mPaint.setColor(Color.WHITE);
        RectF rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        canvas.drawArc(rectF, startAngle, 60, false, mPaint);//第四个参数是否显示半径
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 800);
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);//-1表示无限循环
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }
    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);//
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                startAngle = 360 * value;
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        return valueAnimator;
    }
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility==View.VISIBLE){
            startAnim();
        }else{
            stopAnim();
        }
    }
}
