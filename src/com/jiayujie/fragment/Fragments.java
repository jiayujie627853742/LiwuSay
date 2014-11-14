package com.jiayujie.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jiayujie.adapter.MyAdapter;
import com.jiayujie.android_customeselector.R;
import com.jiayujie.database.DBManager;
import com.jiayujie.entity.TestEntity;
import com.jiayujie.selector.CustomerSelect;
import com.jiayujie.util.JsonUtils;
import com.jiayujie.volleyImageDownLoad.ImageCacheClass;
import com.jiayujie.volleyImageDownLoad.LruCacheClass;
import com.jiayujie.volleyImageDownLoad.QueueClass;
import com.jiayujie.webview.WebViewActivity;

public class Fragments extends Fragment {

	private RequestQueue queue;
	
	private DBManager manager;//数据库Manager

	private Handler  handler = new Handler(){
		

		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				
				Object[] obj = (Object[]) msg.obj;
				
				final List<TestEntity> list=(List<TestEntity>)obj[0];
				
				String url = (String) obj[1];
				
//				List<Map<String, Object>>  lists= new ArrayList<Map<String,Object>>();
//				for(int i = 0;i<list.size();i++){
//					Map<String , Object>  map = new HashMap<String, Object>();
//					TestEntity entity  =list.get(i);
//					map.put("create_at", entity.getCreatedAt());
//					map.put("update", entity.getUpdatedAt());
//					map.put("id", entity.getId());
//					lists.add(map);
//				}
//				
//				SimpleAdapter adapter = new SimpleAdapter(getActivity(), lists, R.layout.fragment_item, new String[]{"create_at","update","id"}, new int[]{R.id.create_time,R.id.update_time,R.id.id});
				
				MyAdapter adapter = new MyAdapter();
				adapter.init(getActivity(), list,QueueClass.getInterface(getActivity()),ImageCacheClass.getInstance(LruCacheClass.getInstance(0)));
				
				listView.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
				
				//在这里将数据写入数据库
				
				
				for(int i = 0;i<list.size();i++){
					TestEntity entity = list.get(i);
					
					String sql = "insert into  liwu(id,type,title,content,imageurl)  values(?,?,?,?,?)";
					
					Object [] bindArgs = {entity.getId(),url,entity.getTitle(),entity.getShareMsg(),entity.getCoverImageUrl()};
					
					manager.executeSQL(sql, bindArgs);
					
				}
				
//				String sql = "select * from liwu";
//				
//				Cursor cursor = manager.queryCursor(sql, null);
//				if (cursor!=null) {
//					while(cursor.moveToNext()){
////					Log.d("","======cursor"+cursor.getInt(cursor.getColumnIndex("_id")));
//						Log.d("","======cursor"+cursor.getInt(cursor.getColumnIndex("id")));
//						Log.d("","======cursor"+cursor.getString(cursor.getColumnIndex("type")));
//						Log.d("","======cursor"+cursor.getString(cursor.getColumnIndex("title")));
//						Log.d("","======cursor"+cursor.getString(cursor.getColumnIndex("content")));
//						Log.d("","======cursor"+cursor.getString(cursor.getColumnIndex("imageurl")));
//					}
//					cursor.close();
//				}
				
				
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						CustomerSelect select =(CustomerSelect)getActivity().findViewById(R.id.customerSelect);
						
						boolean event = select.onTouchEvent(null);
						if (event) {
							return;
						}
						
						ComponentName comp = new ComponentName(getActivity(), WebViewActivity.class);
						Intent intent  = new Intent();
						
						intent.putExtra("url", list.get(position).getContentUrl());
						
						intent.setComponent(comp);
						
						startActivity(intent);
						
					}
				});
				
			}
		};
		
	};

	private ListView listView;

	private  ProgressBar progressBar;

	private String url;
	
	private List<TestEntity> jsonData;//数据集合

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		progressBar = (ProgressBar) getActivity().findViewById(R.id.main_progressbar);
		manager = new DBManager(getActivity());
		jsonData =  new ArrayList<TestEntity>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View   view  =inflater.inflate(R.layout.fragment, container, false);
		
		progressBar.setVisibility(View.VISIBLE);
		
		listView = (ListView)view.findViewById(R.id.fragment_listview);
		
		Bundle bundle = getArguments();
		
		url = bundle.getString("url");
		
		
		//数据库查询更新

		String sql = "select * from liwu where type=?";

		final Cursor cursor = manager.queryCursor(sql, new String[] {url});
		Log.i("", "======cursorColumnCount"+cursor.getCount());
		if (cursor!=null&&cursor.getCount()>0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						while (cursor.moveToNext()) {
						TestEntity entity = new TestEntity();
						entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
						entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
						entity.setShareMsg(cursor.getString(cursor.getColumnIndex("content")));
						entity.setCoverImageUrl(cursor.getString(cursor.getColumnIndex("imageurl")));
						jsonData.add(entity);
						}
						cursor.close();
						sendMessage();
					}
				}).start();
		}else {
			
			queue = Volley.newRequestQueue(getActivity());
			
			queue.add(new JsonObjectRequest(Request.Method.GET,   url, null, new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject obj) {
					if (obj!=null) {
						jsonData = JsonUtils.parseJsonData(obj);
						sendMessage();
					}else {
						if (getActivity()!=null) {
							Toast.makeText(getActivity(), "数据解析失败", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}, new Response.ErrorListener() {
				
				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO 自动生成的方法存根
					if (getActivity()!=null) {
						Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
					}
				}
			}));
		}
		
		
		
		return view;
	} 
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}
	public void sendMessage() {
		Message msg = handler.obtainMessage(1);
		Object []  object = new Object[2];
		object[0]=jsonData;
		object[1] = url;
		msg.obj = object;
		handler.sendMessage(msg);
	}
}
