package com.careem.careemtest.listener;

import android.view.View;

/**
 * Created by hp on 12/13/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
