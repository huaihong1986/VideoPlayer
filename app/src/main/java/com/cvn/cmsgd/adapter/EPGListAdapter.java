package com.cvn.cmsgd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.model.EPGData;

import java.util.ArrayList;


public class EPGListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "EPGListAdapter";

    public static final int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<EPGData.DataEntity> mList;

    public EPGListAdapter(Context context, ArrayList<EPGData.DataEntity> list) {
        mContext = context;
        mList = list;
    }

    public void changeData(ArrayList<EPGData.DataEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epg_list_item, parent, false);
                return new ItemViewHolder(view);
        }
        return null;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                if (mList == null || mList.size() == 0) {
                    break;
                }
                final EPGData.DataEntity item = mList.get(position);
                final ItemViewHolder h = (ItemViewHolder) holder;
                if (item == null) {
                    break;
                }
                StringBuilder builder = new StringBuilder();
                builder.append(item.getStartTime());
                builder.append("-");
                builder.append(item.getEndTime());
                builder.append(" ");
                builder.append(item.getTitle());
                builder.append("    ");
                h.title.setText(builder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;

        public ItemViewHolder(View v) {
            super(v);
            view = v;
            title = (TextView) view.findViewById(R.id.title);
        }
    }

}
