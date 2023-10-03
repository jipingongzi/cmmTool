package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamPaperQuestionOptionTreeEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface ExamPaperQuestionOptionTreeJpaRepo extends JpaRepository<ExamPaperQuestionOptionTreeEntity,Long> {

    List<ExamPaperQuestionOptionTreeEntity> findByQuestionTreeId(Long treeId);

    List<ExamPaperQuestionOptionTreeEntity> findByQuestionTreeIdIn(Collection<Long> treeIds);

}
