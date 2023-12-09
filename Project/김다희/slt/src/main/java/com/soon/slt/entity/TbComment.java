package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long cmtIdx;

	@Lob
	public String cmtContent;

	public LocalDateTime createdAt;

	@ManyToMany
	Set<TbUser> cmtLikes;

	@ManyToOne
	@JoinColumn(name = "bd_idx")
	public TbBoard tbBoard;

	@ManyToOne
	@JoinColumn(name = "user_email")
	public TbUser tbUser;

}
