package com.penoder.mylibrary.view;

import android.support.v7.widget.GridLayoutManager;


public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private WrapAdapter adapter;
    private int mSpanSize = 1;

    public HeaderSpanSizeLookup(WrapAdapter adapter, int spanSize) {
        this.adapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.isHeaderView(position) || adapter.isFooterView(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}