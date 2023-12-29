package xc.investigation.base.domain.strategy;

import xc.investigation.base.dto.ExamQuestionDto;

import java.util.List;

public class SingleOptionStrategy extends JudgeStrategy{
    @Override
    protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer) {
        if(userAnswer.size() > 1){
            throw new RuntimeException("Answer should be one");
        }
        return question.getTitle().equals(userAnswer.get(0));
    }
}
