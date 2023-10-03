package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamBatchEntity;

/**
 * @author ibm
 */
public interface ExamBatchJpaRepo extends JpaRepository<ExamBatchEntity,Long> {

    Page<ExamBatchEntity> findByTitleLike(String title, Pageable pageable);
}
