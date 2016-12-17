package daixiansen.customdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Description 自定义圆形进度条
 * Author daixiansen
 **/
public class RoundProgress extends HorizontalProgress {

    private static final int PROGRESS_RADIUS = 10;
    private int mRadius = dp2px(PROGRESS_RADIUS); // 圆环半径
    private int mMaxRadiusWeight;
    public RoundProgress(Context context) {
        this(context,null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundProgressWithProgress);
        mRadius = (int) typedArray.getDimension(R.styleable.RoundProgressWithProgress_progress_radius,mRadius);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        reachHeight = (int) (unReachHeight * 1.5f);
        mMaxRadiusWeight = Math.max(reachHeight,unReachHeight);
        // 默认四个 padding值相同
        int expect = mRadius * 2 + getPaddingLeft()+getPaddingRight()+mMaxRadiusWeight;
        // 系统测量方法
        int measureWidth = resolveSize(expect, widthMeasureSpec);
        int measureHeight = resolveSize(expect, heightMeasureSpec);
        // 绘制宽度按最小宽度绘制
        int drawWeight = Math.min(measureWidth,measureHeight);
        // 重新计算半径
        mRadius = (drawWeight - getPaddingLeft() - getPaddingRight() - mMaxRadiusWeight) / 2;
        setMeasuredDimension(drawWeight,drawWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        String textContent = getProgress() + "%";
        int textWeight = (int) mPaint.measureText(textContent);
        int textheight = (int) ((mPaint.descent() + mPaint.ascent()) / 2);
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxRadiusWeight/2,getPaddingRight() + mMaxRadiusWeight/2);
        // draw unreachBar
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(unReachColor);
        mPaint.setStrokeWidth(unReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        // draw reachBar
        float percent = getProgress() * 1.0f / getMax();
        float angle = percent * 360;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(reachColor);
        mPaint.setStrokeWidth(reachHeight);
        canvas.drawArc(new RectF(0,0,mRadius * 2,mRadius * 2),0,angle,false,mPaint);
        // draw text
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(textSize);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(textContent,mRadius-textWeight/2,mRadius-textheight,mPaint);
        canvas.restore();
    }
}
