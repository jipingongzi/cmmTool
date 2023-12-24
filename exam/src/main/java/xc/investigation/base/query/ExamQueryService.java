package xc.investigation.base.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.constant.domain.ExamPaperStatus;
import xc.investigation.base.constant.domain.ExamQuestionType;
import xc.investigation.base.dto.*;
import xc.investigation.base.repo.entity.bank.BankEntity;
import xc.investigation.base.repo.entity.bank.QBankEntity;
import xc.investigation.base.repo.entity.exam.*;
import xc.investigation.base.repo.entity.user.QUserEntity;
import xc.investigation.base.repo.entity.user.UserEntity;
import xc.investigation.base.repo.jpa.bank.BankJpaRepo;
import xc.investigation.base.repo.jpa.exam.*;
import xc.investigation.base.repo.jpa.user.UserGroupJpaRepo;
import xc.investigation.base.repo.jpa.user.UserJpaRepo;
import xc.investigation.base.utils.PageUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@Service
public class ExamQueryService {

    private final ExamBatchJpaRepo batchJpaRepo;
    private final ExamPaperJpaRepo paperJpaRepo;
    private final ExamQuestionJpaRepo questionJpaRepo;
    private final ExamQuestionOptionJpaRepo questionOptionJpaRepo;
    private final ExamPaperInstanceJpaRepo paperInstanceJpaRepo;
    private final ExamQuestionInstanceJpaRepo questionInstanceJpaRepo;
    private final ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo;
    private final ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo;
    private final ExamUserMappingJpaRepo userMappingJpaRepo;
    private final ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo;
    private final ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepo userJpaRepo;
    private final BankJpaRepo bankJpaRepo;
    private final ExamUserGroupMappingJpaRepo groupMappingJpaRepo;
    private final UserGroupJpaRepo groupJpaRepo;

    public ExamQueryService(ExamBatchJpaRepo batchJpaRepo, ExamPaperJpaRepo paperJpaRepo, ExamQuestionJpaRepo questionJpaRepo,
                            ExamQuestionOptionJpaRepo questionOptionJpaRepo, ExamPaperInstanceJpaRepo paperInstanceJpaRepo,
                            ExamQuestionInstanceJpaRepo questionInstanceJpaRepo, ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo,
                            ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo, ExamUserMappingJpaRepo userMappingJpaRepo,
                            ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo, ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo,
                            JPAQueryFactory jpaQueryFactory, UserJpaRepo userJpaRepo, BankJpaRepo bankJpaRepo,
                            ExamUserGroupMappingJpaRepo groupMappingJpaRepo, UserGroupJpaRepo groupJpaRepo) {
        this.batchJpaRepo = batchJpaRepo;
        this.paperJpaRepo = paperJpaRepo;
        this.questionJpaRepo = questionJpaRepo;
        this.questionOptionJpaRepo = questionOptionJpaRepo;
        this.paperInstanceJpaRepo = paperInstanceJpaRepo;
        this.questionInstanceJpaRepo = questionInstanceJpaRepo;
        this.questionInstanceFileJpaRepo = questionInstanceFileJpaRepo;
        this.paperInstanceFileJpaRepo = paperInstanceFileJpaRepo;
        this.userMappingJpaRepo = userMappingJpaRepo;
        this.paperQuestionTreeJpaRepo = paperQuestionTreeJpaRepo;
        this.paperQuestionOptionTreeJpaRepo = paperQuestionOptionTreeJpaRepo;
        this.jpaQueryFactory = jpaQueryFactory;
        this.userJpaRepo = userJpaRepo;
        this.bankJpaRepo = bankJpaRepo;
        this.groupMappingJpaRepo = groupMappingJpaRepo;
        this.groupJpaRepo = groupJpaRepo;
    }

    public ExamPaperDto findPaper(Long paperId) {
        Optional<ExamPaperEntity> paperEntityOptional = paperJpaRepo.findById(paperId);
        if (!paperEntityOptional.isPresent()) {
            throw new BizException("问卷不存在");
        }
        ExamPaperEntity paperEntity = paperEntityOptional.get();
        Optional<ExamBatchEntity> batchEntityOptional = batchJpaRepo.findById(paperEntity.getBatchId());
        Long batchId = null;
        String batchTitle = null;
        if (batchEntityOptional.isPresent()) {
            batchId = batchEntityOptional.get().getId();
            batchTitle = batchEntityOptional.get().getTitle();
        }

        List<ExamPaperQuestionTreeEntity> questionTreeEntityList = paperQuestionTreeJpaRepo.findByPaperId(paperId);
        List<Long> questionTreeIdList = questionTreeEntityList.stream().map(ExamPaperQuestionTreeEntity::getId)
                .collect(Collectors.toList());
        List<Long> questionIdList = questionTreeEntityList.stream().map(ExamPaperQuestionTreeEntity::getQuestionId)
                .collect(Collectors.toList());
        Map<Long, ExamQuestionEntity> questionMap = questionJpaRepo.findAllById(questionIdList).stream()
                .collect(Collectors.toMap(ExamQuestionEntity::getId, q -> q));

        List<ExamPaperQuestionOptionTreeEntity> optionTreeEntityList = paperQuestionOptionTreeJpaRepo.findByQuestionTreeIdIn(questionTreeIdList);
        List<Long> optionIdList = optionTreeEntityList.stream().map(ExamPaperQuestionOptionTreeEntity::getOptionId)
                .collect(Collectors.toList());
        Map<Long, List<ExamPaperQuestionOptionTreeEntity>> questionOptionMap = optionTreeEntityList.stream()
                .collect(Collectors.groupingBy(ExamPaperQuestionOptionTreeEntity::getQuestionTreeId));
        Map<Long, ExamQuestionOptionEntity> optionMap = questionOptionJpaRepo.findAllById(optionIdList).stream()
                .collect(Collectors.toMap(ExamQuestionOptionEntity::getId, o -> o));

        ExamPaperDto paperDto = new ExamPaperDto(paperEntity.getId(), paperEntity.getTitle(), paperEntity.getDescription(),
                paperEntity.getPoint(), paperEntity.getQuestionNumber(), paperEntity.getStatus(), batchId, batchTitle);
        questionTreeEntityList.forEach(qt -> {
            List<ExamPaperQuestionOptionTreeEntity> optionList = questionOptionMap.get(qt.getId());
            List<ExamQuestionOptionDto> optionDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(optionList)) {
                optionList.forEach(ot -> {
                    ExamQuestionOptionEntity optionEntity = optionMap.get(ot.getOptionId());
                    optionDtoList.add(new ExamQuestionOptionDto(ot.getOptionId(), ot.getId(), optionEntity.getTitle(),
                            optionEntity.getCorrectFlag(), ot.getEndFlag(), optionEntity.getPoint(), ot.getNextQuestionTreeId()));
                });
            }
            ExamQuestionEntity questionEntity = questionMap.get(qt.getQuestionId());
            paperDto.getQuestionDtoList().add(new ExamQuestionDto(qt.getQuestionId(), qt.getId(), questionEntity.getTitle(), questionEntity.getType(),
                    qt.getPoint(), qt.getRootFlag(), qt.getEndFlag(), qt.getNextPaperQuestionTreeId(), optionDtoList));
        });
        return paperDto;
    }

    public ExamPaperInstanceDto findPaperInstance(Long paperInstanceId) {
        Optional<ExamPaperInstanceEntity> paperInstanceEntityOptional = paperInstanceJpaRepo.findById(paperInstanceId);
        if (!paperInstanceEntityOptional.isPresent()) {
            throw new BizException("问卷实例不存在");
        }
        ExamPaperInstanceEntity paperInstanceEntity = paperInstanceEntityOptional.get();
        List<ExamQuestionInstanceEntity> questionInstanceEntityList = questionInstanceJpaRepo.findByPaperInstanceId(paperInstanceId);

        ExamPaperInstanceDto paperInstanceDto = new ExamPaperInstanceDto(paperInstanceId, paperInstanceEntity.getPaperId(), paperInstanceEntity.getUserId(),
                paperInstanceEntity.getBankCode(), paperInstanceEntity.getStartTime(), paperInstanceEntity.getEndTime(), paperInstanceEntity.getStatus(),
                paperInstanceEntity.getPoint(), paperInstanceEntity.getQuestionNumber());

        List<ExamPaperInstanceFileEntity> paperFiles = paperInstanceFileJpaRepo.findByPaperInstanceId(paperInstanceId);
        if (!CollectionUtils.isEmpty(paperFiles)) {
            paperInstanceDto.setFileList(paperFiles.stream().map(f -> new ExamInstanceFileDto(f.getUrl(),
                    f.getDescription(), f.getType())).collect(Collectors.toList()));
        } else {
            paperInstanceDto.setFileList(Collections.emptyList());
        }
        List<ExamQuestionInstanceFileEntity> questionFiles = questionInstanceFileJpaRepo.findByQuestionInstanceIdIn(
                questionInstanceEntityList.stream().map(ExamQuestionInstanceEntity::getId).collect(Collectors.toList()));

        questionInstanceEntityList.forEach(qi -> {
            List<ExamQuestionInstanceFileEntity> thisQuestionFiles = questionFiles.stream()
                    .filter(qf -> qf.getQuestionInstanceId().equals(qi.getId()))
                    .collect(Collectors.toList());
            List<ExamInstanceFileDto> fileDtos = new ArrayList<>();
            if (!CollectionUtils.isEmpty(thisQuestionFiles)) {
                fileDtos = thisQuestionFiles.stream().map(f -> new ExamInstanceFileDto(f.getUrl(),
                        f.getDescription(), f.getType())).collect(Collectors.toList());
            }
            paperInstanceDto.getAnswerList().add(new ExamQuestionInstanceDto(qi.getPaperQuestionTreeId(),
                    qi.getAnswer(), fileDtos));
        });

        return paperInstanceDto;
    }

    public Page<ExamPaperListAdminDto> findPaperPageForAdmin(Integer pageNo, Integer pageSize, String title, Long batchId) {
        QExamBatchEntity qExamBatchEntity = QExamBatchEntity.examBatchEntity;
        QExamPaperEntity qExamPaperEntity = QExamPaperEntity.examPaperEntity;
        List<Predicate> whereList = new ArrayList<>();
        if (StringUtils.hasText(title)) {
            whereList.add(qExamPaperEntity.title.like("%" + title + "%"));
        }
        if (batchId != null) {
            whereList.add(qExamPaperEntity.batchId.eq(batchId));
        }
        Predicate[] predicates = whereList.toArray(new Predicate[0]);
        JPAQuery<ExamPaperListAdminDto> examPaperListDtoJpaQuery = jpaQueryFactory.
                select(Projections.bean(ExamPaperListAdminDto.class,
                        qExamPaperEntity.id,
                        qExamBatchEntity.id.as("batchId"),
                        qExamPaperEntity.title,
                        qExamBatchEntity.title.as("batchTitle"),
                        qExamPaperEntity.description,
                        qExamPaperEntity.point,
                        qExamPaperEntity.questionNumber,
                        qExamPaperEntity.status)).
                from(qExamPaperEntity).
                leftJoin(qExamBatchEntity).on(qExamPaperEntity.batchId.eq(qExamBatchEntity.id)).
                where(predicates).
                orderBy(qExamPaperEntity.createTime.desc()).
                offset((long) pageNo * pageSize).
                limit(pageSize);
        List<ExamPaperListAdminDto> content = examPaperListDtoJpaQuery.fetch();
        long total = examPaperListDtoJpaQuery.fetchCount();
        return PageUtil.pageData(content, pageNo, pageSize, total);
    }

    public Page<ExamPaperInstanceListDto> findPaperInstancePage(Integer pageNo, Integer pageSize,
                                                                Long batchId, Long userId, String paperTitle,
                                                                String userName, String bankCode,
                                                                Integer minPoint, Integer maxPoint, ExamPaperInstanceStatus status,
                                                                LocalDateTime minStartTime, LocalDateTime maxStartTime) {
        QExamPaperEntity qExamPaperEntity = QExamPaperEntity.examPaperEntity;
        QExamPaperInstanceEntity qExamPaperInstanceEntity = QExamPaperInstanceEntity.examPaperInstanceEntity;
        QUserEntity qUserEntity = QUserEntity.userEntity;
        QBankEntity qBankEntity = QBankEntity.bankEntity;

        List<Predicate> whereList = new ArrayList<>();
        if (batchId != null) {
            whereList.add(qExamPaperEntity.batchId.eq(batchId));
        }
        if (userId != null) {
            whereList.add(qExamPaperInstanceEntity.userId.eq(userId));
        }
        if (StringUtils.hasText(paperTitle)) {
            whereList.add(qExamPaperEntity.title.like("%" + paperTitle.trim() + "%"));
        }
        if (StringUtils.hasText(userName)) {
            whereList.add(qUserEntity.name.like("%" + userName.trim() + "%"));
        }
        if (StringUtils.hasText(bankCode)) {
            whereList.add(qExamPaperInstanceEntity.bankCode.like(bankCode + "%"));
        }
        if (minPoint != null) {
            whereList.add(qExamPaperInstanceEntity.point.goe(minPoint));
        }
        if (maxPoint != null) {
            whereList.add(qExamPaperInstanceEntity.point.loe(maxPoint));
        }
        if (status != null) {
            whereList.add(qExamPaperInstanceEntity.status.eq(status));
        }
        if (minStartTime != null) {
            whereList.add(qExamPaperInstanceEntity.startTime.after(minStartTime));
        }
        if (maxStartTime != null) {
            whereList.add(qExamPaperInstanceEntity.startTime.before(maxStartTime));
        }
        Predicate[] predicates = whereList.toArray(new Predicate[0]);
        JPAQuery<ExamPaperInstanceListDto> paperInstanceListDtoJpaQuery = jpaQueryFactory
                .select(
                        Projections.bean(ExamPaperInstanceListDto.class,
                                qExamPaperInstanceEntity.id,
                                qExamPaperEntity.id.as("paperId"),
                                qExamPaperEntity.title.as("paperTitle"),
                                qUserEntity.name.as("userName"),
                                qBankEntity.name.as("bankName"),
                                qExamPaperInstanceEntity.startTime,
                                qExamPaperInstanceEntity.endTime,
                                qExamPaperInstanceEntity.status,
                                qExamPaperInstanceEntity.point,
                                qExamPaperInstanceEntity.questionNumber
                        )
                )
                .from(qExamPaperInstanceEntity)
                .leftJoin(qExamPaperEntity).on(qExamPaperInstanceEntity.paperId.eq(qExamPaperEntity.id))
                .leftJoin(qBankEntity).on(qExamPaperInstanceEntity.bankCode.eq(qBankEntity.code))
                .leftJoin(qUserEntity).on(qExamPaperInstanceEntity.createId.eq(qUserEntity.id))
                .where(predicates)
                .orderBy(qExamPaperInstanceEntity.createTime.desc())
                .offset((long) pageNo * pageSize)
                .limit(pageSize);
        List<ExamPaperInstanceListDto> content = paperInstanceListDtoJpaQuery.fetch();
        long total = paperInstanceListDtoJpaQuery.fetchCount();
        return PageUtil.pageData(content, pageNo, pageSize, total);
    }

    public List<ExamMappingGroupDto> getGroupMapping(Long paperId) {
        List<ExamUserGroupMappingEntity> groupMappingEntityList = groupMappingJpaRepo.findByPaperId(paperId);
        List<Long> groupIdList = groupMappingEntityList.stream()
                .map(ExamUserGroupMappingEntity::getUserGroupId)
                .collect(Collectors.toList());
        return groupJpaRepo.findAllById(groupIdList).stream()
                .map(g -> new ExamMappingGroupDto(paperId, g.getId(), g.getTitle()))
                .collect(Collectors.toList());
    }

    public List<ExamMappingSpecialUserDto> getSpecialUserMapping(Long paperId) {
        List<ExamUserMappingEntity> userMappingEntityList = userMappingJpaRepo.findByPaperIdAndSpecial(paperId, true);
        List<Long> userIdList = userMappingEntityList.stream()
                .map(ExamUserMappingEntity::getUserId)
                .collect(Collectors.toList());
        return userJpaRepo.findAllById(userIdList).stream()
                .map(u -> new ExamMappingSpecialUserDto(paperId, u.getId(), u.getName()))
                .collect(Collectors.toList());
    }

    public List<ExamMappingCandidateDto> getCandidateForMapping(Long paperId, String userName) {
        List<Long> oldMappingUserIdList = userMappingJpaRepo.findByPaperId(paperId).stream()
                .map(ExamUserMappingEntity::getUserId)
                .collect(Collectors.toList());
        if (StringUtils.hasText(userName)) {
            userName = "%" + userName + "%";
        } else {
            userName = "%%";
        }
        List<UserEntity> userEntityList = userJpaRepo.findByNameLike(userName);
        return userEntityList.stream()
                .filter(u -> !oldMappingUserIdList.contains(u.getId()))
                .map(u -> new ExamMappingCandidateDto(u.getName(), u.getIdNo(), u.getId()))
                .collect(Collectors.toList());
    }

    public List<ExamMappingPaperInstanceDto> getPaperInstanceMapping(Long paperId) {
        QExamPaperEntity qExamPaperEntity = QExamPaperEntity.examPaperEntity;
        QExamUserMappingEntity qExamUserMappingEntity = QExamUserMappingEntity.examUserMappingEntity;
        QUserEntity qUserEntity = QUserEntity.userEntity;
        QExamPaperInstanceEntity qExamPaperInstanceEntity = QExamPaperInstanceEntity.examPaperInstanceEntity;
        QBankEntity qBankEntity = QBankEntity.bankEntity;

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(qExamPaperEntity.id.eq(paperId));
        whereList.add(qExamPaperInstanceEntity.status.eq(ExamPaperInstanceStatus.AUDIT_SUCCESS));
        JPAQuery<ExamMappingPaperInstanceDto> userMappingQuery = jpaQueryFactory
                .select(
                        Projections.bean(ExamMappingPaperInstanceDto.class,
                                qExamPaperEntity.id.as("paperId"),
                                qExamPaperInstanceEntity.id.as("paperInstanceId"),
                                qUserEntity.id.as("userId"),
                                qExamPaperEntity.title.as("paperTitle"),
                                qUserEntity.name.as("userName"),
                                qUserEntity.idNo.as("userIdNo"),
                                qExamPaperInstanceEntity.status,
                                qExamPaperInstanceEntity.startTime,
                                qExamPaperInstanceEntity.endTime,
                                qBankEntity.name.as("bankName")
                        )
                )
                .from(qExamUserMappingEntity)
                .innerJoin(qExamPaperInstanceEntity)
                .on(qExamPaperInstanceEntity.paperId.eq(qExamUserMappingEntity.paperId).and(qExamPaperInstanceEntity.userId.eq(qExamUserMappingEntity.userId)))
                .leftJoin(qUserEntity).on(qUserEntity.id.eq(qExamUserMappingEntity.userId))
                .leftJoin(qExamPaperEntity).on(qExamPaperEntity.id.eq(qExamUserMappingEntity.paperId))
                .leftJoin(qBankEntity).on(qBankEntity.code.eq(qExamPaperInstanceEntity.bankCode))
                .where(whereList.toArray(new Predicate[0]))
                .orderBy(qExamUserMappingEntity.createTime.desc());
        return userMappingQuery.fetch();
    }

    public Page<ExamBatchEntity> findBatchList(String title, Integer pageNo, Integer pageSize) {
        if (!StringUtils.hasText(title)) {
            title = "%%";
        } else {
            title = "%" + title + "%";
        }
        return batchJpaRepo.findByTitleLike(title, PageRequest.of(pageNo, pageSize));
    }

    public List<ExamPaperListUserDto> findPaperPageForUser(Long batchId, Long userId) {
        List<ExamPaperEntity> paperEntityList = paperJpaRepo.findByBatchIdAndStatusIn(batchId, Collections.singletonList(ExamPaperStatus.ENABLE));
        List<ExamUserMappingEntity> userMappingEntityList = userMappingJpaRepo.findByUserId(userId);
        List<Long> userPaperIdList = userMappingEntityList.stream().map(ExamUserMappingEntity::getPaperId).collect(Collectors.toList());
        List<ExamPaperEntity> userPaperEntityList = paperEntityList.stream().filter(p -> userPaperIdList.contains(p.getId())).collect(Collectors.toList());

        List<ExamPaperListUserDto> paperListUserDtoList = new ArrayList<>();
        userPaperEntityList.forEach(p -> {
            List<String> hasInstanceStrList = new ArrayList<>();
            List<ExamPaperInstanceEntity> paperInstanceEntityList = paperInstanceJpaRepo.findByPaperIdAndUserId(p.getId(), userId);
            if (!CollectionUtils.isEmpty(paperInstanceEntityList)) {
                Map<ExamPaperInstanceStatus, Integer> instanceStatusNumberMap = new HashMap<>(paperInstanceEntityList.size());
                paperInstanceEntityList.forEach(pi -> {
                    ExamPaperInstanceStatus status = pi.getStatus();
                    if (!instanceStatusNumberMap.containsKey(status)) {
                        instanceStatusNumberMap.put(status, 1);
                    } else {
                        instanceStatusNumberMap.put(status, instanceStatusNumberMap.get(status) + 1);
                    }
                });
                instanceStatusNumberMap.forEach((key, number) -> {
                    StringBuilder hasInstanceStr = new StringBuilder();
                    String statusText = convertExamPaperInstanceStatusToStr(key);
                    hasInstanceStr.append(statusText).append(" : ").append(number).append("份");
                    hasInstanceStrList.add(hasInstanceStr.toString());
                });
            }
            paperListUserDtoList.add(new ExamPaperListUserDto(p.getId(), p.getTitle(), hasInstanceStrList));
        });
        return paperListUserDtoList;
    }

    public List<ExamPaperInstanceListDto> findPaperInstanceListDtoByUserId(Long userId) {
        return buildPaperInstanceListDto(paperInstanceJpaRepo.findByUserIdOrderByCreateTimeDesc(userId));
    }

    public List<ExamPaperInstanceListDto> findPaperInstanceListDtoByBankId(Long bankId) {
        Optional<BankEntity> bankEntityOptional = bankJpaRepo.findById(bankId);
        return bankEntityOptional.map(bankEntity -> buildPaperInstanceListDto(
                paperInstanceJpaRepo.findByBankCodeOrderByCreateTimeDesc(bankEntity.getCode()))).orElseGet(ArrayList::new);
    }

    public ExamQuestionEntity findQuestionByQuestionTreeId(Long questionTreeId) {
        Optional<ExamPaperQuestionTreeEntity> questionTreeEntityOptional = paperQuestionTreeJpaRepo.findById(questionTreeId);
        if (!questionTreeEntityOptional.isPresent()) {
            throw new BizException("问题在问卷中不存在");
        }
        Optional<ExamQuestionEntity> questionEntityOptional =
                questionJpaRepo.findById(questionTreeEntityOptional.get().getQuestionId());
        if (!questionEntityOptional.isPresent()) {
            throw new BizException("问题不存在");
        }
        return questionEntityOptional.get();
    }

    public Map<Long, List<ExamPaperAnswerDto>> getAnswer(Long paperId, Collection<Long> paperInstanceIds) {

        List<ExamPaperQuestionTreeEntity> questionTreeEntityList = paperQuestionTreeJpaRepo.findByPaperId(paperId);
        List<Long> questionTreeIdList = questionTreeEntityList.stream().map(ExamPaperQuestionTreeEntity::getId).collect(Collectors.toList());
        List<ExamPaperQuestionOptionTreeEntity> optionTreeEntityList = paperQuestionOptionTreeJpaRepo.findByQuestionTreeIdIn(questionTreeIdList);

        List<Long> questionIdList = questionTreeEntityList.stream().map(ExamPaperQuestionTreeEntity::getQuestionId).collect(Collectors.toList());

        List<ExamQuestionEntity> questionEntityList = questionJpaRepo.findAllById(questionIdList);
        List<ExamQuestionOptionEntity> optionEntityList = questionOptionJpaRepo.findByQuestionIdIn(questionIdList);

        Map<Long, ExamQuestionEntity> questionTreeMap = new HashMap<>();
        questionTreeEntityList.forEach(qt -> {
            Optional<ExamQuestionEntity> questionEntityOptional = questionEntityList.stream().filter(q -> qt.getQuestionId().equals(q.getId())).findFirst();
            if (!questionEntityOptional.isPresent()) {
                return;
            }
            questionTreeMap.put(qt.getId(), questionEntityOptional.get());
        });
        Map<Long, ExamQuestionOptionEntity> optionTreeMap = new HashMap<>();
        optionTreeEntityList.forEach(ot -> {
            Optional<ExamQuestionOptionEntity> optionEntityOptional = optionEntityList.stream().filter(o -> ot.getOptionId().equals(o.getId())).findFirst();
            if (!optionEntityOptional.isPresent()) {
                return;
            }
            optionTreeMap.put(ot.getId(), optionEntityOptional.get());
        });

        Map<Long, List<ExamPaperAnswerDto>> result = new LinkedHashMap<>();
        List<ExamQuestionInstanceEntity> questionInstanceEntityList = questionInstanceJpaRepo.findByPaperInstanceIdIn(paperInstanceIds);
        questionInstanceEntityList.forEach(qi -> {

            ExamQuestionEntity questionEntity = questionTreeMap.get(qi.getPaperQuestionTreeId());
            String answerText = "";
            if (ExamQuestionType.INPUT.equals(questionEntity.getType())) {
                answerText = qi.getAnswer();
            } else if (ExamQuestionType.MULTIPLE_CHOICE.equals(questionEntity.getType())) {
                String[] optionTreeIds = qi.getAnswer().split(",");
                for (String optionTreeId : optionTreeIds) {
                    if (StringUtils.hasText(answerText)) {
                        answerText += "," + optionTreeMap.get(Long.parseLong(optionTreeId)).getTitle();
                    } else {
                        answerText = optionTreeMap.get(Long.parseLong(optionTreeId)).getTitle();
                    }
                }
            } else {
                answerText = optionTreeMap.get(Long.parseLong(qi.getAnswer())).getTitle();
            }

            if (result.containsKey(qi.getPaperInstanceId())) {
                List<ExamPaperAnswerDto> answers = result.get(qi.getPaperInstanceId());
                answers.add(new ExamPaperAnswerDto(qi.getId(), questionEntity.getTitle(), qi.getAnswer(), answerText));
            } else {
                List<ExamPaperAnswerDto> answers = new ArrayList<>();
                answers.add(new ExamPaperAnswerDto(qi.getId(), questionEntity.getTitle(), qi.getAnswer(), answerText));
                result.put(qi.getPaperInstanceId(), answers);
            }
        });
        return result;
    }

    private List<ExamPaperInstanceListDto> buildPaperInstanceListDto(List<ExamPaperInstanceEntity> paperInstanceEntityList) {

        Map<String, BankEntity> bankMap = bankJpaRepo.findByCodeIn(paperInstanceEntityList.stream()
                        .map(ExamPaperInstanceEntity::getBankCode).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(BankEntity::getCode, b -> b));

        Map<Long, ExamPaperEntity> paperMap = paperJpaRepo.findAllById(paperInstanceEntityList.stream()
                        .map(ExamPaperInstanceEntity::getPaperId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(ExamPaperEntity::getId, p -> p));

        Map<Long, UserEntity> uerMap = userJpaRepo.findAllById(paperInstanceEntityList.stream()
                        .map(ExamPaperInstanceEntity::getUserId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

        List<ExamPaperInstanceListDto> paperInstanceListDtoList = new ArrayList<>();

        paperInstanceEntityList.forEach(pi -> paperInstanceListDtoList.add(new ExamPaperInstanceListDto(pi.getId(), pi.getPaperId(),
                paperMap.get(pi.getPaperId()).getTitle(),
                uerMap.get(pi.getUserId()).getName(),
                bankMap.get(pi.getBankCode()).getName(),
                pi.getStartTime(), pi.getEndTime(), pi.getStatus(), pi.getPoint(), pi.getQuestionNumber())));

        return paperInstanceListDtoList;
    }

    private String convertExamPaperInstanceStatusToStr(ExamPaperInstanceStatus status) {
        String text = "";
        switch (status) {
            case IN_PROGRESS:
                text = "进行中";
                break;
            case COMPLETE:
                text = "已完成";
                break;
            case SUBMIT:
                text = "已提交";
                break;
            case AUDIT_FAIL:
                text = "审核失败";
                break;
            case AUDIT_SUCCESS:
                text = "审核成功";
                break;
            default:
        }
        return text;
    }

}