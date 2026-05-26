package ro.unitbv.pclp2.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizMetadata {

    private String testName;
    private String subject;
    private List<GradeInterval> gradeIntervals;

}