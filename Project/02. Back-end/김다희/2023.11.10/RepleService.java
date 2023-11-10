package com.soon.slt.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.soon.slt.entity.boardTB;
import com.soon.slt.entity.repleTB;
import com.soon.slt.entity.userTB;
import com.soon.slt.repository.RepleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RepleService {

	private final RepleRepository repleRepository;
	
	@Transactional
	public void repleCreate(boardTB boardTB, String repleCon, userTB userTB) {
		repleTB r = new repleTB();
		r.setRepleCon(repleCon);
		r.setBoardTB(boardTB);
		r.setUserTB(userTB);
		r.setRepleDate(LocalDateTime.now());
		this.repleRepository.save(r);
	}
}
