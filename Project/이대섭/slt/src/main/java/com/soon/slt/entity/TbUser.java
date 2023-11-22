package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class TbUser {

	@Id
	public String userEmail;

	public String userPw, userNick;

	public LocalDateTime joinedAt;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbBoard> tbBoardList;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbComment> tbCommentList;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbLikes> tbLikesList;

	
}
