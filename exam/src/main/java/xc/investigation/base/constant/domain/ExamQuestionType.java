package xc.investigation.base.constant.domain;

/**
 * type of question
 * @author ibm
 */
public enum ExamQuestionType {

    /**
     * radio checkbox input
     */
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    /**
     * 不定项选择题，每个选项可以单独得分
     */
    UN_SETTLED_CHOICE,
    INPUT,
    JUDGMENT;
}
