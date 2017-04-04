package mvpdemo.jarvis.com.zidingyiview.leida;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import mvpdemo.jarvis.com.zidingyiview.R;

/**
 * Created by jiangwei on 2017/4/4.
 */

public class LeidaView extends View {


    private Context mContext;
    // 圆环的信用等级文本
    String[] sesameStr = new String[] {
            "350", "较差",
            "550", "中等",
            "600", "良好",
            "650", "优秀",
            "700", "极好",
            "950"
    };

    // 默认宽高值
    private int defaultSize;

    // 距离圆环的值
    private int arcDistance;

    // view宽度
    private int width;

    // view高度
    private int height;

    // 默认Padding值
    private final static int defaultPadding = 20;

    //  圆环起始角度
    private final static float mStartAngle = 165f;

    // 圆环结束角度
    private final static float mEndAngle = 210f;

    //外层圆环画笔
    private Paint mMiddleArcPaint;

    //内层圆环画笔
    private Paint mInnerArcPaint;

    //信用等级文本画笔
    private Paint mTextPaint;

    //大刻度画笔
    private Paint mCalibrationPaint;

    //小刻度画笔
    private Paint mSmallCalibrationPaint;

    //小刻度画笔
    private Paint mCalibrationTextPaint;

    //进度圆环画笔
    private Paint mArcProgressPaint;

    //半径
    private int radius;

    //外层矩形
    private RectF mMiddleRect;

    //内层矩形
    private RectF mInnerRect;

    //进度矩形
    private RectF mMiddleProgressRect;

    // 最小数字
    private int mMinNum = 0;

    // 最大数字
    private int mMaxNum = 950;

    // 当前进度
    private float mCurrentAngle = 0f;

    //总进度
    private float mTotalAngle = 210f;

    //信用等级
    private String sesameLevel = "";

    //评估时间
    private String evaluationTime = "";

    //小圆点
    private Bitmap bitmap;

    //当前点的实际位置
    private float[] pos;

    //当前点的tangent值
    private float[] tan;

    //矩阵
    private Matrix matrix;

    //小圆点画笔
    private Paint mBitmapPaint;

    public LeidaView(Context context) {
        this(context, null);
    }

    public LeidaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeidaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //逻辑代码可以在这里写，最佳实践
        this.mContext = context;
        init();
    }

    private void init() {
        defaultSize = dp2px(250);
        arcDistance = dp2px(14);

        //外层圆环画笔
        mMiddleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleArcPaint.setStrokeWidth(8);
        mMiddleArcPaint.setColor(Color.WHITE);
        mMiddleArcPaint.setStyle(Paint.Style.STROKE);
        mMiddleArcPaint.setAlpha(80);

        //内层圆环画笔
        mInnerArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerArcPaint.setStrokeWidth(30);
        mInnerArcPaint.setColor(Color.WHITE);
        mInnerArcPaint.setAlpha(80);
        mInnerArcPaint.setStyle(Paint.Style.STROKE);

        //正中间字体画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        //圆环大刻度画笔
        mCalibrationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCalibrationPaint.setStrokeWidth(4);
        mCalibrationPaint.setStyle(Paint.Style.STROKE);
        mCalibrationPaint.setColor(Color.WHITE);
        mCalibrationPaint.setAlpha(120);

        //圆环小刻度画笔
        mSmallCalibrationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCalibrationPaint.setStrokeWidth(1);
        mSmallCalibrationPaint.setStyle(Paint.Style.STROKE);
        mSmallCalibrationPaint.setColor(Color.WHITE);
        mSmallCalibrationPaint.setAlpha(130);

        //圆环刻度文本画笔
        mCalibrationTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCalibrationTextPaint.setTextSize(30);
        mCalibrationTextPaint.setColor(Color.WHITE);

        //外层进度画笔
        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setStrokeWidth(8);
        mArcProgressPaint.setColor(Color.WHITE);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);
        mArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        //初始化小圆点图片
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle);
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveMeasure(widthMeasureSpec,defaultSize),resolveMeasure(heightMeasureSpec,defaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radius = width / 2;
    //外层圆环矩形
        mMiddleRect = new RectF(defaultPadding, defaultPadding,width - defaultPadding, height - defaultPadding);
    //内层圆环矩形
        mInnerRect = new RectF(defaultPadding + arcDistance, defaultPadding + arcDistance,width - defaultPadding - arcDistance, height - defaultPadding - arcDistance);
    // 外层进度矩形
        mMiddleProgressRect = new RectF(defaultPadding, defaultPadding,width - defaultPadding, height - defaultPadding);
    }

    /**
     * 根据传入的值进行测量
     */
    public int resolveMeasure(int measureSpec, int defaultSize) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;

            case MeasureSpec.AT_MOST:
                //设置warp_content时设置默认值
                result = Math.min(specSize, defaultSize);
                break;
            case MeasureSpec.EXACTLY:
                //设置math_parent 和设置了固定宽高值
                break;

            default:
                result = defaultSize;
        }
        return result;
    }

    /**
     * dp2px
     */
    public int dp2px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }


    /**
     * 获取当前时间
     */
    public String getCurrentTime() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
        return format.format(new Date());
    }


}
