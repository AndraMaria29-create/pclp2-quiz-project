package ro.unitbv.pclp2.quiz.service;

import ro.unitbv.pclp2.quiz.model.Quiz;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuizLoaderService {

    private final QuizFileService quizFileService = new QuizFileService();

    public Map<String, List<File>> getTestsGroupedBySubject() throws IOException {

        Map<String, List<File>> testsBySubject = new TreeMap<>();

        File testsFolder = new File("tests");
        File[] files = testsFolder.listFiles();

        if (files == null) {
            return testsBySubject;
        }

        for (File file : files) {

            if (!file.getName().endsWith(".json")) {
                continue;
            }

            Quiz quiz = quizFileService.loadQuiz(file.getName());

            String subject = quiz.getMetadata().getSubject();

            if (!testsBySubject.containsKey(subject)) {
                testsBySubject.put(subject, new ArrayList<>());
            }

            testsBySubject.get(subject).add(file);
        }

        for (List<File> subjectTests : testsBySubject.values()) {
            subjectTests.sort(Comparator.comparing(File::getName));
        }

        return testsBySubject;
    }
}