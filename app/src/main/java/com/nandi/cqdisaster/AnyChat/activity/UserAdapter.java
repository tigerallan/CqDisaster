package com.nandi.cqdisaster.AnyChat.activity;


import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.AnyChat.bussinesscenter.BussinessCenter;
import com.nandi.cqdisaster.AnyChat.bussinesscenter.UserItem;

public class UserAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	ArrayList<UserItem> mUserDatas;
	public UserAdapter(Context context,ArrayList<UserItem> mUserDatas) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mUserDatas= mUserDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		UserItem userItem=mUserDatas.get(position);
		if (convertView == null) {
			View view = mLayoutInflater.inflate(R.layout.user_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.image_userimage);
			viewHolder.textName = (TextView) view
					.findViewById(R.id.txt_username);
			viewHolder.textId = (TextView) view.findViewById(R.id.txt_userid);
			viewHolder.textAddress = (TextView) view.findViewById(R.id.txt_user_ipaddress);
			convertView = view;
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		
		if(userItem!=null)
		{
			int userId=userItem.getUserId();
			if(userId==BussinessCenter.selfUserId)
				convertView.setBackgroundResource(R.drawable.user_self_bg);
			else
				convertView.setBackgroundResource(R.drawable.com_btn_click);
			viewHolder.textId.setText("userId: "+String.valueOf(userId));
			viewHolder.textName.setText(userItem.getUserName());
			viewHolder.textAddress.setText(userItem.getIp());
		
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView textName;
		public TextView textId;
		public TextView textAddress;
	}

}
