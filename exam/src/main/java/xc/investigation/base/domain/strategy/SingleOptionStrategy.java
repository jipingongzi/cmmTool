package xc.investigation.base.domain.strategy;

import xc.investigation.base.dto.ExamQuestionDto;

import java.util.List;

public class SingleOptionStrategy extends JudgeStrategy{
    @Override
    protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer) {
        return null;
    }
}
