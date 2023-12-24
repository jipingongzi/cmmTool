package xc.investigation.base.domain.strategy;

import xc.investigation.base.dto.ExamQuestionDto;

import java.util.List;

public abstract class JudgeStrategy {

    public Boolean judge(ExamQuestionDto question, List<String> userAnswer){
        return subJudge(question, userAnswer);
    }

    abstract protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer);
}
