package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamPaperInstanceFileEntity;

import java.util.List;

/**
 * @author ibm
 */
public interface ExamPaperInstanceFileJpaRepo extends JpaRepository<ExamPaperInstanceFileEntity,Long> {

    List<ExamPaperInstanceFileEntity> findByPaperInstanceId(Long paperInstanceId);

    void deleteByPaperInstanceId(Long paperInstanceId);
}
