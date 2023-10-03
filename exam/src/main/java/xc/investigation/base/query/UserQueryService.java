package xc.investigation.base.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.dto.UserDto;
import xc.investigation.base.repo.entity.user.QUserEntity;
import xc.investigation.base.repo.entity.user.QUserGroupEntity;
import xc.investigation.base.repo.entity.user.QUserGroupMappingEntity;
import xc.investigation.base.repo.entity.user.UserEntity;
import xc.investigation.base.repo.entity.user.UserGroupEntity;
import xc.investigation.base.repo.entity.user.UserGroupMappingEntity;
import xc.investigation.base.repo.jpa.bank.BankJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperInstanceJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperJpaRepo;
import xc.investigation.base.repo.jpa.user.UserGroupJpaRepo;
import xc.investigation.base.repo.jpa.user.UserGroupMappingJpaRepo;
import xc.investigation.base.repo.jpa.user.UserJpaRepo;
import xc.investigation.base.repo.jpa.user.UserTokenJpaRepo;
import xc.investigation.base.utils.PageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@Service
public class UserQueryService {

    private final UserJpaRepo userJpaRepo;
    private final UserTokenJpaRepo userTokenJpaRepo;
    private final BankJpaRepo bankJpaRepo;
    private final ExamPaperInstanceJpaRepo paperInstanceJpaRepo;
    private final ExamPaperJpaRepo paperJpaRepo;
    private final UserGroupJpaRepo userGroupJpaRepo;
    private final UserGroupMappingJpaRepo userGroupMappingJpaRepo;
    private final JPAQueryFactory jpaQueryFactory;

    public UserQueryService(UserJpaRepo userJpaRepo, UserTokenJpaRepo userTokenJpaRepo, BankJpaRepo bankJpaRepo, ExamPaperInstanceJpaRepo paperInstanceJpaRepo,
                            ExamPaperJpaRepo paperJpaRepo, UserGroupJpaRepo userGroupJpaRepo, UserGroupMappingJpaRepo userGroupMappingJpaRepo,
                            JPAQueryFactory jpaQueryFactory) {
        this.userJpaRepo = userJpaRepo;
        this.userTokenJpaRepo = userTokenJpaRepo;
        this.bankJpaRepo = bankJpaRepo;
        this.paperInstanceJpaRepo = paperInstanceJpaRepo;
        this.paperJpaRepo = paperJpaRepo;
        this.userGroupJpaRepo = userGroupJpaRepo;
        this.userGroupMappingJpaRepo = userGroupMappingJpaRepo;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public UserDto findUserDto(Long userId){
        Optional<UserEntity> userEntityOptional = userJpaRepo.findById(userId);
        if(!userEntityOptional.isPresent()){
            throw new BizException("用户不存在");
        }
        UserEntity userEntity = userEntityOptional.get();
        List<UserGroupMappingEntity> groupMappingEntityList = userGroupMappingJpaRepo.findByUserIdIn(Collections.singletonList(userId));
        List<UserGroupEntity> groupEntityList = userGroupJpaRepo.findAllById(groupMappingEntityList.stream()
                .map(UserGroupMappingEntity::getGroupId).collect(Collectors.toList()));

        List<Map<String,Object>> userPaperInstanceCountList = paperInstanceJpaRepo.findUserPaperInstanceCount(Collections.singletonList(userId));
        Long paperInstanceCount = 0L;
        if(!CollectionUtils.isEmpty(userPaperInstanceCountList)){
            paperInstanceCount = (Long) userPaperInstanceCountList.get(0).get("paperInstanceCount");
        }

        return new UserDto(userId,userEntity.getName(),userEntity.getIdNo(),userEntity.getPwd(),userEntity.getStatus(),
                groupEntityList.stream().map(UserGroupEntity::getTitle).collect(Collectors.toList()), paperInstanceCount);
    }

    public Page<UserDto> findUserPage(Integer pageNo, Integer pageSize, Long groupId, String name){
        QUserEntity qUserEntity = QUserEntity.userEntity;
        QUserGroupEntity qUserGroupEntity = QUserGroupEntity.userGroupEntity;
        QUserGroupMappingEntity qUserGroupMappingEntity = QUserGroupMappingEntity.userGroupMappingEntity;

        List<Predicate> whereList = new ArrayList<>();
        if(StringUtils.hasText(name)){
            whereList.add(qUserEntity.name.like("%" + name + "%"));
        }
        if(groupId != null){
            whereList.add(qUserGroupEntity.id.eq(groupId));
        }
        Predicate[] predicates = whereList.toArray(new Predicate[0]);
        JPAQuery<UserDto> userDtoJpaQuery = jpaQueryFactory.
                select(Projections.bean(UserDto.class,
                        qUserEntity.id,
                        qUserEntity.name,
                        qUserEntity.idNo,
                        qUserEntity.pwd,
                        qUserEntity.status)).
                from(qUserEntity).
                leftJoin(qUserGroupMappingEntity).on(qUserEntity.id.eq(qUserGroupMappingEntity.userId)).
                leftJoin(qUserGroupEntity).on(qUserGroupEntity.id.eq(qUserGroupMappingEntity.groupId)).
                where(predicates).
                groupBy(qUserEntity.id).
                orderBy(qUserEntity.createTime.desc()).
                offset((long) pageNo * pageSize).
                limit(pageSize);
        List<UserDto> content = userDtoJpaQuery.fetch();
        //封装用户分组
        List<Long> userIdList = content.stream().map(UserDto::getId).collect(Collectors.toList());
        List<UserGroupMappingEntity> userGroupMappingEntityList = userGroupMappingJpaRepo
                .findByUserIdIn(userIdList);

        Map<Long,List<Long>> userGroupMappingMap = new HashMap<>(userIdList.size());
        userGroupMappingEntityList.forEach(userGroupMappingEntity -> {
            Long userId = userGroupMappingEntity.getUserId();
            if(userGroupMappingMap.containsKey(userId)){
                userGroupMappingMap.get(userId).add(userGroupMappingEntity.getGroupId());
            }else {
                List<Long> groupIdList = new ArrayList<>();
                groupIdList.add(userGroupMappingEntity.getGroupId());
                userGroupMappingMap.put(userId,groupIdList);
            }
        });

        List<Long> groupIdList = userGroupMappingEntityList.stream()
                .map(UserGroupMappingEntity::getGroupId)
                .collect(Collectors.toList());
        Map<Long, UserGroupEntity> groupMap = userGroupJpaRepo.findAllById(groupIdList)
                .stream()
                .collect(Collectors.toMap(UserGroupEntity::getId,g -> g));

        content.forEach(userDto -> {
            List<Long> myGroupIdList = userGroupMappingMap.get(userDto.getId());
            if(!CollectionUtils.isEmpty(myGroupIdList)) {
                List<String> myGroupTitleList = new ArrayList<>();
                myGroupIdList.forEach(gId -> {
                    myGroupTitleList.add(groupMap.get(gId).getTitle());
                });
                userDto.setGroupTitle(myGroupTitleList);
            }
        });

        long total = userDtoJpaQuery.fetchCount();

        List<Map<String,Object>> userPaperInstanceCountList = paperInstanceJpaRepo
                .findUserPaperInstanceCount(content.stream().map(UserDto::getId).collect(Collectors.toList()));
        Map<Long,Long> userPaperInstanceCountMap = userPaperInstanceCountList.stream()
                .collect(Collectors.toMap(m -> (Long)m.get("userId") , n -> (Long)n.get("paperInstanceCount")));
        content.forEach(userDto -> userDto.setPaperInstanceCount(userPaperInstanceCountMap.getOrDefault(userDto.getId(),0L)));
        return PageUtil.pageData(content,pageNo,pageSize,total);
    }

    public List<UserGroupEntity> findGroups(){
        return userGroupJpaRepo.findAll();
    }
}
