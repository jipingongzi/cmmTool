package xc.investigation.base.domain.strategy;

import xc.investigation.base.constant.domain.ExamQuestionType;
import xc.investigation.base.dto.ExamQuestionDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JudgeStrategy {

    private final Map<ExamQuestionType, JudgeStrategy> strategyMap = new HashMap<>();

    {
        strategyMap.put(ExamQuestionType.SINGLE_CHOICE, new SingleOptionStrategy());
        strategyMap.put(ExamQuestionType.MULTIPLE_CHOICE, new MultipleOptionStrategy());
    }

    public Boolean judge(ExamQuestionDto question, List<String> userAnswer) {
        return strategyMap.get(question.getType()).judge(question, userAnswer);
    }

    abstract protected Boolean subJudge(ExamQuestionDto question, List<String> userAnswer);
}
