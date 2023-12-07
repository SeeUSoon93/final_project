package com.soon.slt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TbLikes {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String likeIdx;

	@ManyToOne
	public TbBoard tbBoard;

	@ManyToOne
	public TbUser tbUser;

	public LocalDateTime createdAt;

}
