package com.soon.myhome.role;

import lombok.Getter;

// 인증 후 사용자에게 부여할 권한 작성
// enum - 열거형(Enumeration)을 나타내는 자바의 데이터 유형
//        1. 상수 집합을 정의하며, 이 집합은 고정되어 변경불가능
//        2. 가독성 향상 : 각 상수는 의미 있는 이름을 가지고 있어 코드 이해가 쉬움
//        3. 열거형 상수의 타입을 지정할 수 있어 잘못된 타입의 값이 들어오는 것을 방지
//        4. 편리한 메서드 제공(values(), valueOf()등)
@Getter
public enum MemberRole {
	
	// ADMIN, USER 두가지 역할
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	
	MemberRole(String value){
		this.value = value;
	}
	
	private String value;
	
}
