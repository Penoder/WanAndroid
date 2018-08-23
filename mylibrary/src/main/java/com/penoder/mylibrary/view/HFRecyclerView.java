package com.penoder.mylibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 可添加头尾部的RecyclerView
 */
public class HFRecyclerView extends RecyclerView {

    /**
     * 外部包装adapter
     */
    private WrapAdapter outAdapter;

    private Context mContext;

    public HFRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public HFRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HFRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        outAdapter = new WrapAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        outAdapter.setAdapter(adapter);
        super.setAdapter(outAdapter);
    }

    public WrapAdapter getOutAdapter() {
        return outAdapter;
    }

    public Adapter getInnerAdapter() {
        return outAdapter.getInnnerAdapter();
    }

    /**
     * 设置HeaderView
     */
    public void setHeaderView(View view) {
        if (outAdapter == null) {
            return;
        }
        if (outAdapter.getHeaderViewCount() == 0) {
            outAdapter.addHeaderView(view);
        } else {
            outAdapter.removeHeaderView();
            outAdapter.addHeaderView(view);
        }
    }

    /**
     * 设置FooterView
     */
    public void setFooterView(View view) {
        if (outAdapter == null) {
            return;
        }
        if (outAdapter.getFooterViewCount() == 0) {
            outAdapter.addFooterView(view);
        } else {
            outAdapter.removeFooterView();
            outAdapter.addFooterView(view);
        }
    }

    /**
     * 获取FooterView
     */
    public View getFooterView() {
        if (outAdapter == null) {
            return null;
        }
        return outAdapter.getFooterView();
    }

    /**
     * 获取HeaderView
     */
    public View getHeaderView() {
        if (outAdapter == null) {
            return null;
        }
        return outAdapter.getHeaderView();
    }

    /**
     * 移除FooterView
     */
    public void removeFooterView() {
        if (outAdapter != null) {
            int footerViewCounter = outAdapter.getFooterViewCount();
            if (footerViewCounter > 0) {
                outAdapter.removeFooterView();
            }
        }
    }

    /**
     * 移除HeaderView
     */
    public void removeHeaderView() {
        if (outAdapter != null) {
            int headerViewCounter = outAdapter.getHeaderViewCount();
            if (headerViewCounter > 0) {
                outAdapter.removeHeaderView();
            }
        }
    }

    /**
     * 使用本方法替代RecyclerView.ViewHolder的getLayoutPosition()方法
     */
    public int getLayoutPosition(RecyclerView.ViewHolder holder) {
        if (outAdapter != null) {

            int headerViewCounter = outAdapter.getHeaderViewCount();
            if (headerViewCounter > 0) {
                return holder.getLayoutPosition() - headerViewCounter;
            }
        }

        return holder.getLayoutPosition();
    }

    /**
     * 使用本方法替代RecyclerView.ViewHolder的getAdapterPosition()方法
     */
    public int getAdapterPosition(RecyclerView.ViewHolder holder) {
        if (outAdapter != null) {
            int headerViewCounter = outAdapter.getHeaderViewCount();
            if (headerViewCounter > 0) {
                return holder.getAdapterPosition() - headerViewCounter;
            }
        }
        return holder.getAdapterPosition();
    }


}