package ro.unitbv.pclp2.quiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ro.unitbv.pclp2.quiz.model.Quiz;

import java.io.File;
import java.io.IOException;

public class QuizFileService {

    private static final String TESTS_DIRECTORY = "tests";

    private final ObjectMapper objectMapper;

    public QuizFileService() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveQuiz(Quiz quiz, String fileName) throws IOException {

        File directory = new File(TESTS_DIRECTORY);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        objectMapper.writeValue(file, quiz);
    }

    public Quiz loadQuiz(String fileName) throws IOException {

        File file = new File(TESTS_DIRECTORY, fileName);

        return objectMapper.readValue(file, Quiz.class);
    }

}