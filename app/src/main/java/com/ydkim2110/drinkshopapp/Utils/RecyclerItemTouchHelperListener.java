package com.ydkim2110.drinkshopapp.Utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public interface RecyclerItemTouchHelperListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

}
