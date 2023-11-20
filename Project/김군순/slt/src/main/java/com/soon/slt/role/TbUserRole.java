package com.soon.slt.role;

import lombok.Getter;

@Getter
public enum TbUserRole {

	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");

	private TbUserRole(String value) {
		this.value = value;
	}
	
	private String value;

}
