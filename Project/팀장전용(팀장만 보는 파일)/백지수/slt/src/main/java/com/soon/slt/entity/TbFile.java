package com.soon.slt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class TbFile {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String fileIdx;

	public String fileName, fileOriName, fileThumbName, fileExt, fileCategory;

	public Integer fileSize;

	public LocalDateTime uploadedAt;

	@ManyToOne
	public TbBoard tbBoard;

}
