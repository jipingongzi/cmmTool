package xc.investigation.base.domain.strategy;

import xc.investigation.base.dto.ExamQuestionDto;

import java.util.List;

public class MultipleOptionStrategy extends JudgeStrategy{
    @Override
    protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer) {
        return userAnswer.contains(question.getTitle());
    }
}
