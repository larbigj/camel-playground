package net.larbig.camel;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SQLBean {
	
	
	
	
	public Number dropSQL(HashMap<String, Object> pHm){
		return (Number)pHm.get("col1");
	}
	
	public String convertToJSON(HashMap<String, Object> pHm){
		Number col1 = (Number)pHm.get("col1");
		String col2 = (String)pHm.get("col2");
		TestObject to = new TestObject();
		to.setCol1(col1);
		to.setCol2(col2);
		Gson gson = new Gson();
		return gson.toJson(to);
	}
	
	
	public String convertToSQL(String json){
		Gson gson = new GsonBuilder().create();
		TestObject to = gson.fromJson(json, TestObject.class);
		StringBuffer sb = new StringBuffer();
		sb.append("insert into test_out (col1,col2) values (");
		sb.append(to.getCol1());
		sb.append(",");
		sb.append("'");
		sb.append(to.getCol2());
		sb.append("')");
		return sb.toString();
	}

}
