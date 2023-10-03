package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamPaperQuestionTreeEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
public interface ExamPaperQuestionTreeJpaRepo extends JpaRepository<ExamPaperQuestionTreeEntity,Long> {

    List<ExamPaperQuestionTreeEntity> findByPaperId(Long paperId);

    Optional<ExamPaperQuestionTreeEntity> findByPaperIdAndQuestionId(Long paperId,Long questionId);

}
