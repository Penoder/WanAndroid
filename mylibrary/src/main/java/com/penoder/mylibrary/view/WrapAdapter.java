package com.penoder.mylibrary.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView.Adapter的外部包装Adapter
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;  // Item 类型为 Header
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1; // Item 类型为 Footer
//    private static final int TYPE_NORMAL_VIEW = Integer.MIN_VALUE + 2;  // Item 类型为 正常数据视图

    private View mHeaderView = null;
    private View mFooterView = null;

    /**
     * RecyclerView 实际使用的Adapter
     */
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;


    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderViewCount(), itemCount);
        }


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition + getHeaderViewCount(), toPosition + getHeaderViewCount() + itemCount);
        }
    };

    public WrapAdapter() {
    }

    public WrapAdapter(RecyclerView.Adapter innerAdapter) {
        setAdapter(innerAdapter);
    }


    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (adapter == null) {
            throw new RuntimeException("your adapter is null");
        }
        if (mInnerAdapter != null) {
            //清除已有的item
            notifyItemRangeRemoved(getFooterViewCount(), mInnerAdapter.getItemCount());
            mInnerAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        mInnerAdapter = adapter;
        mInnerAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeaderViewCount(), mInnerAdapter.getItemCount());
    }

    /**
     * 获取adapter
     */
    public RecyclerView.Adapter getInnnerAdapter() {
        return mInnerAdapter;
    }

    /**
     * 增加头布局
     */
    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("headerView is null");
        }
        mHeaderView = header;
        this.notifyDataSetChanged();
    }

    /**
     * 增加尾布局
     *
     * @param footer
     */
    public void addFooterView(View footer) {
        if (footer == null) {
            throw new RuntimeException("footerView is null");
        }
        mFooterView = footer;
        this.notifyDataSetChanged();
    }

    /**
     * 获取头布局
     */
    public View getHeaderView() {
        return getHeaderViewCount() > 0 ? mHeaderView : null;
    }

    /**
     * 获取尾布局
     */
    public View getFooterView() {
        return getFooterViewCount() > 0 ? mFooterView : null;
    }

    /**
     * 移除头布局
     */
    public void removeHeaderView() {
        if (isHaveHeaderView()) {
            mHeaderView = null;
            this.notifyDataSetChanged();
        }
    }

    /**
     * 移除尾布局
     */
    public void removeFooterView() {
        if (isHaveFooterView()) {
            mFooterView = null;
            this.notifyDataSetChanged();
        }
    }

    /**
     * HeaderView 数量
     */
    public int getHeaderViewCount() {
        return mHeaderView == null ? 0 : 1;
    }

    /**
     * FooterView 数量
     */
    public int getFooterViewCount() {
        return mFooterView == null ? 0 : 1;
    }

    /**
     * 是否存在HeaderView
     */
    public boolean isHaveHeaderView() {
        return mHeaderView != null;
    }

    /**
     * 是否存在FooterView
     */
    public boolean isHaveFooterView() {
        return mFooterView != null;
    }

    /**
     * 该position位置是否是HeaderView
     */
    public boolean isHeaderView(int position) {
        return isHaveHeaderView() && position == 0;
    }

    /**
     * 该position位置是否是FooterView
     */
    public boolean isFooterView(int position) {
        return isHaveFooterView() && position == getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        return getHeaderViewCount() + getFooterViewCount() + mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int innerCount = mInnerAdapter.getItemCount();
        int headerViewCount = getHeaderViewCount();
        if (position < headerViewCount) {
            return TYPE_HEADER_VIEW;
        } else if (position >= headerViewCount + innerCount) {
            return TYPE_FOOTER_VIEW;
        } else {
            return getInnnerAdapter().getItemViewType(position - headerViewCount);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_VIEW) {
            return new ViewHolder(mHeaderView);
        } else if (viewType == TYPE_FOOTER_VIEW) {
            return new ViewHolder(mFooterView);
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerViewCount = getHeaderViewCount();
        if (position >= headerViewCount && position < headerViewCount + mInnerAdapter.getItemCount()) {
            mInnerAdapter.onBindViewHolder(holder, position - headerViewCount);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }


}