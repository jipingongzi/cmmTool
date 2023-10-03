package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamQuestionInstanceFileEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface ExamQuestionInstanceFileJpaRepo extends JpaRepository<ExamQuestionInstanceFileEntity,Long> {

    List<ExamQuestionInstanceFileEntity> findByQuestionInstanceId(Long questionInstanceId);

    List<ExamQuestionInstanceFileEntity> findByQuestionInstanceIdIn(Collection<Long> questionInstanceId);

    void deleteByQuestionInstanceIdIn(Collection<Long> questionInstanceIdList);
}
