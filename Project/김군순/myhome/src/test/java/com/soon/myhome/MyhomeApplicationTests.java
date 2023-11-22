package com.soon.myhome;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.soon.myhome.service.BoardService;

//@SpringBootTest - SbbApplicationTests 클래스가 스프링부트 테스트 클래스임을 의미
@SpringBootTest
class MyhomeApplicationTests {
	
	// 스프링의 DI 기능으로 questionRepository 객체를 스프링이 자동으로 생성
    @Autowired
    private BoardService boardService;
	
	@Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
            String boardTitle = String.format("테스트 제목:[%03d]", i);
            String boardCon = "테스트 내용";
            this.boardService.createBoard(boardTitle, boardCon,null);
        }
    }

}
