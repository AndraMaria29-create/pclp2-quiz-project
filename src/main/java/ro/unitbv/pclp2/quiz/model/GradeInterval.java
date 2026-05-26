package ro.unitbv.pclp2.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeInterval {

    private int grade;
    private double minPercentage;
    private double maxPercentage;

}