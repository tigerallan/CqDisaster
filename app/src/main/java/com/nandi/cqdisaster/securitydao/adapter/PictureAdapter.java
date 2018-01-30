package com.nandi.cqdisaster.securitydao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nandi.cqdisaster.R;

/**
 * @author qingsong  on 2017/10/26.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
    private Context mContext;
    public PictureAdapter.OnItemClickListener mOnItemClickListener;
    private String[] mList;

    public PictureAdapter(Context context, String[] list) {
        this.mContext = context;
        this.mList = list;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(PictureAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public PictureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_picture, null);
        PictureAdapter.MyViewHolder holderA = new PictureAdapter.MyViewHolder(view);
        return holderA;
    }

    @Override
    public void onBindViewHolder(PictureAdapter.MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mContext.getString(R.string.base_url) + "/down/img?path="
                + mContext.getString(R.string.picPath) + "uploadImg/" +mList[position])
                .placeholder(R.drawable.downloading)
                .error(R.drawable.download_pass)
                .into(holder.picture);

        if (mOnItemClickListener != null) {
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.length > 0 ? mList.length : 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;

        public MyViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.iv_picture);
        }
    }
}
