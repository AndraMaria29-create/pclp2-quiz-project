package ro.unitbv.pclp2.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    private int id;
    private String text;
    private List<String> options;
    private int correctOption;
    private double score;

}