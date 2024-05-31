package team2.summary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.summary.service.GptService;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final GptService gptService;

    @Autowired
    public ChatController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/summary")
    public ResponseEntity<String> getSummary(@RequestBody String prompt) {
        try {
            String summary = gptService.getChatResponse(prompt + " 이걸 5문장으로 줄여줘. 그리고 추가적으로 5문제의 OX퀴즈를 만들어줘.");
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}