package com.jiayujie.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiayujie.entity.TestEntity;

public class JsonUtils {

	public static List<TestEntity>  parseJsonData(JSONObject  object){
		if (object==null) {
			return null;
		}
		List<TestEntity> list = new ArrayList<TestEntity>();
		
		try {
			JSONObject  obj = object.getJSONObject("data");
			JSONArray  array = obj.getJSONArray("items");
			for(int i = 0;i<array.length();i++){
				TestEntity  entity = new TestEntity();
				
				JSONObject  js= array.getJSONObject(i);
				
				entity.setContentUrl(js.optString("content_url"));
				entity.setCreatedAt(js.optLong("created_at"));
				entity.setUpdatedAt(js.optLong("updated_at"));
				entity.setId(js.optInt("id"));
				entity.setShareMsg(js.optString("share_msg"));
				entity.setTitle(js.optString("title"));
				entity.setLikedsCount(js.optInt("likes_count"));
				
				entity.setCoverImageUrl(js.optString("cover_image_url"));
				
				list.add(entity);
				
			}
			
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return list;
	}
	
}
