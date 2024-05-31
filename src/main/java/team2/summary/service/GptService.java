package team2.summary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import team2.summary.domain.OxQuiz;
import team2.summary.dto.SummaryResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class GptService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SummaryResponse getChatResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String content = new JSONObject(response.getBody()).getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message").getString("content");

            // Parse the response content
            return parseResponse(content);
        } else {
            throw new RuntimeException("Failed to get response from OpenAI: " + response.getBody());
        }
    }

    private SummaryResponse parseResponse(String content) {
        SummaryResponse summaryResponse = new SummaryResponse();
        List<OxQuiz> oxQuizzes = new ArrayList<>();

        // Split the response content into summary and OX quizzes
        String[] parts = content.split("OX퀴즈:");
        String summary = parts[0].trim();
        summaryResponse.setSummary(summary);

        // Parse OX quizzes
        String[] quizLines = parts[1].trim().split("\n");
        for (String line : quizLines) {
            if (line.contains("(")) {
                String question = line.substring(0, line.lastIndexOf('(')).trim();
                String answer = line.substring(line.lastIndexOf('(') + 1, line.lastIndexOf(')')).trim();
                OxQuiz oxQuiz = new OxQuiz();
                oxQuiz.setQuestion(question);
                oxQuiz.setAnswer(answer);
                oxQuizzes.add(oxQuiz);
            }
        }

        summaryResponse.setOxQuizzes(oxQuizzes);
        return summaryResponse;
    }
}