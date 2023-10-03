package xc.investigation.base.repo.jpa.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.exam.ExamUserGroupMappingEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface ExamUserGroupMappingJpaRepo extends JpaRepository<ExamUserGroupMappingEntity,Long> {

    List<ExamUserGroupMappingEntity> findByPaperId(Long paperId);

    void deleteByPaperIdAndUserGroupIdIn(Long paperId, Collection<Long> groupIdList);

}
