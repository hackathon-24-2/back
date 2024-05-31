package team2.summary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.summary.dto.SummaryResponse;
import team2.summary.service.GptService;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final GptService gptService;

    public ChatController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(@RequestBody String prompt) {
        try {
            String modifiedPrompt = prompt + " 이걸 5문장으로 줄여줘서 첫번째 줄에 넣어줘. 그리고 추가적으로 5문제의 OX퀴즈를 만들고 'OX퀴즈:'라고 항상 먼저 지칭을 해주고 그 다음에 문제번호와 문제 내용과 정답을 알려줘.";
            SummaryResponse summaryResponse = gptService.getChatResponse(modifiedPrompt);
            return ResponseEntity.ok(summaryResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}