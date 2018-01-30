package com.nandi.cqdisaster.securitydao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.bean.PersonnelInfo;

import java.util.List;

/**
 * @author qingsong  on 2017/10/26.
 */

public class PersonnelAdapter extends RecyclerView.Adapter<PersonnelAdapter.MyViewHolder> {
    private Context mContext;
    public PersonnelAdapter.OnItemClickListener mOnItemClickListener;
    private List<PersonnelInfo> list;

    public PersonnelAdapter(Context context, List<PersonnelInfo> list) {
        this.mContext = context;
        this.list = list;

    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(PersonnelAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public PersonnelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_personnel_information, parent,false);
        PersonnelAdapter.MyViewHolder holderA = new PersonnelAdapter.MyViewHolder(view);
        return holderA;
    }

    @Override
    public void onBindViewHolder(PersonnelAdapter.MyViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getType());
        Glide.with(mContext).load(mContext.getString(R.string.base_url) + "/down/img?path="
                + mContext.getString(R.string.picPath) + list.get(position).getImageType()+"/"+ list.get(position).getUrl())
                .placeholder(R.drawable.downloading)
                .error(R.drawable.download_pass)
                .into(holder.picture);
        holder.phone.setText(list.get(position).getTel());
        holder.name.setText(list.get(position).getName());
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
        return list.size() > 0 ? list.size() : 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView title, name, phone;

        public MyViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.iv_rc_person_2);
            title = (TextView) itemView.findViewById(R.id.tv_rc_person_1);
            name = (TextView) itemView.findViewById(R.id.tv_rc_person_3);
            phone = (TextView) itemView.findViewById(R.id.tv_rc_person_4);
        }
    }
}
