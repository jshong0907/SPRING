package com.example.restapi.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Telegram {
	
	@Value("${telegram.token}")
	private static String Token;
    @Value("${telegram.chat_id}")
    private static String chat_id;
	
    @Value("${telegram.token}")
	public void setToken(String Token) { 
		this.Token = Token; 
	}
  
    @Value("${telegram.chat_id}")
	public void setChatId(String chat_id) { 
		this.chat_id = chat_id; 
	}
    
	public static void funcTelegram(){
		
		String text = "비인가 사용자 출입!!!" ;
		
		BufferedReader in = null;
		
		 try {
			 URL obj = new URL("https://api.telegram.org/bot" + Token + "/sendmessage?chat_id=" + chat_id + "&text=" + text); // 호출할 url
			 
			 HttpURLConnection con = (HttpURLConnection)obj.openConnection();
			 con.setRequestMethod("GET");
			 in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			 String line;
			 
			 while((line = in.readLine()) != null) { // response를 차례대로 출력
				 System.out.println(line);
			 }		 
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
		 }
	}
}