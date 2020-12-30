package com.example.restapi.member;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	
	@Autowired
	MemberRepository repo;
//	PasswordEncoder passwordEncoder;
//	
//	@Autowired
//    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
//
//        this.repo = memberRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public Member registerUser(String user_Id, String user_Pw, String name) {
//        Optional<Member> existed = repo.findById(user_Id);
////        if(existed.isPresent()){
////            throw new User_IdExistedException(user_Id);
////        }
//        // **** 해싱하는 부분 ****
//        String encodePassword = passwordEncoder.encode(user_Pw);
//        User user = User.builder()
//                .email(email)
//                .name(name)
//                .password(encodePassword)
//                .level(1L)
//                .build();
//        return userRepository.save(user);
//    }
	
	public List getMemberList(){
		repo.findAll().forEach(mem->{
			log.info(mem.toString());
		});
		return (List) repo.findAll();
	}
	
	public void saveMember(Member member) {
		repo.save(member);
	}
	
	public void deleteMember(String user_Id) {
		repo.deleteById(user_Id);
	}
	
	
}