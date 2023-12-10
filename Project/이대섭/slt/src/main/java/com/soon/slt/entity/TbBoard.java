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

	// GenerationType.UUID 로 설정하면 UUID를 사용해 기본키 생성
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long bdIdx;

	// DB에서 이미 길이제한을 설정했으면, @Column 어노테이션의 legth속성은 실제 DB에 영향을 미치지 않음
	public String bdCategory, bdTitle;

	@Lob // 데이터베이스의 TEXT, CLOB (Character Large Object) 또는 해당 데이터베이스의 대응되는 큰 텍스트 데이터 타입으로 매핑
	public String bdContent;

	public int bdViews;

	@ManyToMany
	private Set<TbUser> bdLikes = new HashSet<>(); // 좋아요 기본값 0으로 설정

	public LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "user_email")
	public TbUser tbUser;

	@OneToMany(mappedBy = "tbBoard", cascade = CascadeType.REMOVE)
	public List<TbComment> tbCommentList;


	@OneToMany(mappedBy = "tbBoard", cascade = CascadeType.REMOVE)
	public List<TbLikes> tbLikesList;

}
