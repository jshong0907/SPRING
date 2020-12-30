package com.example.restapi.visit;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VisitRepository extends CrudRepository<Visit, Integer> {

	@Query(value = "select * from visit b where b.user_id like %?1%", nativeQuery = true)
    List<Visit> findByUserId(String user_id);
} 
