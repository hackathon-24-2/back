package team2.summary.dto;

import team2.summary.domain.OxQuiz;

import java.util.List;

public class SummaryResponse {
    private String summary;
    private List<OxQuiz> oxQuizzes;

    // Getters and Setters
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<OxQuiz> getOxQuizzes() {
        return oxQuizzes;
    }

    public void setOxQuizzes(List<OxQuiz> oxQuizzes) {
        this.oxQuizzes = oxQuizzes;
    }
}