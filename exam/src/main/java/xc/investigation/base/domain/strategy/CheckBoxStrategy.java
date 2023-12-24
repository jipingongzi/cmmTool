package xc.investigation.base.domain.strategy;

import xc.investigation.base.dto.ExamQuestionDto;

import java.util.List;

public class CheckBoxStrategy extends JudgeStrategy{
    @Override
    protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer) {
        return null;
    }
}
