package com.soon.slt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
public class TranslateControlle {

    private final RestTemplate restTemplate;

    @GetMapping("/translate")
    public String goTranslate() {
        String fastApiUrl = "http://127.0.0.1:8000/translate"; // FastAPI 서버 주소
        String response = restTemplate.getForObject(fastApiUrl, String.class);
        // restTemplate.getForObject("요청보낼 url", "어떤 타입으로 받을 것인지")
        //
        return response;
    }
}
