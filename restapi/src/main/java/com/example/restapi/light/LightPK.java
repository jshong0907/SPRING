package com.example.restapi.light;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;

public class LightPK implements Serializable {
	
    @EqualsAndHashCode.Include
    @Id
    private String user_id;

    @EqualsAndHashCode.Include
    @Id
    private String light_custom;

}
