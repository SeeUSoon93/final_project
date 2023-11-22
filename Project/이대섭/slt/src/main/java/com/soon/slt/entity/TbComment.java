package com.soon.slt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TbComment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String cmtIdx;

	@Lob
	public String cmtContent;

	public LocalDateTime createdAt;

	public Integer cmtLikes;

	@ManyToOne
	public TbBoard tbBoard;

	@ManyToOne
	public TbUser tbUser;

}
