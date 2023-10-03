package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xc.investigation.base.repo.entity.exam.ExamPaperInstanceEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author ibm
 */
public interface ExamPaperInstanceJpaRepo extends JpaRepository<ExamPaperInstanceEntity,Long> {

    List<ExamPaperInstanceEntity> findByBankCodeOrderByCreateTimeDesc(String bankCode);

    List<ExamPaperInstanceEntity> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<ExamPaperInstanceEntity> findByPaperIdAndUserId(Long paperId, Long userId);

    List<ExamPaperInstanceEntity> findByPaperIdAndUserIdIn(Long paperId, Collection<Long> userId);

    List<ExamPaperInstanceEntity> findByPaperId(Long paperId);

    @Query("SELECT new map(epi.userId AS userId,COUNT(epi.id) AS paperInstanceCount) " +
           "FROM ExamPaperInstanceEntity epi " +
           "WHERE epi.userId IN (:userIdList) " +
           "GROUP BY epi.userId")
    List<Map<String,Object>> findUserPaperInstanceCount(@Param("userIdList") Collection<Long> userIdList);
}
