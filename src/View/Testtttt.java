package View;

import com.google.gson.Gson;

import Data.AdditionalData;

public class Testtttt {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		AdditionalData ad = new AdditionalData();
		
		Gson gson = new Gson();
		
		String json = gson.toJson(ad);
		
		System.out.println(json);
		
		AdditionalData ad2 = gson.fromJson(json, AdditionalData.class);
		
		json = gson.toJson(ad2);
		
		System.out.println(json);
		
		System.out.println(ad2.dev_key);
	}

}
