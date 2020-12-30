package com.example.restapi.member;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping(value = "/member")
public class MemberController {
	
	@Autowired
	private MemberRepository memberRep;
	
	@GetMapping(value="/list")
	public Iterable<Member> list() throws Exception {
		return memberRep.findAll();
	}
	
    //id로 테이블 값 가져오기
	@GetMapping(value = "/detail/{user_Id}")
	public Optional<Member> getId(@PathVariable String user_Id) throws Exception{

		return memberRep.findById(user_Id);
	}
	
	//회원가입
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public Boolean save(@RequestBody Member member) throws Exception {	
		
		try {
			//id 중복인 경우
			memberRep.findById(member.getUser_Id()).get();
			
			return false;
			
		} catch (Exception e) {
			
			//id 중복이 아닌 경우
			String encryptPw = BCrypt.hashpw(member.getUser_Pw(),BCrypt.gensalt());
			member.setUser_Pw(encryptPw);
			memberRep.save(member);
			    
			return true;
		}
	}
	
	//로그인
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public HttpStatus login (@RequestBody Member member) {    //HTTP요청의 내용을 객체에 매핑하기 위해 @RequestBody 를 설정.
		
		String user_id = member.getUser_Id();
		String user_pw = member.getUser_Pw();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    //비밀번호 암호화
	    
		try {
			memberRep.findById(user_id);
			String pw = getPw(user_id);
			
			if (passwordEncoder.matches(user_pw, pw)) {
				return HttpStatus.OK;
			} else {
				
				return HttpStatus.UNAUTHORIZED;
			}
			
		} catch (Exception e) {
			return HttpStatus.NOT_FOUND;
		}
	}
	
	//회원정보 변경
	@RequestMapping(value = "/modify/{user_id}", method = RequestMethod.PUT)
	public Boolean modify(@PathVariable String user_id, @RequestBody Member member) throws Exception {

		try {
			String encryptPw = BCrypt.hashpw(member.getUser_Pw(),BCrypt.gensalt());
			member.setUser_Pw(encryptPw);
			memberRep.save(member);
			
			return true;
			
		} catch (Exception e) {
			
			return false;
		}
		
	}
	
	//회원탈퇴
	@DeleteMapping(value="/deluser/{user_id}")
	public Boolean delete(@PathVariable String user_id) throws Exception {
		
		try {
			memberRep.deleteById(user_id);
			
			return true;
			
		} catch (Exception e) {
			//아이디가 없는 경우
			return false;
		}
	}
	
    //id로 비밀번호 값 가져오기
	public String getPw(String user_Id) throws Exception {

		Member member = memberRep.findById(user_Id).get();
		String password = member.getUser_Pw();
		
		return password;
	}

}
