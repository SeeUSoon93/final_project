package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TbSignlang {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long slangIdx;

	public String slangCategory, slangVideo, slangThumb;

	@Lob // 데이터베이스의 TEXT, CLOB (Character Large Object) 또는 해당 데이터베이스의 대응되는 큰 텍스트 데이터 타입으로 매핑
	public String slangText;
}
