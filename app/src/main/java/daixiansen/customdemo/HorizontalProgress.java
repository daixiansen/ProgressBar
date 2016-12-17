package daixiansen.customdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Description 自定义progress
 * Author daixiansen
 **/
public class HorizontalProgress extends ProgressBar {

    private static final int PROGRESS_REACH_COLOR = 0xffE004AB;
    private static final int PROGRESS_REACH_HEIGHT = 2;  //dp
    private static final int PROGRESS_UNREACH_COLOR = 0x55E004AB;
    private static final int PROGRESS_UNREACH_HEIGHT = 2; //dp
    private static final int PROGRESS_TEXT_SIZE = 10; //sp
    private static final int PROGRESS_TEXT_COLOR = 0xff11E0CA;
    private static final int PROGRESS_TEXT_OFFSET = 10;

    protected int reachColor = PROGRESS_REACH_COLOR;
    protected int reachHeight = dp2px(PROGRESS_REACH_HEIGHT);
    protected int unReachColor = PROGRESS_UNREACH_COLOR;
    protected int unReachHeight = dp2px(PROGRESS_UNREACH_HEIGHT);
    protected int textSize = sp2px(PROGRESS_TEXT_SIZE);
    protected int textColor = PROGRESS_TEXT_COLOR;
    protected int textOffset = dp2px(PROGRESS_TEXT_OFFSET);

    Paint mPaint = new Paint();

    private int mRealWidth; // 实际绘制宽度


    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置文字height 以方便下面计算 textHeight 高度
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(textSize);
        obtainStyledAttrs(attrs);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressWithProgress);
        reachColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgress_progress_reach_color, PROGRESS_REACH_COLOR);
        reachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgress_progress_reach_height, reachHeight);
        unReachColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgress_progress_unreach_color, PROGRESS_UNREACH_COLOR);
        unReachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgress_progress_unreach_height, unReachHeight);
        textOffset = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgress_progress_text_offset, textOffset);
        textSize = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgress_progress_text_size, textSize);
        textColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgress_progress_text_color,PROGRESS_TEXT_COLOR);
        typedArray.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = computeHeight(heightMeasureSpec);
        // 确定view的宽和高
        setMeasuredDimension(widthSize,heightSize);
        mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    private int computeHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if(mode == MeasureSpec.EXACTLY){
            // 精确模式
            result = size;
        }else{
            // MeasureSpec.UNSPECIFIED   MeasureSpec.AT_MOST
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop()+getPaddingBottom()+Math.max(Math.max(reachHeight,unReachHeight),Math.abs(textHeight));
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeddUnreach = true;
        // 计算文字的宽度
        String textContent = getProgress()+"%";
        int textWeight = (int) mPaint.measureText(textContent);
        float radio = getProgress() * 1.0f / getMax();
        // draw unreach
        float progressX = mRealWidth * radio;
        if(progressX + textWeight > mRealWidth){
            // 不需要绘制 unReach 部分了
            progressX = mRealWidth - textWeight;
            noNeddUnreach = false;
        }
        int endX = (int) (progressX - textOffset/2);
        if(endX > 0){
            mPaint.setColor(reachColor);
            mPaint.setStrokeWidth(reachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        // 绘制百分比
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(textSize);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(textContent,progressX,y,mPaint);

        if(noNeddUnreach){
            // 绘制 unReach 部分
            mPaint.setColor(unReachColor);
            mPaint.setStrokeWidth(unReachHeight);
            float startX = progressX + textOffset/2 + textWeight;
            canvas.drawLine(startX,0,mRealWidth,0,mPaint);
        }
        canvas.restore();
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }


    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}
