package ro.unitbv.pclp2.quiz.service;

import ro.unitbv.pclp2.quiz.model.GradeInterval;
import ro.unitbv.pclp2.quiz.model.Question;
import ro.unitbv.pclp2.quiz.model.Quiz;
import ro.unitbv.pclp2.quiz.model.QuizMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestJsonApp {

    public static void main(String[] args) throws IOException {

        List<GradeInterval> intervals = new ArrayList<>();
        intervals.add(new GradeInterval(1, 0, 9.99));
        intervals.add(new GradeInterval(2, 10, 19.99));
        intervals.add(new GradeInterval(3, 20, 29.99));
        intervals.add(new GradeInterval(4, 30, 39.99));
        intervals.add(new GradeInterval(5, 40, 49.99));
        intervals.add(new GradeInterval(6, 50, 59.99));
        intervals.add(new GradeInterval(7, 60, 69.99));
        intervals.add(new GradeInterval(8, 70, 79.99));
        intervals.add(new GradeInterval(9, 80, 89.99));
        intervals.add(new GradeInterval(10, 90, 100));

        QuizMetadata metadata = new QuizMetadata(
                "Test introductiv Java",
                "Programare Java",
                intervals
        );

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                1,
                "Care cuvant-cheie este folosit pentru mostenire in Java?",
                List.of("implements", "extends", "inherits", "super"),
                2,
                2.5
        ));

        Quiz quiz = new Quiz(metadata, questions);

        QuizFileService service = new QuizFileService();

        service.saveQuiz(quiz, "test-java.json");

        System.out.println("Testul a fost salvat cu succes!");
    }
}