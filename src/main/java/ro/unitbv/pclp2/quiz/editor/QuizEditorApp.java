package ro.unitbv.pclp2.quiz.editor;

import ro.unitbv.pclp2.quiz.model.GradeInterval;
import ro.unitbv.pclp2.quiz.model.Question;
import ro.unitbv.pclp2.quiz.model.Quiz;
import ro.unitbv.pclp2.quiz.model.QuizMetadata;
import ro.unitbv.pclp2.quiz.service.QuizFileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizEditorApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final QuizFileService service = new QuizFileService();

    public static void main(String[] args) throws IOException {

        System.out.println("=== EDITOR TESTE GRILA ===");
        System.out.println("1. Creeaza test nou");
        System.out.println("2. Incarca test existent");
        System.out.print("Alege optiunea: ");

        int option = readNumber(1, 2);

        Quiz quiz;
        String fileName;

        if (option == 1) {
            quiz = createQuiz();
            fileName = generateFileName(quiz.getMetadata().getTestName());
        } else {
            System.out.print("Introdu numele fisierului JSON: ");
            fileName = scanner.nextLine();
            quiz = service.loadQuiz(fileName);
            System.out.println("Test incarcat cu succes!");
        }

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== MENIU EDITARE ===");
            System.out.println("1. Afiseaza intrebari");
            System.out.println("2. Adauga intrebare");
            System.out.println("3. Modifica intrebare");
            System.out.println("4. Sterge intrebare");
            System.out.println("5. Modifica metadate");
            System.out.println("6. Salveaza si iesi");
            System.out.print("Alege optiunea: ");

            int choice = readNumber(1, 6);

            switch (choice) {
                case 1:
                    showQuestions(quiz);
                    break;
                case 2:
                    addQuestion(quiz);
                    break;
                case 3:
                    editQuestion(quiz);
                    break;
                case 4:
                    deleteQuestion(quiz);
                    break;
                case 5:
                    editMetadata(quiz);
                    fileName = generateFileName(quiz.getMetadata().getTestName());
                    break;
                case 6:
                    service.saveQuiz(quiz, fileName);
                    System.out.println("Test salvat in fisierul: " + fileName);
                    running = false;
                    break;
            }
        }
    }

    private static Quiz createQuiz() {

        String testName;

        while (true) {
            System.out.print("Numele testului: ");
            testName = scanner.nextLine();

            if (testName.isBlank()) {
                System.out.println("Numele testului nu poate fi gol.");
                continue;
            }

            if (testNameAlreadyExists(testName)) {
                System.out.println("Exista deja un test cu acest nume.");
                continue;
            }

            break;
        }

        System.out.print("Disciplina: ");
        String subject = scanner.nextLine();

        List<GradeInterval> intervals = createDefaultGradeIntervals();
        List<Question> questions = new ArrayList<>();

        System.out.print("Cate intrebari vrei sa adaugi? ");
        int numberOfQuestions = readNumber(1, 100);

        for (int i = 1; i <= numberOfQuestions; i++) {
            questions.add(readQuestion(i));
        }

        QuizMetadata metadata = new QuizMetadata(testName, subject, intervals);
        return new Quiz(metadata, questions);
    }

    private static void showQuestions(Quiz quiz) {

        System.out.println();
        System.out.println("Test: " + quiz.getMetadata().getTestName());
        System.out.println("Disciplina: " + quiz.getMetadata().getSubject());

        if (quiz.getQuestions().isEmpty()) {
            System.out.println("Nu exista intrebari.");
            return;
        }

        for (Question question : quiz.getQuestions()) {
            System.out.println();
            System.out.println("Intrebarea " + question.getId() + ": " + question.getText());

            for (int i = 0; i < question.getOptions().size(); i++) {
                System.out.println((i + 1) + ". " + question.getOptions().get(i));
            }

            System.out.println("Raspuns corect: " + question.getCorrectOption());
            System.out.println("Punctaj: " + question.getScore());
        }
    }

    private static void addQuestion(Quiz quiz) {
        int id = quiz.getQuestions().size() + 1;
        Question question = readQuestion(id);
        quiz.getQuestions().add(question);
        System.out.println("Intrebarea a fost adaugata.");
    }

    private static void editQuestion(Quiz quiz) {

        if (quiz.getQuestions().isEmpty()) {
            System.out.println("Nu exista intrebari de modificat.");
            return;
        }

        showQuestions(quiz);

        System.out.print("Alege id-ul intrebarii de modificat: ");
        int id = readNumber(1, quiz.getQuestions().size());

        Question newQuestion = readQuestion(id);
        quiz.getQuestions().set(id - 1, newQuestion);

        System.out.println("Intrebarea a fost modificata.");
    }

    private static void deleteQuestion(Quiz quiz) {

        if (quiz.getQuestions().isEmpty()) {
            System.out.println("Nu exista intrebari de sters.");
            return;
        }

        showQuestions(quiz);

        System.out.print("Alege id-ul intrebarii de sters: ");
        int id = readNumber(1, quiz.getQuestions().size());

        quiz.getQuestions().remove(id - 1);

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            quiz.getQuestions().get(i).setId(i + 1);
        }

        System.out.println("Intrebarea a fost stearsa.");
    }

    private static void editMetadata(Quiz quiz) {

        System.out.print("Noul nume al testului: ");
        String newName = scanner.nextLine();

        System.out.print("Noua disciplina: ");
        String newSubject = scanner.nextLine();

        quiz.getMetadata().setTestName(newName);
        quiz.getMetadata().setSubject(newSubject);
        quiz.getMetadata().setGradeIntervals(createDefaultGradeIntervals());

        System.out.println("Metadatele au fost modificate.");
    }

    private static Question readQuestion(int id) {

        System.out.println();
        System.out.println("Intrebarea " + id);

        String text;

        while (true) {
            System.out.print("Enunt: ");
            text = scanner.nextLine();

            if (!text.isBlank()) {
                break;
            }

            System.out.println("Enuntul nu poate fi gol.");
        }

        System.out.print("Cate variante de raspuns? ");
        int numberOfOptions = readNumber(2, 10);

        List<String> options = new ArrayList<>();

        for (int i = 1; i <= numberOfOptions; i++) {
            String option;

            while (true) {
                System.out.print("Varianta " + i + ": ");
                option = scanner.nextLine();

                if (!option.isBlank()) {
                    break;
                }

                System.out.println("Varianta nu poate fi goala.");
            }

            options.add(option);
        }

        System.out.print("Numarul variantei corecte: ");
        int correctOption = readNumber(1, numberOfOptions);

        System.out.print("Punctaj: ");
        double score = readPositiveDouble();

        return new Question(id, text, options, correctOption, score);
    }

    private static int readNumber(int min, int max) {

        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine());

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

    private static double readPositiveDouble() {

        while (true) {
            try {
                double number = Double.parseDouble(scanner.nextLine());

                if (number <= 0) {
                    System.out.print("Punctajul trebuie sa fie pozitiv: ");
                    continue;
                }

                return number;

            } catch (NumberFormatException e) {
                System.out.print("Introdu un numar valid: ");
            }
        }
    }

    private static List<GradeInterval> createDefaultGradeIntervals() {

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

        return intervals;
    }

    private static String generateFileName(String testName) {
        return testName.toLowerCase()
                .replace(" ", "-")
                .replace("/", "-")
                + ".json";
    }

    private static boolean testNameAlreadyExists(String testName) {

        File folder = new File("tests");
        File[] files = folder.listFiles();

        if (files == null) {
            return false;
        }

        String wantedFileName = generateFileName(testName);

        for (File file : files) {
            if (file.getName().equalsIgnoreCase(wantedFileName)) {
                return true;
            }
        }

        return false;
    }
}