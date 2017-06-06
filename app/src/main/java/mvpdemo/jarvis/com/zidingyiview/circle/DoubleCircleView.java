package mvpdemo.jarvis.com.zidingyiview.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import mvpdemo.jarvis.com.zidingyiview.R;

import static android.graphics.Paint.Style.STROKE;

/**
 * Created by jiangwei on 2017/6/6.
 * 双向圆形进度条
 */

public class DoubleCircleView extends View {
    private Paint mBgPaint;//背景的画笔
    private Paint mProgressPaint;//进度的画笔
    private Paint mTextPaint;//文字的画笔
    private int mRoundBackgroundColor;//圆环背景色
    private int mRoundProgressColor;//圆环进度的颜色
    private int mTextColor;//文字颜色
    private float mTextSize;
    private float mRoundWidth;//圆环的宽度
    private float mRadius;

    //这是构造方法的一种最佳实践，保证每个方法的调用都不会有问题
    public DoubleCircleView(Context context) {
        this(context,null);
    }

    public DoubleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public DoubleCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttr(attrs,context);
    }

    //初始化属性，首先必须要在attrs.xml中定义，再在layout布局中引入命名空间并使用，再在这里给属性赋值
    private void initAttr(AttributeSet attrs, Context context) {
        if (attrs != null){
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
            mRoundBackgroundColor = mTypedArray.getColor(R.styleable.CircleProgressBar_roundColor, Color.GRAY);
            mRoundProgressColor = mTypedArray.getColor(R.styleable.CircleProgressBar_roundProgressColor, Color.GREEN);
            mTextColor = mTypedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.GREEN);
            mTextSize = mTypedArray.getDimension(R.styleable.CircleProgressBar_textSize, dp2px(9));
            mRoundWidth = mTypedArray.getDimension(R.styleable.CircleProgressBar_roundWidth, dp2px(5));
            mRadius = mTypedArray.getDimension(R.styleable.CircleProgressBar_radius, dp2px(5));
            mTypedArray.recycle();
        }
    }

    //初始化画笔
    private void initPaint() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mRoundBackgroundColor);
        mBgPaint.setAntiAlias(true);//抗锯齿
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mRoundWidth);//设置线宽
        mBgPaint.setShader(null);//设置渐变模式，这里没有设置

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mRoundProgressColor);
        mProgressPaint.setAntiAlias(true);//抗锯齿
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mRoundWidth);
        mProgressPaint.setShader(null);//设置渐变模式，这里没有设置
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆度，就是结束的时候是圆头，想象生殖器

        //文字paint
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(STROKE);
        mTextPaint.setTextSize(mTextSize);
        //mTextPaint.setStrokeWidth(roundWidth);
        mTextPaint.setAntiAlias(true);

    }

    /**
     * dp2px
     */
    public int dp2px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }
}
