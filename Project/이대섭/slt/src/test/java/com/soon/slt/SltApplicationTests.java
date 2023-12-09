package com.soon.slt;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.soon.slt.entity.TbBoard;
import com.soon.slt.repository.TbBoardRepository;

@SpringBootTest
class SltApplicationTests {

	@Autowired
	private TbBoardRepository tbboardRepository;
	
	
	@Test
	void contextLoads() {
		TbBoard b1 = new TbBoard();
		b1.setBdTitle("제목테스트1");
		b1.setBdCategory("자유");
		b1.setBdContent("내용테스트1");
		b1.setBdViews(32);
		b1.setCreatedAt(LocalDateTime.now());
		this.tbboardRepository.save(b1);
		
		TbBoard b2 = new TbBoard();
		b2.setBdTitle("제목테스트2");
		b2.setBdCategory("건의");
		b2.setBdContent("내용테스트2");
		b2.setBdViews(32);
		b2.setCreatedAt(LocalDateTime.now());
		this.tbboardRepository.save(b2);
	}

}
