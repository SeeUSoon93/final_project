package com.soon.slt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class TbLikes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long likeIdx;

	@ManyToOne
	@JoinColumn(name = "bd_idx")
	public TbBoard tbBoard;

	@ManyToOne
	@JoinColumn(name = "user_email")
	public TbUser tbUser;

	public LocalDateTime createdAt;

}
