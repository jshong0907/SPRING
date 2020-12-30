package com.example.restapi.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	@Id 
	private String user_Id;
	@Column
	private String user_Pw;
	@Column
	private String name;
	@Column
	private String email;
}
