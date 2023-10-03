package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamQuestionOptionEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
public interface ExamQuestionOptionJpaRepo extends JpaRepository<ExamQuestionOptionEntity,Long> {

    Optional<ExamQuestionOptionEntity> findByQuestionIdAndTitle(Long questionId, String title);

    List<ExamQuestionOptionEntity> findByQuestionId(Long questionId);

    List<ExamQuestionOptionEntity> findByQuestionIdIn(Collection<Long> questionIdList);
}
