package xc.investigation.base.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.constant.domain.ExamQuestionType;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.constant.domain.SysAdminStatus;
import xc.investigation.base.dto.ExamInstanceFileDto;
import xc.investigation.base.dto.ExamPaperInstanceEditableDto;
import xc.investigation.base.dto.ExamQuestionInstanceDto;
import xc.investigation.base.repo.entity.exam.*;
import xc.investigation.base.repo.entity.sys.SysAdminEntity;
import xc.investigation.base.repo.jpa.exam.*;
import xc.investigation.base.repo.jpa.sys.SysAdminJpaRepo;
import xc.investigation.base.utils.IdUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExamForCommonDomainService {

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
    private final SysAdminJpaRepo sysAdminJpaRepo;

    public ExamForCommonDomainService(ExamBatchJpaRepo batchJpaRepo, ExamPaperJpaRepo paperJpaRepo, ExamQuestionJpaRepo questionJpaRepo, ExamQuestionOptionJpaRepo questionOptionJpaRepo, ExamPaperInstanceJpaRepo paperInstanceJpaRepo, ExamQuestionInstanceJpaRepo questionInstanceJpaRepo, ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo, ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo, ExamUserMappingJpaRepo userMappingJpaRepo, ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo, ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo, SysAdminJpaRepo sysAdminJpaRepo) {
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
        this.sysAdminJpaRepo = sysAdminJpaRepo;
    }

    public ExamPaperEntity findPaper(Long paperId) {
        Optional<ExamPaperEntity> paperEntityOptional = paperJpaRepo.findById(paperId);
        if (!paperEntityOptional.isPresent()) {
            throw new BizException("请先创建问卷");
        }
        return paperEntityOptional.get();
    }

    public ExamPaperInstanceEntity findPaperInstance(Long paperInstanceId) {
        Optional<ExamPaperInstanceEntity> paperInstanceEntityOptional = paperInstanceJpaRepo.findById(paperInstanceId);
        if (!paperInstanceEntityOptional.isPresent()) {
            throw new BizException("问卷实例不存在");
        }
        return paperInstanceEntityOptional.get();
    }


    public ExamQuestionInstanceEntity findQuestionInstance(Long questionInstanceId) {
        Optional<ExamQuestionInstanceEntity> questionInstanceEntityOptional = questionInstanceJpaRepo.findById(questionInstanceId);
        if (!questionInstanceEntityOptional.isPresent()) {
            throw new BizException(String.format("题目实例不存在：%d", questionInstanceId));
        }
        return questionInstanceEntityOptional.get();
    }

    public ExamQuestionEntity findQuestion(Long questionId) {
        Optional<ExamQuestionEntity> questionEntityOptional = questionJpaRepo.findById(questionId);
        if (!questionEntityOptional.isPresent()) {
            throw new BizException(String.format("题目：%d，不存在", questionId));
        }
        return questionEntityOptional.get();
    }

    public ExamPaperQuestionTreeEntity findQuestionTree(Long questionTreeId) {
        Optional<ExamPaperQuestionTreeEntity> questionTreeEntityOptional = paperQuestionTreeJpaRepo.
                findById(questionTreeId);
        if (!questionTreeEntityOptional.isPresent()) {
            throw new BizException(String.format("试卷中无此题：%d", questionTreeId));
        }
        return questionTreeEntityOptional.get();

    }

    public ExamQuestionOptionEntity findQuestionOption(Long optionId) {
        Optional<ExamQuestionOptionEntity> optionEntityOptional = questionOptionJpaRepo.findById(optionId);
        if (!optionEntityOptional.isPresent()) {
            throw new BizException(String.format("选项：%d不存在", optionId));
        }
        return optionEntityOptional.get();
    }

    public ExamPaperQuestionOptionTreeEntity findQuestionOptionTree(Long optionTreeId) {
        Optional<ExamPaperQuestionOptionTreeEntity> previousQuestionOptionTreeEntityOptional = paperQuestionOptionTreeJpaRepo.findById(optionTreeId);
        if (!previousQuestionOptionTreeEntityOptional.isPresent()) {
            throw new BizException(String.format("选项：%d，在题目中不存在", optionTreeId));
        }
        return previousQuestionOptionTreeEntityOptional.get();
    }

    public Integer calculateQuestionPoint(ExamQuestionEntity questionEntity,
                                          List<ExamQuestionOptionEntity> optionEntityList,
                                          String answer) {
        if (ExamQuestionType.INPUT.equals(questionEntity.getType())) {
            return 0;
        }
        List<ExamQuestionOptionEntity> correctOptionList = optionEntityList.stream()
                .filter(ExamQuestionOptionEntity::getCorrectFlag)
                .collect(Collectors.toList());
        List<Long> correctOptionIdList = correctOptionList.stream().map(ExamQuestionOptionEntity::getId).collect(Collectors.toList());

        int point = 0;
        Optional<ExamPaperQuestionOptionTreeEntity> optionTreeEntityOpt = Optional.empty();
        switch (questionEntity.getType()) {
            case JUDGMENT:
            case SINGLE_CHOICE:
                optionTreeEntityOpt = paperQuestionOptionTreeJpaRepo.findById(Long.parseLong(answer));
                if (optionTreeEntityOpt.isPresent()) {
                    Long optionId = optionTreeEntityOpt.get().getOptionId();
                    Optional<Long> correctOption = correctOptionIdList.stream().filter(id -> id.equals(optionId)).findAny();
                    if (correctOption.isPresent()) {
                        point = questionEntity.getPoint();
                    }
                }
                break;
            case MULTIPLE_CHOICE:
                boolean allCorrect = true;
                List<Long> answerIdList = Arrays.stream(answer.split(",")).map(Long::parseLong).collect(Collectors.toList());
                if (answerIdList.size() == correctOptionList.size()) {
                    for (Long aId : answerIdList) {
                        optionTreeEntityOpt = paperQuestionOptionTreeJpaRepo.findById(aId);
                        if (optionTreeEntityOpt.isPresent() && !correctOptionIdList.contains(optionTreeEntityOpt.get().getOptionId())) {
                            allCorrect = false;
                            break;
                        }
                    }
                    if (allCorrect) {
                        point = questionEntity.getPoint();
                    }
                }
                break;
            case UN_SETTLED_CHOICE:
                optionTreeEntityOpt = paperQuestionOptionTreeJpaRepo.findById(Long.parseLong(answer));
                if (optionTreeEntityOpt.isPresent()) {
                    Long optionId = optionTreeEntityOpt.get().getOptionId();
                    Optional<ExamQuestionOptionEntity> optionEntityOptional = optionEntityList.stream()
                            .filter(o -> o.getId().equals(optionId)).findFirst();
                    if (optionEntityOptional.isPresent()) {
                        point = optionEntityOptional.get().getPoint();
                    }
                }
                break;
            default:
        }
        return point;
    }

    public Integer calculatePaperPoint(List<ExamQuestionInstanceEntity> questionInstanceEntityList) {
        return questionInstanceEntityList.stream().mapToInt(ExamQuestionInstanceEntity::getPoint).sum();
    }

    public void createPaperInstance(Long createId, Long paperId, String bankCode, LocalDateTime startTime,
                                    LocalDateTime endTime, List<ExamInstanceFileDto> paperFileList, List<ExamQuestionInstanceDto> answerList) {

        ExamPaperInstanceEntity paperInstanceEntity = new ExamPaperInstanceEntity(IdUtil.generateId(), createId,
                paperId, createId, bankCode, startTime, endTime);
        paperInstanceJpaRepo.save(paperInstanceEntity);
        if (!CollectionUtils.isEmpty(paperFileList)) {
            paperFileList.forEach(pf -> paperInstanceFileJpaRepo.save(
                    new ExamPaperInstanceFileEntity(IdUtil.generateId(), createId,
                            paperInstanceEntity.getId(), pf.getType(), pf.getUrl(), pf.getDescription())));
        }
        List<ExamQuestionInstanceEntity> questionInstanceEntityList = saveQuestionInstance(paperInstanceEntity, answerList, createId);
        savePaperInstance(paperInstanceEntity, questionInstanceEntityList, createId);
    }

    public void editPaperInstance(Long paperInstanceId, Long editId, String bankCode, LocalDateTime startTime,
                                  LocalDateTime endTime, List<ExamInstanceFileDto> paperFileList, List<ExamQuestionInstanceDto> answerList) {
        ExamPaperInstanceEntity paperInstanceEntity = findPaperInstance(paperInstanceId);
        ExamPaperInstanceEditableDto editableDto = checkEditable(paperInstanceEntity, editId);
        List<ExamQuestionInstanceEntity> questionInstanceEntityList = questionInstanceJpaRepo.findByPaperInstanceId(paperInstanceId);

        if (!editableDto.getEditable()) {
            throw new BizException(editableDto.getInfo());
        }
        //删除老数据
        questionInstanceJpaRepo.deleteAll(questionInstanceEntityList);
        paperInstanceFileJpaRepo.deleteByPaperInstanceId(paperInstanceId);
        questionInstanceFileJpaRepo.deleteByQuestionInstanceIdIn(questionInstanceEntityList.stream().map(ExamQuestionInstanceEntity::getId).collect(Collectors.toList()));

        if (!CollectionUtils.isEmpty(paperFileList)) {
            paperFileList.forEach(pf -> paperInstanceFileJpaRepo.save(
                    new ExamPaperInstanceFileEntity(IdUtil.generateId(), editId,
                            paperInstanceEntity.getId(), pf.getType(), pf.getUrl(), pf.getDescription())));
        }

        questionInstanceEntityList = saveQuestionInstance(paperInstanceEntity, answerList, editId);
        paperInstanceEntity.update(editId, bankCode, startTime, endTime);
        savePaperInstance(paperInstanceEntity, questionInstanceEntityList, editId);
    }

    private void savePaperInstance(ExamPaperInstanceEntity paperInstanceEntity, List<ExamQuestionInstanceEntity> questionInstanceEntityList, Long saveId) {
        int paperPoint = calculatePaperPoint(questionInstanceEntityList);
        paperInstanceEntity.setPoint(saveId, paperPoint);
        paperInstanceEntity.setQuestionNumber(saveId, questionInstanceEntityList.size());
        paperInstanceJpaRepo.save(paperInstanceEntity);
    }

    private List<ExamQuestionInstanceEntity> saveQuestionInstance(ExamPaperInstanceEntity paperInstanceEntity, List<ExamQuestionInstanceDto> answerList, Long userId) {
        long paperId = paperInstanceEntity.getPaperId();

        List<ExamPaperQuestionTreeEntity> questionTreeEntityList = paperQuestionTreeJpaRepo.findByPaperId(paperId);
        List<ExamQuestionEntity> questionEntityList = questionJpaRepo.findAllById(questionTreeEntityList
                .stream()
                .map(ExamPaperQuestionTreeEntity::getQuestionId)
                .collect(Collectors.toList()));
        Map<Long, ExamQuestionEntity> questionEntityMap = questionEntityList.stream()
                .collect(Collectors.toMap(ExamQuestionEntity::getId, q -> q));

        Map<Long, ExamQuestionEntity> questionTreeEntityMap = questionTreeEntityList.stream()
                .collect(Collectors.toMap(ExamPaperQuestionTreeEntity::getId, t -> questionEntityMap.get(t.getQuestionId())));


        List<Long> questionIdList = questionEntityList.stream().map(ExamQuestionEntity::getId).collect(Collectors.toList());

        List<ExamQuestionOptionEntity> optionEntityList = questionOptionJpaRepo.findByQuestionIdIn(questionIdList);
        Map<Long, List<ExamQuestionOptionEntity>> optionEntityListMap = optionEntityList.stream()
                .collect(Collectors.groupingBy(ExamQuestionOptionEntity::getQuestionId));

        List<ExamQuestionInstanceEntity> questionInstanceEntityList = new ArrayList<>();
        answerList.forEach(a -> {
            String answerInner = a.getAnswer();
            ExamQuestionEntity questionInner = questionTreeEntityMap.get(a.getQuestionTreeId());
            List<ExamQuestionOptionEntity> optionListInner = optionEntityListMap.get(questionInner.getId());

            int pointInner = calculateQuestionPoint(questionInner, optionListInner, answerInner);

            ExamQuestionInstanceEntity questionInstanceEntity = new ExamQuestionInstanceEntity(IdUtil.generateId(),
                    userId, paperInstanceEntity.getId(), a.getQuestionTreeId(), answerInner, pointInner);
            questionInstanceJpaRepo.save(questionInstanceEntity);
            questionInstanceEntityList.add(questionInstanceEntity);

            if (!CollectionUtils.isEmpty(a.getFileList())) {
                a.getFileList().forEach(af -> questionInstanceFileJpaRepo.save(new ExamQuestionInstanceFileEntity(IdUtil.generateId(), userId,
                        questionInstanceEntity.getId(), af.getType(), af.getUrl(), af.getDescription())));
            }
        });
        questionInstanceJpaRepo.saveAll(questionInstanceEntityList);
        return questionInstanceEntityList;
    }

    public void editQuestionInstanceFile(Long editId, Long questionInstanceId, Collection<ExamInstanceFileDto> fileDtoList) {

        List<ExamQuestionInstanceFileEntity> fileEntityList = questionInstanceFileJpaRepo.findByQuestionInstanceId(questionInstanceId);
        questionInstanceFileJpaRepo.deleteAll(fileEntityList);

        fileDtoList.forEach(f -> questionInstanceFileJpaRepo.save(
                new ExamQuestionInstanceFileEntity(IdUtil.generateId(), editId, questionInstanceId, f.getType(), f.getUrl(), f.getDescription())));
    }

    public void editPaperInstanceFile(Long editId, Long paperInstanceId, Collection<ExamInstanceFileDto> fileDtoList) {

        List<ExamPaperInstanceFileEntity> fileEntityList = paperInstanceFileJpaRepo.findByPaperInstanceId(paperInstanceId);
        paperInstanceFileJpaRepo.deleteAll(fileEntityList);

        fileDtoList.forEach(f -> paperInstanceFileJpaRepo.save(
                new ExamPaperInstanceFileEntity(IdUtil.generateId(), editId, paperInstanceId, f.getType(), f.getUrl(), f.getDescription())));
    }

    public ExamPaperInstanceEditableDto checkEditable(Long paperInstanceId, Long editId) {
        ExamPaperInstanceEntity paperInstanceEntity = findPaperInstance(paperInstanceId);
        return checkEditable(paperInstanceEntity, editId);
    }

    public ExamPaperInstanceEditableDto checkEditable(ExamPaperInstanceEntity paperInstanceEntity, Long editId) {
        Optional<SysAdminEntity> adminEntityOptional = sysAdminJpaRepo.findById(editId);
        if (adminEntityOptional.isPresent()) {
            //管理员编辑
            SysAdminEntity adminEntity = adminEntityOptional.get();
            if (SysAdminStatus.DISABLE.equals(adminEntity.getStatus())) {
                return new ExamPaperInstanceEditableDto(false, "该管理员已禁用");
            }
            if (!SysAdminRoleType.ADMIN.equals(adminEntity.getRoleType())) {
                return new ExamPaperInstanceEditableDto(false, "该管理员无权限编辑");
            }
            if (ExamPaperInstanceStatus.IN_PROGRESS.equals(paperInstanceEntity.getStatus())) {
                return new ExamPaperInstanceEditableDto(false, "该问卷正在进行中，请勿编辑");
            }
        } else {
            //用户编辑
            if (ExamPaperInstanceStatus.AUDIT_SUCCESS.equals(paperInstanceEntity.getStatus())) {
                return new ExamPaperInstanceEditableDto(false, "审核成功的问卷不可编辑");
            }
            if (ExamPaperInstanceStatus.COMPLETE.equals(paperInstanceEntity.getStatus())) {
                return new ExamPaperInstanceEditableDto(false, "已完成的问卷不可编辑");
            }
            if (ExamPaperInstanceStatus.SUBMIT.equals(paperInstanceEntity.getStatus())) {
                return new ExamPaperInstanceEditableDto(false, "已提交的问卷不可编辑");
            }
        }
        return new ExamPaperInstanceEditableDto(true, "");
    }

}
