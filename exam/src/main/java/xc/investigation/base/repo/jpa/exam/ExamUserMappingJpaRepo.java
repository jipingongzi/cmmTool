package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamUserMappingEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface ExamUserMappingJpaRepo extends JpaRepository<ExamUserMappingEntity,Long> {

    List<ExamUserMappingEntity> findByPaperIdAndUserIdIn(Long paperId, Collection<Long> userIdList);

    List<ExamUserMappingEntity> findByUserId(Long uerId);

    List<ExamUserMappingEntity> findByPaperId(Long paperId);

    List<ExamUserMappingEntity> findByPaperIdAndSpecial(Long uerId,Boolean special);

    void deleteByPaperIdAndUserIdIn(Long paperId,Collection<Long> userIdList);


}
