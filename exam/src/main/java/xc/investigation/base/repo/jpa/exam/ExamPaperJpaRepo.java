package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.constant.domain.ExamPaperStatus;
import xc.investigation.base.repo.entity.exam.ExamPaperEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
public interface ExamPaperJpaRepo extends JpaRepository<ExamPaperEntity,Long> {

    Optional<ExamPaperEntity> findByBatchIdAndTitle(Long batchId, String title);

    List<ExamPaperEntity> findByBatchIdAndStatusIn(Long batchId, Collection<ExamPaperStatus> statuses);
}
