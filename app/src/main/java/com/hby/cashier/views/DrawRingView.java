package com.hby.cashier.views;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.ContextCompat;

import com.hby.cashier.R;
import com.hby.cashier.utils.DensityUtils;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/7
 */
public class DrawRingView extends View {
    private Context mContext;

    private int mArcColor;
    private int mArcWidth;
    private int mCenterTextColor;
    private int maxTextSize;
    private int minTextSize;
    private int mCircleRadius;
    private Paint arcPercentPaint;
    private Paint arcBackPaint;
    private Paint textPaint1;
    private Paint textPaint2;

    private RectF arcRectF;
    private Rect text1BoundRect;
    private Rect text2BoundRect;
    private float mCurData = 0;

    private int arcBackColor;
    private int arcPercentColor;

    public DrawRingView(Context context) {
        this(context, null);
    }

    public DrawRingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentBar, defStyleAttr, 0);
        mArcWidth = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_arcWidth, DensityUtils.dip2px(context, 20));
        maxTextSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_maxTextSize, DensityUtils.dip2px(context, 18));
        minTextSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_minTextSize, DensityUtils.dip2px(context, 14));
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_circleRadius, DensityUtils.dip2px(context, 50));
        arcBackColor = typedArray.getColor(R.styleable.CirclePercentBar_arcBackColor, ContextCompat.getColor(mContext, R.color.color_e6));
        arcPercentColor = typedArray.getColor(R.styleable.CirclePercentBar_arcPercentColor, ContextCompat.getColor(mContext, R.color.themeColor));
        mCenterTextColor = typedArray.getColor(R.styleable.CirclePercentBar_centerTextColor, ContextCompat.getColor(mContext, R.color.color_1a));

        typedArray.recycle();

        initPaint();

    }

    private void initPaint() {
        arcBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcBackPaint.setStyle(Paint.Style.STROKE);
        arcBackPaint.setStrokeWidth(mArcWidth);
        arcBackPaint.setColor(arcBackColor);
        arcBackPaint.setStrokeCap(Paint.Cap.ROUND);


        arcPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPercentPaint.setStyle(Paint.Style.STROKE);
        arcPercentPaint.setStrokeWidth(mArcWidth);
        arcPercentPaint.setColor(arcPercentColor);
        arcPercentPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint1.setStyle(Paint.Style.STROKE);
        textPaint1.setColor(mCenterTextColor);
        textPaint1.setTextSize(maxTextSize);
        textPaint1.setTypeface(Typeface.DEFAULT_BOLD);

        textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint2.setStyle(Paint.Style.STROKE);
        textPaint2.setColor(mCenterTextColor);
        textPaint2.setTextSize(minTextSize);

        //圓弧的外接矩形
        arcRectF = new RectF();

        //文字的边界矩形
        text1BoundRect = new Rect();
        text2BoundRect = new Rect();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = mCircleRadius * 2;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {

//        canvas.rotate(-90, getWidth() / 2, getHeight() / 2);

        arcRectF.set(getWidth() / 2 - mCircleRadius + mArcWidth / 2, getHeight() / 2 - mCircleRadius + mArcWidth / 2
                , getWidth() / 2 + mCircleRadius - mArcWidth / 2, getHeight() / 2 + mCircleRadius - mArcWidth / 2);


        canvas.drawArc(arcRectF, 0, 360, false, arcBackPaint);
        canvas.drawArc(arcRectF, 0, 360 * mCurData / 100, false, arcPercentPaint);

        String data = String.valueOf(mCurData) + "%";
        String data2 = minText;
        textPaint1.getTextBounds(data, 0, data.length(), text1BoundRect);
        textPaint2.getTextBounds(data2, 0, data2.length(), text2BoundRect);
//        float maxTextY=getHeight() / 2 + text1BoundRect.height() / 2;
        float maxTextY = getHeight() / 2 + text1BoundRect.height() / 2 - text2BoundRect.height();
        float minTextY = getHeight() / 2 + (text2BoundRect.height() + text1BoundRect.height()) / 2;
        canvas.drawText(data, getWidth() / 2 - text1BoundRect.width() / 2, maxTextY, textPaint1);
        canvas.drawText(data2, getWidth() / 2 - text2BoundRect.width() / 2, minTextY, textPaint2);
    }

    private double percentData;
    private String minText;
    private TimeInterpolator interpolator;

    public DrawRingView setPercentData(double data, TimeInterpolator interpolator) {
        this.percentData = data;
        this.interpolator = interpolator;
        return this;
    }

    public DrawRingView setPercentData(double data) {
        setPercentData(data, new DecelerateInterpolator());
        return this;
    }

    public DrawRingView setMinText(String minText) {
        this.minText = minText;
        return this;
    }

    public DrawRingView setArcPercentColor(int arcPercentColor) {
        arcPercentPaint.setColor(arcPercentColor);
        return this;
    }

    public void show() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurData, (float) percentData);
        valueAnimator.setDuration((long) (Math.abs(mCurData - percentData) * 30));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mCurData = (float) (Math.round(value * 100)) / 100;
                invalidate();
            }
        });
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }


}

