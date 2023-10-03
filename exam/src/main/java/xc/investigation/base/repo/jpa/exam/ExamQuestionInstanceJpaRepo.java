package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamQuestionInstanceEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface ExamQuestionInstanceJpaRepo extends JpaRepository<ExamQuestionInstanceEntity,Long> {

    List<ExamQuestionInstanceEntity> findByPaperInstanceId(Long paperInstanceId);

    List<ExamQuestionInstanceEntity> findByPaperInstanceIdIn(Collection<Long> paperInstanceIds);
}
