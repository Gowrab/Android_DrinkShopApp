package com.ydkim2110.drinkshopapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.R;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MultiChoiceViewHolder> {

    private static final String TAG = "MultiChoiceAdapter";

    private Context mContext;
    private List<Drink> mOptionList;

    public MultiChoiceAdapter(Context context, List<Drink> optionList) {
        mContext = context;
        mOptionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.multi_check_layout, parent, false);

        return new MultiChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.mCheckBox.setText(mOptionList.get(position).Name);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Common.toppingAdded.add(compoundButton.getText().toString());
                    Common.toppingPice += Double.parseDouble(mOptionList.get(position).Price);
                }
                else {
                    Common.toppingAdded.add(compoundButton.getText().toString());
                    Common.toppingPice -= Double.parseDouble(mOptionList.get(position).Price);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptionList.size();
    }

    public class MultiChoiceViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckBox;

        public MultiChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.ckb_topping);
        }
    }
}
