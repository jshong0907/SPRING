package com.example.restapi.light;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor 
@IdClass(LightPK.class)
public class Light {
	
	@Id
	private String user_id;
	@Id
	private String light_custom;	
}
