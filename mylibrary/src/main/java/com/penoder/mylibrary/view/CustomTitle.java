package com.penoder.mylibrary.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.penoder.mylibrary.R;
import com.penoder.mylibrary.utils.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Penoder
 * @date 18-4-19.
 */
public class CustomTitle extends LinearLayout {

    private Context mContext;

    /**
     * 标题
     */
    private TextView txtTitle;

    /**
     * 左侧按钮（一般返回键）
     */
    private ImageView imgLeftIcon;

    /**
     * 左标题
     */
    private TextView txtLeftTitle;

    /**
     * 右侧菜单按钮
     */
    private ImageView imgRightIcon;

    /**
     * 菜单标题
     */
    private TextView txtRightTitle;

    private boolean isDoubleClick = false;

    private Timer tExit;

    public CustomTitle(Context context) {
        this(context, null);
    }

    public CustomTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);
    }

    @SuppressLint("InflateParams")
    private void initView(AttributeSet attrs) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_title_bar, null);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        imgLeftIcon = (ImageView) view.findViewById(R.id.img_leftIcon);
        txtLeftTitle = (TextView) view.findViewById(R.id.txt_leftTitle);
        imgRightIcon = (ImageView) view.findViewById(R.id.img_rightIcon);
        txtRightTitle = (TextView) view.findViewById(R.id.txt_rightTitle);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitle);
            for (int i = 0; i < typedArray.getIndexCount(); i++) {
                int styleable = typedArray.getIndex(i);
                if (styleable == R.styleable.CustomTitle_leftIcon) { // 左图标
                    int leftIcon = typedArray.getResourceId(R.styleable.CustomTitle_leftIcon, 0);
                    imgLeftIcon.setImageResource(leftIcon);
                } else if (styleable == R.styleable.CustomTitle_leftIconEnable) {
                    boolean enable = typedArray.getBoolean(R.styleable.CustomTitle_leftIconEnable, true);
                    imgLeftIcon.setEnabled(enable);
                } else if (styleable == R.styleable.CustomTitle_leftTitle) {  // 左标题
                    String leftTitle = (String) typedArray.getText(R.styleable.CustomTitle_leftTitle);
                    txtLeftTitle.setText(leftTitle);
                } else if (styleable == R.styleable.CustomTitle_titleTxt) { // 标题
                    String title = (String) typedArray.getText(R.styleable.CustomTitle_titleTxt);
                    txtTitle.setText(title);
                } else if (styleable == R.styleable.CustomTitle_rightTitle) { // 菜单标题
                    String rightTitle = (String) typedArray.getText(R.styleable.CustomTitle_rightTitle);
                    txtRightTitle.setText(rightTitle);
                } else if (styleable == R.styleable.CustomTitle_rightIcon) { // 右菜单图标，与文字不可同时存在
                    int rightIcon = typedArray.getResourceId(R.styleable.CustomTitle_rightIcon, 0);
                    imgRightIcon.setImageResource(rightIcon);
                } else if (styleable == R.styleable.CustomTitle_textColor) {
                    int textColor = typedArray.getColor(R.styleable.CustomTitle_textColor, 0);
                    txtLeftTitle.setTextColor(textColor);
                    txtTitle.setTextColor(textColor);
                    txtRightTitle.setTextColor(textColor);
                } else if (styleable == R.styleable.CustomTitle_leftTitleSize) {
                    float leftTitleSize = typedArray.getDimension(R.styleable.CustomTitle_leftTitleSize, 0);
                    leftTitleSize = DensityUtils.dp2sp(mContext, leftTitleSize);
                    txtTitle.setTextSize(leftTitleSize);
                } else if (styleable == R.styleable.CustomTitle_titleSize) {
                    float titleSize = typedArray.getDimension(R.styleable.CustomTitle_titleSize, 0);
                    titleSize = DensityUtils.dp2sp(mContext, titleSize);
                    txtLeftTitle.setTextSize(titleSize);
                } else if (styleable == R.styleable.CustomTitle_rightTitleSize) {
                    float rightTitleSize = typedArray.getDimension(R.styleable.CustomTitle_rightTitleSize, 0);
                    rightTitleSize = DensityUtils.dp2sp(mContext, rightTitleSize);
                    txtRightTitle.setTextSize(rightTitleSize);
                }
            }
            typedArray.recycle();
        }
        addView(view);

        // 左边图标点击事件，如果代码中不重写，直接结束该Activity
        imgLeftIcon.setOnClickListener(v -> {
            if (onLeftIconClickListener != null) {
                onLeftIconClickListener.onLeftIconClick(v);
            } else {
                ((Activity) mContext).finish();
            }
        });

        imgRightIcon.setOnClickListener(v -> {
            if (onRightIconClickListener != null) {
                onRightIconClickListener.onRightIconClick(v);
            }
        });

        txtTitle.setOnClickListener(v -> {
            if (onTitleClickListener != null) {
                if (!isDoubleClick) {
                    isDoubleClick = true;
                    if (tExit == null) {
                        tExit = new Timer();
                    }
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {     // 这是在子线程当中，子线程设置单击双击事件的话，不能够更新UI啊，否则异常
                            isDoubleClick = false;
                            post(() -> {
                                onTitleClickListener.onTitleClick(view);     // 单击事件
                            });
                        }
                    }, 200); // 如果没有点击两次，则启动定时器取消掉刚才执行的任务
                } else {
                    isDoubleClick = false;
                    if (tExit != null) {
                        tExit.cancel();
                        tExit = null;
                    }
                    onTitleClickListener.onTitleDoubleClick(view);     // 双击事件
                }
            }
        });

        txtRightTitle.setOnClickListener(v -> {
            if (onRightTitleClickListener != null) {
                onRightTitleClickListener.onRightTitleClick(v);
            }
        });
    }

    public OnLeftIconClickListener onLeftIconClickListener;

    public OnTitleClickListener onTitleClickListener;

    public OnRightIconClickListener onRightIconClickListener;

    public OnRightTitleClickListener onRightTitleClickListener;

    public void setOnLeftIconClickListener(OnLeftIconClickListener onLeftIconClickListener) {
        this.onLeftIconClickListener = onLeftIconClickListener;
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public void setOnRightIconClickListener(OnRightIconClickListener onRightIconClickListener) {
        this.onRightIconClickListener = onRightIconClickListener;
    }

    public void setOnRightTitleClickListener(OnRightTitleClickListener onRightTitleClickListener) {
        this.onRightTitleClickListener = onRightTitleClickListener;
    }

    public interface OnLeftIconClickListener {

        /**
         * 左边图标点击事件
         */
        void onLeftIconClick(View view);

    }

    public interface OnTitleClickListener {

        /**
         * 标题点击事件
         */
        void onTitleClick(View view);

        /**
         * 标题双击事件
         */
        void onTitleDoubleClick(View view);
    }

    public interface OnRightIconClickListener {

        /**
         * 右边图标点击事件
         */
        void onRightIconClick(View view);

    }

    public interface OnRightTitleClickListener {

        /**
         * 右边标题点击事件
         */
        void onRightTitleClick(View view);

    }

    // 设置左标题
    public void setLeftTitle(String leftTitle) {
        txtLeftTitle.setText(leftTitle);
    }

    public void setLeftTitle(int resId) {
        txtLeftTitle.setText(resId);
    }

    // 设置标题
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setTitle(int resId) {
        txtTitle.setText(resId);
    }

    // 设置菜单标题
    public void setRightTitle(String rightTitle) {
        txtRightTitle.setText(rightTitle);
    }

    public void setRightTitle(int resId) {
        txtRightTitle.setText(resId);
    }

    // 设置左图标
    public void setLeftIcon(int resID) {
        imgLeftIcon.setImageResource(resID);
    }

    // 设置右图标
    public void setRightIcon(int resID) {
        imgRightIcon.setImageResource(resID);
    }
}
