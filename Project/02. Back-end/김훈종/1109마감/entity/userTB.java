package com.soon.slt.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class userTB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long email;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private LocalDateTime signDate;
	
	@OneToMany(mappedBy = "userTB", cascade = CascadeType.REMOVE)
	private List<boardTB> boardList;
	
	@OneToMany(mappedBy = "userTB", cascade = CascadeType.REMOVE)
	private List<repleTB> repleList;

}
