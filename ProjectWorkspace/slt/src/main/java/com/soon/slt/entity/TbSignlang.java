package com.soon.slt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class TbSignlang {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String slangIdx;

	public String slangCategory, slangVideo;

	@Lob // 데이터베이스의 TEXT, CLOB (Character Large Object) 또는 해당 데이터베이스의 대응되는 큰 텍스트 데이터 타입으로
			// 매핑
	public String slangText;
}
