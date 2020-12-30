package com.example.restapi.light;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("Light")
public interface LightRepository extends CrudRepository<Light, String> {
	
	
	@Query(value = "select * from light b where b.user_id like %?1%", nativeQuery = true)
    List<Light> findByUserId(String user_id);
	
	@Modifying
	@Transactional
	@Query(value = "delete from light b where b.user_id like %?1%", nativeQuery = true)
    public void deleteByUserId(String user_id);


}
