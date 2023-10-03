package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamQuestionEntity;

import java.util.Optional;

/**
 * @author ibm
 */
public interface ExamQuestionJpaRepo extends JpaRepository<ExamQuestionEntity,Long> {

    Optional<ExamQuestionEntity> findByPaperIdAndTitle(Long paperId,String title);

}
