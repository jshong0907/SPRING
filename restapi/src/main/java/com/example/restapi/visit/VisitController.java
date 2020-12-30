package com.example.restapi.visit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.light.Light;
import com.example.restapi.light.LightRepository;
import com.example.restapi.member.Member;
import com.example.restapi.member.MemberRepository;
import com.example.restapi.telegram.Telegram;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping(value = "/visit")
public class VisitController {
	
	@Autowired
	private VisitRepository visitRep;
	@Autowired
	private MemberRepository memberRep;
	@Autowired
	private LightRepository lightRep;
	
	//전체 방문기록 확인 (운영상으로만 쓰임)
	@GetMapping(value="/list")
	public Iterable<Visit> list() throws Exception {
		
		return visitRep.findAll();
	}

    //id로 테이블 값(user_id, name, enter_at) 가져오기
	@GetMapping(value = "/log/{user_id}")
	public Object getId(@PathVariable String user_id) throws Exception{
		
		List<Visit> check = visitRep.findByUserId(user_id);
		
		//방문기록에 id가 없을 때
		if (check.size() == 0) {
			return null;
		}
		
		HashMap<String,String> map = new HashMap<String,String>();
		List list = new ArrayList();
		
		for (int i = 0; i < visitRep.findByUserId(user_id).size(); i++) {
			
			Visit log = new Visit();
			Visit visit = visitRep.findByUserId(user_id).get(i);
			
			String name = visit.getName();
			String time = visit.getEnter_at();
			String custom = visit.getLight_custom();
			
			log.setName(name);
			log.setEnter_at(time);
			log.setLight_custom(custom);
			
			map.put("name", name);
			map.put("time", time);
			map.put("custom", custom);
			
			list.add(map);
		}		
		return list;
	}	
	
	//detected된 id값 받아서 저장하기
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public HttpStatus save(@RequestBody Map<String, Object> params) throws JsonProcessingException {

		try {
			Object id = params.get("detected");
			Object enter = params.get("time");
			
			String user_id = id.toString();
			String enterAt = enter.toString();
			
			Member member = memberRep.findById(user_id).get();
			String name = member.getName();
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			List list = new ArrayList();
			
			for (int i = 0; i < lightRep.findByUserId(user_id).size(); i++) {
				Light light = lightRep.findByUserId(user_id).get(i);
				String custom = light.getLight_custom();
				list.add(custom);
			}	
			
			Visit visit = new Visit();
			visit.setUser_id(user_id);
			visit.setName(name);
			visit.setEnter_at(enterAt);
			visit.setLight_custom(list.toString());
			
			
			visitRep.save(visit);
			
			return HttpStatus.OK;	
			
		} catch (Exception e) {
			
			//저장되지 않은 사용자의 경우 텔레그램 알림
			Telegram telegram = new Telegram();
			telegram.funcTelegram();
	
			return HttpStatus.FORBIDDEN;
		}
	}
}
