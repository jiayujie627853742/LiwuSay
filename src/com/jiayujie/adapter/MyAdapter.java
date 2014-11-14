package com.jiayujie.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jiayujie.android_customeselector.R;
import com.jiayujie.entity.TestEntity;
import com.jiayujie.volleyImageDownLoad.ImageDownLoad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

		
		private List<TestEntity> list;
		private Context  context;
		private RequestQueue queue;
		private ImageCache  imageCache;
		
		public void init(Context  context,List<TestEntity> list,RequestQueue queue,ImageCache  imageCache){
			this.list  = list;
			this.context   = context;
			this.queue = queue;
			this.imageCache = imageCache;
		}
		
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return list.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO 自动生成的方法存根
			return list.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			
			ViewHolder holder = null;
			if (convertView==null) {
				holder = new ViewHolder();
				convertView  = LayoutInflater.from(context).inflate(R.layout.fragment_item, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.content = (TextView)convertView.findViewById(R.id.content);
//				holder.id = (TextView)convertView.findViewById(R.id.id);
				holder.img = (ImageView)convertView.findViewById(R.id.img);
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			TestEntity  entity  = list.get(position);
			
//			Date  date = new Date(entity.getCreatedAt());
//			SimpleDateFormat  dateFormat  = new SimpleDateFormat("yyyy-MM-dd E hh:mm:ss");
//			String create = dateFormat.format(date);
			
			holder.title.setText(entity.getTitle());
			holder.title.setTextSize(20);
			
//			date = new Date(entity.getUpdatedAt());
//			String update = dateFormat.format(date);
			
			holder.content.setText(entity.getShareMsg());
//			holder.id.setText(String.valueOf(entity.getId()));
			
			ImageDownLoad.downLoadImage(entity.getCoverImageUrl(), queue, context, holder.img,imageCache);
			
			return convertView;
		}
		
		private class ViewHolder{
			private ImageView  img;
			private TextView  title,content,id;
		}

}
