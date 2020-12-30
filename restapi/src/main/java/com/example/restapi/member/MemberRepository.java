package com.example.restapi.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("Member")
public interface MemberRepository extends CrudRepository<Member, String>{
	
}
