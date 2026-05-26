package ro.unitbv.pclp2.quiz.runner;

import ro.unitbv.pclp2.quiz.model.GradeInterval;
import ro.unitbv.pclp2.quiz.model.Question;
import ro.unitbv.pclp2.quiz.model.Quiz;
import ro.unitbv.pclp2.quiz.service.QuizFileService;
import ro.unitbv.pclp2.quiz.service.QuizLoaderService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QuizRunnerApp {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        QuizLoaderService loaderService = new QuizLoaderService();
        Map<String, List<File>> testsBySubject = loaderService.getTestsGroupedBySubject();

        if (testsBySubject.isEmpty()) {
            System.out.println("Nu exista teste disponibile.");
            return;
        }

        System.out.println("=== DISCIPLINE DISPONIBILE ===");

        List<String> subjects = new ArrayList<>(testsBySubject.keySet());

        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }

        System.out.print("Alege disciplina: ");
        int subjectChoice = readNumber(scanner, 1, subjects.size());

        String selectedSubject = subjects.get(subjectChoice - 1);
        List<File> tests = testsBySubject.get(selectedSubject);

        System.out.println();
        System.out.println("=== TESTE DISPONIBILE PENTRU " + selectedSubject + " ===");

        for (int i = 0; i < tests.size(); i++) {
            System.out.println((i + 1) + ". " + tests.get(i).getName());
        }

        System.out.print("Alege testul: ");
        int testChoice = readNumber(scanner, 1, tests.size());

        File selectedFile = tests.get(testChoice - 1);

        QuizFileService service = new QuizFileService();
        Quiz quiz = service.loadQuiz(selectedFile.getName());

        System.out.println();
        System.out.print("Introdu nickname-ul: ");
        String nickname = scanner.nextLine();

        System.out.println();
        System.out.println("Salut, " + nickname + "!");
        System.out.println("Test: " + quiz.getMetadata().getTestName());
        System.out.println("Disciplina: " + quiz.getMetadata().getSubject());
        System.out.println();

        double totalScore = 0;
        double maxScore = 0;

        long startTime = System.currentTimeMillis();

        for (Question question : quiz.getQuestions()) {

            System.out.println(question.getText());

            for (int i = 0; i < question.getOptions().size(); i++) {
                System.out.println((i + 1) + ". " + question.getOptions().get(i));
            }

            System.out.print("Raspunsul tau: ");
            int answer = readNumber(scanner, 1, question.getOptions().size());

            if (answer == question.getCorrectOption()) {
                totalScore += question.getScore();
            }

            maxScore += question.getScore();

            System.out.println();
        }

        long endTime = System.currentTimeMillis();

        double percentage = (totalScore / maxScore) * 100;
        int grade = calculateGrade(quiz, percentage);

        long durationSeconds = (endTime - startTime) / 1000;
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;

        System.out.println("=== REZULTAT FINAL ===");
        System.out.println("Utilizator: " + nickname);
        System.out.println("Punctaj obtinut: " + totalScore);
        System.out.println("Punctaj maxim: " + maxScore);
        System.out.printf("Procent: %.2f%%\n", percentage);
        System.out.println("Nota propusa: " + grade);
        System.out.println("Durata testului: " + minutes + " minute si " + seconds + " secunde");
    }

    private static int readNumber(Scanner scanner, int min, int max) {

        int number;

        while (true) {

            try {
                number = Integer.parseInt(scanner.nextLine());

                if (number < min || number > max) {
                    System.out.print("Alegere invalida. Incearca din nou: ");
                    continue;
                }

                return number;

            } catch (NumberFormatException e) {
                System.out.print("Introdu un numar valid: ");
            }
        }
    }

    private static int calculateGrade(Quiz quiz, double percentage) {

        for (GradeInterval interval : quiz.getMetadata().getGradeIntervals()) {

            if (percentage >= interval.getMinPercentage()
                    && percentage <= interval.getMaxPercentage()) {
                return interval.getGrade();
            }
        }

        return 1;
    }
}