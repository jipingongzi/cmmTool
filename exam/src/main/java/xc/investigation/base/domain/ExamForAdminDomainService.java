package xc.investigation.base.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xc.investigation.base.api.vo.exam.PaperCreateVo;
import xc.investigation.base.api.vo.exam.QuestionAddVo;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.constant.domain.ExamPaperStatus;
import xc.investigation.base.constant.domain.ExamQuestionType;
import xc.investigation.base.dto.UserDto;
import xc.investigation.base.query.UserQueryService;
import xc.investigation.base.repo.entity.exam.ExamPaperEntity;
import xc.investigation.base.repo.entity.exam.ExamPaperInstanceEntity;
import xc.investigation.base.repo.entity.exam.ExamPaperQuestionOptionTreeEntity;
import xc.investigation.base.repo.entity.exam.ExamPaperQuestionTreeEntity;
import xc.investigation.base.repo.entity.exam.ExamQuestionEntity;
import xc.investigation.base.repo.entity.exam.ExamQuestionInstanceEntity;
import xc.investigation.base.repo.entity.exam.ExamQuestionOptionEntity;
import xc.investigation.base.repo.entity.exam.ExamUserGroupMappingEntity;
import xc.investigation.base.repo.entity.exam.ExamUserMappingEntity;
import xc.investigation.base.repo.jpa.exam.ExamPaperInstanceFileJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperInstanceJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperQuestionOptionTreeJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperQuestionTreeJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionInstanceFileJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionInstanceJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionOptionJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamUserGroupMappingJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamUserMappingJpaRepo;
import xc.investigation.base.utils.IdUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExamForAdminDomainService {

    private final ExamForCommonDomainService examForCommonDomainService;
    private final ExamPaperJpaRepo paperJpaRepo;
    private final ExamPaperInstanceJpaRepo paperInstanceJpaRepo;
    private final ExamQuestionJpaRepo questionJpaRepo;
    private final ExamQuestionInstanceJpaRepo questionInstanceJpaRepo;
    private final ExamQuestionOptionJpaRepo questionOptionJpaRepo;
    private final ExamUserMappingJpaRepo userMappingJpaRepo;
    private final ExamUserGroupMappingJpaRepo groupMappingJpaRepo;
    private final ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo;
    private final ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo;
    private final ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo;
    private final ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo;
    private final UserQueryService userQueryService;

    public ExamForAdminDomainService(ExamForCommonDomainService examForCommonDomainService,
                                     ExamPaperJpaRepo paperJpaRepo, ExamPaperInstanceJpaRepo paperInstanceJpaRepo,
                                     ExamQuestionJpaRepo questionJpaRepo, ExamQuestionInstanceJpaRepo questionInstanceJpaRepo,
                                     ExamQuestionOptionJpaRepo questionOptionJpaRepo, ExamUserMappingJpaRepo userMappingJpaRepo,
                                     ExamUserGroupMappingJpaRepo groupMappingJpaRepo, ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo,
                                     ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo,
                                     ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo, ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo,
                                     UserQueryService userQueryService) {
        this.examForCommonDomainService = examForCommonDomainService;
        this.paperJpaRepo = paperJpaRepo;
        this.paperInstanceJpaRepo = paperInstanceJpaRepo;
        this.questionJpaRepo = questionJpaRepo;
        this.questionInstanceJpaRepo = questionInstanceJpaRepo;
        this.questionOptionJpaRepo = questionOptionJpaRepo;
        this.userMappingJpaRepo = userMappingJpaRepo;
        this.groupMappingJpaRepo = groupMappingJpaRepo;
        this.paperQuestionTreeJpaRepo = paperQuestionTreeJpaRepo;
        this.paperQuestionOptionTreeJpaRepo = paperQuestionOptionTreeJpaRepo;
        this.paperInstanceFileJpaRepo = paperInstanceFileJpaRepo;
        this.questionInstanceFileJpaRepo = questionInstanceFileJpaRepo;
        this.userQueryService = userQueryService;
    }

    public Long savePaper(Long adminId, PaperCreateVo paperCreateVo){
        Long paperId;
        if(paperCreateVo.getId() == null){
            paperId = createPaper(adminId,paperCreateVo.getBatchId(),paperCreateVo.getTitle(),paperCreateVo.getDescription());
        }else {
            paperId = paperCreateVo.getId();
            List<ExamPaperInstanceEntity> paperInstanceEntityList = paperInstanceJpaRepo.findByPaperId(paperId);
            if(!CollectionUtils.isEmpty(paperInstanceEntityList)){
                throw new BizException("已经生产问卷实例，无法修改");
            }
            deletePaperQuestion(paperId);
            editPaper(adminId,paperId,paperCreateVo.getBatchId(),paperCreateVo.getTitle(),paperCreateVo.getDescription());
        }
        saveQuestion(adminId,paperId,paperCreateVo.getQuestionAddVoList(),true,null,null);
        flushPaper(paperId);
        return paperId;
    }

    private void deletePaperQuestion(Long paperId){
        List<ExamPaperQuestionTreeEntity> oldQuestionTreeList = paperQuestionTreeJpaRepo.findByPaperId(paperId);
        List<Long> oldQuestionIdList = oldQuestionTreeList.stream().map(ExamPaperQuestionTreeEntity::getQuestionId).collect(Collectors.toList());
        List<ExamQuestionEntity> oldQuestionList = questionJpaRepo.findAllById(oldQuestionIdList);
        questionJpaRepo.deleteAll(oldQuestionList);
        paperQuestionTreeJpaRepo.deleteAll(oldQuestionTreeList);

        List<Long> oldQuestionTreeIdList = oldQuestionTreeList.stream().map(ExamPaperQuestionTreeEntity::getId).collect(Collectors.toList());
        List<ExamPaperQuestionOptionTreeEntity> oldOptionTreeEntityList = paperQuestionOptionTreeJpaRepo.findByQuestionTreeIdIn(oldQuestionTreeIdList);
        List<Long> oldOptionIdList = oldOptionTreeEntityList.stream().map(ExamPaperQuestionOptionTreeEntity::getOptionId).collect(Collectors.toList());
        List<ExamQuestionOptionEntity> oldOptionEntityList = questionOptionJpaRepo.findAllById(oldOptionIdList);
        questionOptionJpaRepo.deleteAll(oldOptionEntityList);
        paperQuestionOptionTreeJpaRepo.deleteAll(oldOptionTreeEntityList);
    }

    private void saveQuestion(Long adminId,Long paperId,List<QuestionAddVo> questionAddVoList, Boolean markRoot,
                              ExamPaperQuestionTreeEntity parentQuestionTree,ExamPaperQuestionOptionTreeEntity parentOptionTree){
        List<ExamQuestionEntity> newQuestionList = new ArrayList<>();
        List<ExamPaperQuestionTreeEntity> newQuestionTreeList = new ArrayList<>();
        List<ExamQuestionOptionEntity> newOptionList = new ArrayList<>();
        List<ExamPaperQuestionOptionTreeEntity> newOptionTreeList = new ArrayList<>();

        for (int i = 0; i < questionAddVoList.size(); i++) {
            QuestionAddVo questionAddVo = questionAddVoList.get(i);
            ExamQuestionEntity questionEntity = new ExamQuestionEntity(IdUtil.generateId(),adminId,paperId,
                    questionAddVo.getTitle(),questionAddVo.getType(),questionAddVo.getPoint());
            newQuestionList.add(questionEntity);

            ExamPaperQuestionTreeEntity questionTreeEntity = new ExamPaperQuestionTreeEntity(IdUtil.generateId(),adminId,paperId,
                    questionEntity.getId(), markRoot && newQuestionTreeList.size() == 0,questionAddVo.getPoint());
            newQuestionTreeList.add(questionTreeEntity);

            questionAddVo.getOptionAddVoList().forEach(optionAddVo -> {
                ExamQuestionOptionEntity optionEntity = new ExamQuestionOptionEntity(IdUtil.generateId(),adminId,
                        questionEntity.getId(),optionAddVo.getTitle(),optionAddVo.getEndFlag(),optionAddVo.getCorrectFlag());
                if(ExamQuestionType.UN_SETTLED_CHOICE.equals(questionEntity.getType())){
                    optionEntity.setPointForUnSettledChoice(adminId,optionAddVo.getPoint());
                }
                newOptionList.add(optionEntity);

                ExamPaperQuestionOptionTreeEntity optionTreeEntity = new ExamPaperQuestionOptionTreeEntity(IdUtil.generateId(),adminId,
                        questionTreeEntity.getId(),optionEntity.getId(),optionAddVo.getEndFlag());
                newOptionTreeList.add(optionTreeEntity);
                //处理分支路径
                if(!CollectionUtils.isEmpty(optionAddVo.getQuestionAddVoList())){
                    saveQuestion(adminId,paperId,optionAddVo.getQuestionAddVoList(),false,questionTreeEntity,optionTreeEntity);
                }
            });
            //补充nextQuestion
            if(parentQuestionTree != null && i == 0){
                parentQuestionTree.setNext(questionTreeEntity.getId());
            }
            if(parentOptionTree != null && i == 0){
                parentOptionTree.setNextQuestionTreeId(questionTreeEntity.getId());
            }
            if(i > 0){
                newQuestionTreeList.get(i -1).setNext(questionTreeEntity.getId());
            }
        }
        //将最后一道题设置为end
        if(!markRoot) {
            newQuestionTreeList.get(newQuestionTreeList.size() - 1).markEnd(adminId);
        }
        questionJpaRepo.saveAll(newQuestionList);
        paperQuestionTreeJpaRepo.saveAll(newQuestionTreeList);
        questionOptionJpaRepo.saveAll(newOptionList);
        paperQuestionOptionTreeJpaRepo.saveAll(newOptionTreeList);
    }


    public Long createPaper(Long adminId, Long batchId, String title, String description){
        Optional<ExamPaperEntity> oldPaperEntityOptional = paperJpaRepo.findByBatchIdAndTitle(batchId,title);
        if(oldPaperEntityOptional.isPresent()){
            throw new BizException(String.format("该批次已经下存在问卷《%s》",title));
        }
        ExamPaperEntity paperEntity = new ExamPaperEntity(IdUtil.generateId(),adminId,batchId,title,description);
        paperJpaRepo.save(paperEntity);
        return paperEntity.getId();
    }


    public void editPaper(Long adminId,Long paperId,Long batchId,String title,String description){
        ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
        paperEntity.update(adminId,batchId,title,description);
        paperJpaRepo.save(paperEntity);
    }

    public void deletePaper(Long adminId,Long paperId){
        ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
        List<ExamPaperInstanceEntity> paperInstanceEntityList = paperInstanceJpaRepo.findByPaperId(paperId);
        if(!CollectionUtils.isEmpty(paperInstanceEntityList)){
            throw new BizException("已经生产问卷实例，无法删除");
        }
        paperJpaRepo.delete(paperEntity);
        deletePaperQuestion(paperId);
    }

    /**
     * 拖拽原有题目进行新问卷
     * @param adminId 管理员id
     * @param paperId 问卷id
     * @param previousQuestionTreeId 前一题映射id
     * @param previousOptionTreeId 前一题选项映射id（可选）
     * @param questionId 原有题目id
     */
    public Long addQuestionForPaper(Long adminId, Long paperId, Long previousQuestionTreeId, Long previousOptionTreeId,
     Long questionId){
        ExamQuestionEntity questionEntity = examForCommonDomainService.findQuestion(questionId);
        Optional<ExamPaperQuestionTreeEntity> oldQuestionTreeEntityOptional = paperQuestionTreeJpaRepo.findByPaperIdAndQuestionId(paperId,questionId);
        if(oldQuestionTreeEntityOptional.isPresent()){
            throw new BizException(String.format("该问卷已经下存在题目《%s》",questionEntity.getTitle()));
        }
        return questionTreeProcess(adminId,paperId,previousQuestionTreeId,previousOptionTreeId,questionEntity.getId(),
        questionEntity.getPoint());
    }

    /**
     * 手动新增题目
     * @param adminId 管理员id
     * @param paperId 问卷id
     * @param previousQuestionTreeId 前一题映射id
     * @param previousOptionTreeId 前一题选项映射id（可选）
     * @param title 题目名称
     * @param type 类型
     * @param point 分值
     * @return questionTreeId
     */
    public Long addQuestionForPaper(Long adminId, Long paperId, Long previousQuestionTreeId, Long previousOptionTreeId,
                                    String title,
                                    ExamQuestionType type, Integer point){
        Optional<ExamQuestionEntity> oldQuestionEntityOptional = questionJpaRepo.findByPaperIdAndTitle(paperId,title);
        if(oldQuestionEntityOptional.isPresent()){
            throw new BizException(String.format("该问卷已经下存在题目《%s》",title));
        }
        ExamQuestionEntity questionEntity = new ExamQuestionEntity(IdUtil.generateId(),adminId,paperId,title,type,point);
        questionJpaRepo.save(questionEntity);
        return questionTreeProcess(adminId,paperId,previousQuestionTreeId,previousOptionTreeId,
        questionEntity.getId(),point);
    }

    private Long questionTreeProcess(Long adminId,Long paperId, Long previousQuestionTreeId,Long previousOptionTreeId,
    Long questionId,Integer point){
        //处理前后题目映射关系
        ExamPaperQuestionTreeEntity paperQuestionTreeEntity;
        ExamPaperQuestionTreeEntity previousPaperQuestionTreeEntity = null;

        if(previousQuestionTreeId == null){
            paperQuestionTreeEntity = new ExamPaperQuestionTreeEntity(IdUtil.generateId(),adminId,paperId,questionId,
                    true,point);
            paperQuestionTreeJpaRepo.save(paperQuestionTreeEntity);
        }else {
            paperQuestionTreeEntity = new ExamPaperQuestionTreeEntity(IdUtil.generateId(),adminId,
                    paperId,questionId,false,point);
            paperQuestionTreeJpaRepo.save(paperQuestionTreeEntity);

            previousPaperQuestionTreeEntity = examForCommonDomainService.findQuestionTree(previousQuestionTreeId);
            previousPaperQuestionTreeEntity.setNext(paperQuestionTreeEntity.getId());
            paperQuestionTreeJpaRepo.save(previousPaperQuestionTreeEntity);
        }
        //处理选项与题目映射关系
        if(previousOptionTreeId != null && previousPaperQuestionTreeEntity != null){
            ExamPaperQuestionOptionTreeEntity previousQuestionOptionTreeEntity = examForCommonDomainService.findQuestionOptionTree(previousOptionTreeId);
            previousQuestionOptionTreeEntity.setNextQuestionTreeId(paperQuestionTreeEntity.getId());
            paperQuestionOptionTreeJpaRepo.save(previousQuestionOptionTreeEntity);
        }
        flushPaper(paperId);
        return paperQuestionTreeEntity.getId();
    }


    public void updateQuestion(Long adminId,Long questionId,String title,Integer point){
        ExamQuestionEntity questionEntity = examForCommonDomainService.findQuestion(questionId);
        questionEntity.update(adminId,title,point);
        questionJpaRepo.save(questionEntity);
    }

    public void removeQuestionTree(Long treeId){
        Optional<ExamPaperQuestionTreeEntity> paperQuestionTreeEntityOptional = paperQuestionTreeJpaRepo.findById(treeId);
        if(!paperQuestionTreeEntityOptional.isPresent()) {
            return;
        }
        ExamPaperQuestionTreeEntity paperQuestionTreeEntity = paperQuestionTreeEntityOptional.get();
        paperQuestionTreeJpaRepo.delete(paperQuestionTreeEntity);

        List<ExamPaperQuestionOptionTreeEntity> paperQuestionOptionTreeEntityList = paperQuestionOptionTreeJpaRepo.findByQuestionTreeId(treeId);
        paperQuestionOptionTreeJpaRepo.deleteAll(paperQuestionOptionTreeEntityList);

        if(StringUtils.hasText(paperQuestionTreeEntity.getNextPaperQuestionTreeId())){
            String[] treeIdStrList = paperQuestionTreeEntity.getNextPaperQuestionTreeId().split(",");
            for (String s : treeIdStrList) {
                Long subTreeId = Long.valueOf(s);
                removeQuestionTree(subTreeId);
            }
        }
    }

    /**
     * 增加选项
     * @param adminId 管理员id
     * @param questionTreeId 问题树id
     * @param title 名称
     * @param endFlag 是否结束
     * @param correctFlag 是否正确
     * @param point 不定项选择题选项才有的得分
     * @return optionTreeId
     */
    public Long addOption(Long adminId,Long questionTreeId,String title,Boolean endFlag,Boolean correctFlag,Integer point){
        ExamPaperQuestionTreeEntity questionTreeEntity = examForCommonDomainService.findQuestionTree(questionTreeId);
        ExamQuestionEntity questionEntity = examForCommonDomainService.findQuestion(questionTreeEntity.getQuestionId());

        Optional<ExamQuestionOptionEntity> oldQuestionOptionEntityOptional =
         questionOptionJpaRepo.findByQuestionIdAndTitle(questionTreeEntity.getQuestionId(),title);
        if(oldQuestionOptionEntityOptional.isPresent()){
            throw new BizException(String.format("改题目以存在选项：%s",title));
        }
        ExamQuestionOptionEntity questionOptionEntity = new ExamQuestionOptionEntity(IdUtil.generateId(),adminId,
        questionTreeEntity.getQuestionId(),title,endFlag,correctFlag);
        if(ExamQuestionType.UN_SETTLED_CHOICE.equals(questionEntity.getType())){
            questionOptionEntity.setPointForUnSettledChoice(adminId,point);
        }
        questionOptionJpaRepo.save(questionOptionEntity);
        ExamPaperQuestionOptionTreeEntity optionTreeEntity = new ExamPaperQuestionOptionTreeEntity(IdUtil.generateId(),
         adminId,questionTreeId,
         questionOptionEntity.getId(),endFlag);
        paperQuestionOptionTreeJpaRepo.save(optionTreeEntity);
        return questionOptionEntity.getId();
    }


    public void updateOption(Long adminId,Long optionId,String title){
        ExamQuestionOptionEntity optionEntity = examForCommonDomainService.findQuestionOption(optionId);
        optionEntity.update(adminId,title);
        questionOptionJpaRepo.save(optionEntity);
    }


    public void removeOption(Long optionId){
        Optional<ExamQuestionOptionEntity> optionEntityOptional = questionOptionJpaRepo.findById(optionId);
        optionEntityOptional.ifPresent(questionOptionJpaRepo::delete);
    }

    /**
     * 标记此题目为最后一题
     * @param adminId 管理员id
     * @param paperQuestionTreeId 问卷中的题目id
     */
    public void markQuestionEnd(Long adminId,Long paperQuestionTreeId){
        ExamPaperQuestionTreeEntity questionTreeEntity = examForCommonDomainService.findQuestionTree(paperQuestionTreeId);
        questionTreeEntity.markEnd(adminId);
        paperQuestionTreeJpaRepo.save(questionTreeEntity);
    }

    /**
     * 取消标记此题目为最后一题
     * @param adminId 管理员id
     * @param paperQuestionTreeId 问卷中的题目id
     */
    public void unMarkQuestionEnd(Long adminId,Long paperQuestionTreeId){
        ExamPaperQuestionTreeEntity questionTreeEntity = examForCommonDomainService.findQuestionTree(paperQuestionTreeId);
        questionTreeEntity.unMarkEnd(adminId);
        paperQuestionTreeJpaRepo.save(questionTreeEntity);
    }

    /**
     * 标记此选项为结束整个问卷
     * @param adminId 管理员id
     * @param optionTreeId 问卷中的题目选项id
     */
    public void markOptionEnd(Long adminId,Long optionTreeId){
        ExamPaperQuestionOptionTreeEntity optionTreeEntity =
         examForCommonDomainService.findQuestionOptionTree(optionTreeId);
        optionTreeEntity.markEnd(adminId);
        paperQuestionOptionTreeJpaRepo.save(optionTreeEntity);
    }

    /**
     * 取消标记此选项为结束整个问卷
     * @param adminId 管理员id
     * @param optionTreeId 问卷中的题目选项id
     */
    public void unMarkOptionEnd(Long adminId,Long optionTreeId){
        ExamPaperQuestionOptionTreeEntity optionTreeEntity =
                examForCommonDomainService.findQuestionOptionTree(optionTreeId);
        optionTreeEntity.unMarkEnd(adminId);
        paperQuestionOptionTreeJpaRepo.save(optionTreeEntity);
    }

    /**
     * 标记此选项为正确
     * @param adminId 管理员id
     * @param optionTreeId 问卷中的题目选项id
     */
    public void markOptionCorrect(Long adminId,Long optionTreeId){
        ExamPaperQuestionOptionTreeEntity optionTreeEntity =
                examForCommonDomainService.findQuestionOptionTree(optionTreeId);
        ExamQuestionOptionEntity optionEntity = examForCommonDomainService.findQuestionOption(optionTreeEntity.getOptionId());
        optionEntity.markCorrect(adminId);
        questionOptionJpaRepo.save(optionEntity);
    }

    /**
     * 取消标记此选项为错误
     * @param adminId 管理员id
     * @param optionTreeId 问卷中的题目选项id
     */
    public void unMarkOptionCorrect(Long adminId,Long optionTreeId){
        ExamPaperQuestionOptionTreeEntity optionTreeEntity =
                examForCommonDomainService.findQuestionOptionTree(optionTreeId);
        ExamQuestionOptionEntity optionEntity = examForCommonDomainService.findQuestionOption(optionTreeEntity.getOptionId());
        optionEntity.unMarkCorrect(adminId);
        questionOptionJpaRepo.save(optionEntity);
    }


    public void completePaper(Long adminId,Long paperId){
        ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
        List<ExamPaperQuestionTreeEntity> paperQuestionTreeEntityList = paperQuestionTreeJpaRepo.findByPaperId(paperId);
        paperEntity.sum(paperQuestionTreeEntityList);
        paperEntity.complete(adminId);
        paperJpaRepo.save(paperEntity);
    }

    public void disablePaper(Long adminId,Long paperId){
        ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
        paperEntity.disable(adminId);
        paperJpaRepo.save(paperEntity);
    }

    /**
     * 每次新增完题目后会自动刷新问卷总分与总题数
     * @param paperId 问卷id
     */
    private void flushPaper(Long paperId){
         ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
         List<ExamPaperQuestionTreeEntity> paperQuestionTreeEntityList =
          paperQuestionTreeJpaRepo.findByPaperId(paperId);
         paperEntity.sum(paperQuestionTreeEntityList);
         paperJpaRepo.save(paperEntity);
    }

    public Integer unMappingPaperUser(Long adminId,Long paperId, List<Long> userIdList,
                                      List<Long> groupIdList){
        List<Long> deleteUserId = new ArrayList<>();
        List<ExamUserMappingEntity> realUserMapping = userMappingJpaRepo.findByPaperId(paperId);
        if(!CollectionUtils.isEmpty(groupIdList)){
            List<Long> specialUserIds = realUserMapping.stream()
                    .filter(ExamUserMappingEntity::getSpecial)
                    .map(ExamUserMappingEntity::getUserId).collect(Collectors.toList());
            for (Long aLong : groupIdList) {
                deleteUserId.addAll(userQueryService.findUserPage(0, Integer.MAX_VALUE, aLong, "")
                        .getContent().stream()
                        .map(UserDto::getId)
                        .filter(uId -> !specialUserIds.contains(uId))
                        .collect(Collectors.toList()));
            }
            groupMappingJpaRepo.deleteByPaperIdAndUserGroupIdIn(paperId,groupIdList);
        }
        if(!CollectionUtils.isEmpty(userIdList)){
            deleteUserId.addAll(userIdList);
        }

        userMappingJpaRepo.deleteByPaperIdAndUserIdIn(paperId,deleteUserId);
        return CollectionUtils.isEmpty(realUserMapping) ? 0 : deleteUserId.size();
    }

    public Integer mappingPaperUser(Long adminId,Long paperId, List<Long> userIdList,List<Long> groupIdList){
        if(userIdList == null){
            userIdList = new ArrayList<>();
        }
        if(groupIdList == null){
            groupIdList = new ArrayList<>();
        }
        Collection<Long> specialUserIdList = new ArrayList<>(userIdList);
        if(!CollectionUtils.isEmpty(groupIdList)){
            for (int i = 0; i < groupIdList.size(); i++) {
                userIdList.addAll(userQueryService.findUserPage(0,Integer.MAX_VALUE,groupIdList.get(i),"")
                        .getContent().stream().map(UserDto::getId).collect(Collectors.toList()));
            }
        }

        ExamPaperEntity paperEntity = examForCommonDomainService.findPaper(paperId);
        List<Long> oldMappingUserIdList = userMappingJpaRepo
                .findByPaperIdAndUserIdIn(paperId,userIdList).stream()
                .map(ExamUserMappingEntity::getUserId)
                .collect(Collectors.toList());
        List<Long> oldMappingGroupIdList = groupMappingJpaRepo.findByPaperId(paperId).stream()
                .map(ExamUserGroupMappingEntity::getUserGroupId)
                .collect(Collectors.toList());

        List<Long> validUserIdList = userIdList.stream()
                .filter(uId -> !oldMappingUserIdList.contains(uId))
                .collect(Collectors.toList());
        List<Long> validGroupIdList = groupIdList.stream()
                .filter(gId -> !oldMappingGroupIdList.contains(gId))
                .collect(Collectors.toList());

        if(ExamPaperStatus.ENABLE.equals(paperEntity.getStatus())) {
            List<ExamUserMappingEntity> mappingEntityList = new ArrayList<>();
            validUserIdList.forEach(userId -> {
                ExamUserMappingEntity uMapping = new ExamUserMappingEntity(IdUtil.generateId(), adminId, paperEntity.getId(), userId);
                if(specialUserIdList.contains(userId)) {
                    uMapping.markSpecial(adminId);
                }
                mappingEntityList.add(uMapping);
            });
            userMappingJpaRepo.saveAll(mappingEntityList);

            List<ExamUserGroupMappingEntity> groupMappingEntityList = new ArrayList<>();
            validGroupIdList.forEach(groupId -> groupMappingEntityList.add(
                    new ExamUserGroupMappingEntity(IdUtil.generateId(),adminId,paperEntity.getId(),groupId)));
            groupMappingJpaRepo.saveAll(groupMappingEntityList);
        }else {
            throw new BizException(String.format("试卷：%d，暂未启用",paperId));
        }
        return validUserIdList.size();
    }


    public void unMappingPaperUser(Long adminId,Long paperId, Collection<Long> userIdList){
        List<ExamUserMappingEntity> mappingEntityList = userMappingJpaRepo.findByPaperIdAndUserIdIn(paperId,userIdList);
        userMappingJpaRepo.deleteAll(mappingEntityList);
    }

    public void auditSuccess(Long adminId,Long paperInstanceId){
        ExamPaperInstanceEntity paperInstanceEntity = examForCommonDomainService.findPaperInstance(paperInstanceId);
        if(ExamPaperInstanceStatus.IN_PROGRESS.equals(paperInstanceEntity.getStatus())){
            throw new BizException("问卷实例未完成，不可审核");
        }
        if(ExamPaperInstanceStatus.COMPLETE.equals(paperInstanceEntity.getStatus())){
            throw new BizException("问卷实例未提交，不可审核");
        }
        paperInstanceEntity.auditSuccess(adminId);
        paperInstanceJpaRepo.save(paperInstanceEntity);
    }

    public void auditFail(Long adminId,Long paperInstanceId){
        ExamPaperInstanceEntity paperInstanceEntity = examForCommonDomainService.findPaperInstance(paperInstanceId);
        if(ExamPaperInstanceStatus.IN_PROGRESS.equals(paperInstanceEntity.getStatus())){
            throw new BizException("问卷实例未完成，不可审核");
        }
        if(ExamPaperInstanceStatus.COMPLETE.equals(paperInstanceEntity.getStatus())){
            throw new BizException("问卷实例未提交，不可审核");
        }
        paperInstanceEntity.auditFail(adminId);
        paperInstanceJpaRepo.save(paperInstanceEntity);
    }

    public void pointInputQuestionInstance(Long adminId,Long questionInstanceId,Integer point){
        ExamQuestionInstanceEntity questionInstanceEntity = examForCommonDomainService.findQuestionInstance(questionInstanceId);
        ExamPaperQuestionTreeEntity paperQuestionTreeEntity = examForCommonDomainService.findQuestionTree(questionInstanceEntity.getPaperQuestionTreeId());

        ExamQuestionEntity questionEntity = examForCommonDomainService.findQuestion(paperQuestionTreeEntity.getQuestionId());
        if(!ExamQuestionType.INPUT.equals(questionEntity.getType())){
            throw new BizException("只有主观题可以手动评分");
        }
        if(point > paperQuestionTreeEntity.getPoint()){
            throw new BizException(String.format("此题最高分值：%d",paperQuestionTreeEntity.getPoint()));
        }
        questionInstanceEntity.point(adminId,point);
        questionInstanceJpaRepo.save(questionInstanceEntity);
    }

    //todo
    public void copyPaper(Long paperId){}


}
