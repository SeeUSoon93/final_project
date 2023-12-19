package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TbBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long bdIdx;

	public String bdCategory, bdTitle;

	@Lob
	public String bdContent;

	public int bdViews;

	@ManyToMany
	Set<TbUser> bdLikes = new HashSet<>(); // 좋아요 기본값 0으로 설정

	public LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "user_email")
	public TbUser tbUser;

	@OneToMany(mappedBy = "tbBoard", cascade = CascadeType.REMOVE)
	public List<TbComment> tbCommentList;

	@OneToMany(mappedBy = "tbBoard", cascade = CascadeType.REMOVE)
	public List<TbLikes> tbLikesList;

}
