package com.jiayujie;

import java.util.ArrayList;
import java.util.List;

import android.R.drawable;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiayujie.android_customeselector.R;
import com.jiayujie.fragment.Fragments;
import com.jiayujie.selector.CustomerSelect;
import com.jiayujie.selector.SelectorItemListener;
import com.jiayujie.volleyImageDownLoad.LruCacheClass;
import com.jiayujie.volleyImageDownLoad.QueueClass;

public class MainActivity extends Activity implements SelectorItemListener {

	private FrameLayout  layout;
	private RelativeLayout  relativeLayout;
	private RadioGroup   titleGroup;
	private HorizontalScrollView  titleScrollView;
	
	private CustomerSelect customerSelect;
	
	
	
	private List<String >list ;
	
	private float  screenWidth;
	private float  screenHeight;
	
	
	
	private String[]  urls= new String [] {
			"http://api.liwushuo.com/v1/channels/14/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/5/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/10/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/9/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/6/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/17/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/2/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/3/items?offset=0&limit=20",
			"http://api.liwushuo.com/v1/channels/11/items?offset=0&limit=20"
	};
	
	
	private boolean selectorOntouch;
	
	
	
    public boolean isSelectorOntouch() {
		return selectorOntouch;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findById();
        
        //创建lruCache
        int maxSize = ((ActivityManager)this
        		.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 8*1024*1024;
        LruCacheClass.getInstance(maxSize);
        
        QueueClass.getInterface(getApplicationContext());
        
        
        list = new ArrayList<String>();
        list.add("爸妈");
        list.add("闺蜜们");
        list.add("女朋友");
        list.add("男票");
        list.add("同事");
        list.add("美物");
        list.add("手工");
        list.add("萌萌哒");
        list.add("小清新");
        
        getScreenParams();
        
        
        
        customerSelect.setList(list);
        
        customerSelect.setSelectorItemListener(this);
        
        for(int i = 0;i<list.size();i++){
        	RadioButton  radioButton = new RadioButton(this);
        	radioButton.setText(list.get(i));
        	RadioGroup.LayoutParams params  = new LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        	radioButton.setLayoutParams(params);
        	radioButton.setButtonDrawable(android.R.color.transparent);
        	radioButton.setPadding(20, 5, 20, 5);
        	radioButton.setOnClickListener(new RadioButtonListener());
        	titleGroup.addView(radioButton);
        }
        
        //默认加载第一个界面的一些参数设置
        Fragments fragment = new Fragments();
        Bundle bundle = new Bundle();
        bundle.putString("url", urls[0]);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragment_layout, fragment).commit();
        RadioButton rButton=(RadioButton)titleGroup.getChildAt(0);
        rButton.setTextColor(Color.RED);
        
    }

	private void findById() {
		layout = (FrameLayout) this.findViewById(R.id.mainlayout);
        relativeLayout = (RelativeLayout)this.findViewById(R.id.fragment_layout);
        titleGroup = (RadioGroup)this.findViewById(R.id.title_group);
        titleScrollView = (HorizontalScrollView)this.findViewById(R.id.title_scrollView);
        customerSelect = (CustomerSelect) this.findViewById(R.id.customerSelect);
	}
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	
    	 customerSelect.onTouch(ev);
    	
    	 selectorOntouch =customerSelect.onTouchEvent(ev);
    	 
    	return super.dispatchTouchEvent(ev);
    }

	private void getScreenParams() {
		// TODO 自动生成的方法存根
		WindowManager  windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
		
		Display display = windowManager.getDefaultDisplay();
		
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		screenWidth = metrics.xdpi;
		screenHeight = metrics.ydpi;
		
	}

	@Override
	public void onItemClick(int id, String title) {
		loadFragment(id);
		
		RadioButton radioButton  = (RadioButton) titleGroup.getChildAt(id);
		radioButton.setChecked(true);
		
		RadioButton  button=null;
		for(int i = 0;i<titleGroup.getChildCount();i++){
			button = (RadioButton) titleGroup.getChildAt(i);
			if (button.isChecked()) {
				button.setTextColor(Color.RED);
				loadFragment(i);
				
				 int x =  (int) button.getX();
				 int width = button.getMeasuredWidth();
				 
				 int d = x - (int)screenWidth/2-width/2;
				titleScrollView.smoothScrollTo(d, 0);
				
			}else {
				button.setTextColor(Color.BLACK);
			}
		}
	}

	private void loadFragment(int id) {
		Fragments fragment = new Fragments();
        Bundle bundle = new Bundle();
        bundle.putString("url", urls[id]);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
	}
	
	private class RadioButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int childCount  = titleGroup.getChildCount();
			RadioButton button = null;
			for(int i = 0 ;i<childCount;i++){
				button = (RadioButton) titleGroup.getChildAt(i);
				if (button.isChecked()) {
					button.setTextColor(Color.RED);
					loadFragment(i);
					
					 int x =  (int) button.getX();
					 int width = button.getMeasuredWidth();
					 
					 int d = x - (int)screenWidth/2-width/2;
					titleScrollView.smoothScrollTo(d, 0);
					
				}else {
					button.setTextColor(Color.BLACK);
				}
			}
		}
		
	}
	
}
