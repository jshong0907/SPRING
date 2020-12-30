package com.example.restapi.light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.visit.Visit;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping(value = "/led")
public class LightController {
	
	@Autowired
	private LightRepository lightRep; 
	
	@GetMapping(value="/list")
	public Iterable<Light> list(){
		return lightRep.findAll();
	}
	
    //id로 조명 커스텀 값 가져오기
	@GetMapping(value = "/status/{user_id}")
	public Object getId(@PathVariable String user_id) {
		
		return getLight(user_id).get("customInfo");
	}
	
	//조명 커스텀
	@RequestMapping(value="/custom", method = RequestMethod.POST)
	public Boolean custom(@RequestBody Map<String, Object> params) {
		
		try {
		
			String user_id = params.get("user_id").toString();
			newCustom(user_id, params);
			
			return true;
		} catch (Exception e) {
			return false;			
		}
	}

	//조명 설정 변경
	@RequestMapping(value="/modify/{user_id}", method = RequestMethod.PUT)
	public Boolean modify(@PathVariable String user_id, @RequestBody Map<String, Object> params) {
		
		try {				
			lightRep.deleteByUserId(user_id);
			
			newCustom(user_id, params);
			
			return true;
		} catch (Exception e) {
			return false;			
		}
	}
	
	//Rpi로 request 보내기
	@GetMapping(value = "/rpi/{user_id}")
	public Map<String, Object> getLight(@PathVariable String user_id) {

		HashMap<String,Object> map = new HashMap<String,Object>();
		List list = new ArrayList();
		
		for (int i = 0; i < lightRep.findByUserId(user_id).size(); i++) {
			Light light = lightRep.findByUserId(user_id).get(i);
			String custom = light.getLight_custom();
			list.add(custom);
		}		
		
		map.put("customInfo", list);
		
		return map;
	}
	
	//새로운 조명 커스텀
	public void newCustom(String user_id, Map<String, Object> params) {
		
		Object custom = params.get("light_custom");
		
		Map<String, Object> room = (Map<String, Object>) custom;
		List list = new ArrayList();
		
		if(Integer.valueOf(room.get("kitchen").toString()) == 1) {
			Light light = new Light();
			light.setUser_id(user_id);
			light.setLight_custom("kitchen");
			lightRep.save(light);
			list.add(light);
		}
		if(Integer.valueOf(room.get("livingroom").toString()) == 1) {
			Light light = new Light();
			light.setUser_id(user_id);
			light.setLight_custom("livingroom");
			lightRep.save(light);
			list.add(light);
		}
		if(Integer.valueOf(room.get("bedroom").toString()) == 1) {
			Light light = new Light();
			light.setUser_id(user_id);
			light.setLight_custom("bedroom");
			lightRep.save(light);
			list.add(light);
		}
		
	}
}
