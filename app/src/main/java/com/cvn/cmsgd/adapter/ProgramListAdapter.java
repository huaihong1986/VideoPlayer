package com.cvn.cmsgd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvn.cmsgd.PlayerActivity;
import com.cvn.cmsgd.R;
import com.cvn.cmsgd.model.ProgramData;
import com.cvn.cmsgd.net.Net;

import java.util.ArrayList;
import java.util.List;


public class ProgramListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ProgramListAdapter";

    public static final int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<ProgramData.DataEntity> mList;

    public ProgramListAdapter(Context context, ArrayList<ProgramData.DataEntity> list) {
        mContext = context;
        mList = list;
    }

    public void changeData(ArrayList<ProgramData.DataEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_program_list_item, parent, false);
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
                final ProgramData.DataEntity item = mList.get(position);
                final ItemViewHolder h = (ItemViewHolder) holder;
                if (item == null) {
                    break;
                }
                h.view.setTag(item);
                h.play.setTag(item);
                h.title.setText(item.getTitle());

                List<ProgramData.DataEntity.EpgListEntity> EpgList = item.getEpgList();
                ProgramData.DataEntity.EpgListEntity currEpg = null;
                if (EpgList != null && EpgList.size() > 0) {
                    currEpg = EpgList.get(0);
                }
                if (currEpg != null && !TextUtils.isEmpty(currEpg.getTitle())) {
                    h.desc.setText(mContext.getString(R.string.current_play_epg, currEpg.getTitle()));
                } else {
                    h.desc.setText("");
                }

                h.icon.setImageResource(R.mipmap.default_thumb);
                if (!TextUtils.isEmpty(item.getCont_pic())) {
                    Net.loadImage(item.getCont_pic(), h.icon);
                }
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

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        ImageView icon;
        TextView title;
        TextView desc;
        ImageView play;
        View line;

        public ItemViewHolder(View v) {
            super(v);
            view = v;
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            play = (ImageView) view.findViewById(R.id.play);
            line = view.findViewById(R.id.line);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final ProgramData.DataEntity item = (ProgramData.DataEntity) view.getTag();
            if (item != null) {
                Intent it = new Intent(mContext, PlayerActivity.class);
                it.putExtra(PlayerActivity.KEY_PLAY_URL, item.getCont_play());
                it.putExtra(PlayerActivity.KEY_PLAY_FREE_TIME, item.getTimeFreelong());
                it.putExtra(PlayerActivity.KEY_TITLE, item.getTitle());
                mContext.startActivity(it);
            }
        }
    }

}
