package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TbUser {

	@Id
	@Email(message = "올바른 이메일 주소를 입력해주세요.")
	@NotBlank(message = "이메일 주소를 입력해주세요.")
	public String userEmail;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	public String userPw;
	
	@Column(unique = true)
	@NotBlank(message = "닉네임을 입력해주세요.")
	public String userNick;

	public LocalDateTime joinedAt;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbBoard> tbBoardList;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbComment> tbCommentList;

	@OneToMany(mappedBy = "tbUser", cascade = CascadeType.REMOVE)
	public List<TbLikes> tbLikesList;

	
}
