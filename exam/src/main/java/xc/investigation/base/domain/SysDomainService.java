package xc.investigation.base.domain;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.domain.Model.SysAdminModel;
import xc.investigation.base.repo.entity.bank.BankEntity;
import xc.investigation.base.repo.entity.sys.SysAdminEntity;
import xc.investigation.base.repo.entity.sys.SysAdminTokenEntity;
import xc.investigation.base.repo.entity.sys.SysModuleEntity;
import xc.investigation.base.repo.entity.sys.SysModuleFunctionEntity;
import xc.investigation.base.repo.entity.sys.SysModuleFunctionMappingEntity;
import xc.investigation.base.repo.jpa.bank.BankJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysAdminJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysAdminTokenJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysModuleFunctionJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysModuleFunctionMappingJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysModuleJpaRepo;
import xc.investigation.base.utils.IdUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDomainService {

    private final SysAdminJpaRepo adminJpaRepo;
    private final SysAdminTokenJpaRepo adminTokenJpaRepo;
    private final BankJpaRepo bankJpaRepo;
    private final SysModuleJpaRepo moduleJpaRepo;
    private final SysModuleFunctionJpaRepo moduleFunctionJpaRepo;
    private final SysModuleFunctionMappingJpaRepo moduleFunctionMappingJpaRepo;

    public SysDomainService(SysAdminJpaRepo adminJpaRepo, SysAdminTokenJpaRepo adminTokenJpaRepo, BankJpaRepo bankJpaRepo, SysModuleJpaRepo moduleJpaRepo, SysModuleFunctionJpaRepo moduleFunctionJpaRepo, SysModuleFunctionMappingJpaRepo moduleFunctionMappingJpaRepo) {
        this.adminJpaRepo = adminJpaRepo;
        this.adminTokenJpaRepo = adminTokenJpaRepo;
        this.bankJpaRepo = bankJpaRepo;
        this.moduleJpaRepo = moduleJpaRepo;
        this.moduleFunctionJpaRepo = moduleFunctionJpaRepo;
        this.moduleFunctionMappingJpaRepo = moduleFunctionMappingJpaRepo;
    }

    public void createAdmin(Long adminId, String name, String pwd, String bankCode, SysAdminRoleType roleType){
        Optional<SysAdminEntity> oldAdminEntityOptional = adminJpaRepo.findByNameAndBankCode(name,bankCode);
        if(oldAdminEntityOptional.isPresent()){
            throw new BizException(String.format("该银行已有管理员：%s",name));
        }
        SysAdminEntity adminEntity = new SysAdminEntity(IdUtil.generateId(),adminId,bankCode,name,pwd,roleType);
        adminJpaRepo.save(adminEntity);
    }

    public void disable(Long adminId,Long updateId){
        Optional<SysAdminEntity> adminEntityOptional = adminJpaRepo.findById(adminId);
        if(!adminEntityOptional.isPresent()){
            throw new BizException("管理员不存在");
        }
        SysAdminEntity adminEntity = adminEntityOptional.get();
        adminEntity.disable(updateId);
        adminJpaRepo.save(adminEntity);
    }

    public void enable(Long adminId,Long updateId){
        Optional<SysAdminEntity> adminEntityOptional = adminJpaRepo.findById(adminId);
        if(!adminEntityOptional.isPresent()){
            throw new BizException("管理员不存在");
        }
        SysAdminEntity adminEntity = adminEntityOptional.get();
        adminEntity.enable(updateId);
        adminJpaRepo.save(adminEntity);
    }

    public SysAdminModel login(String name, String pwd){
        Optional<SysAdminEntity> adminEntityOptional = adminJpaRepo.findByNameAndPwd(name,pwd);
        if(!adminEntityOptional.isPresent()){
            throw new BizException("用户名或密码错误");
        }
        SysAdminEntity adminEntity = adminEntityOptional.get();
        String token = IdUtil.generateToken();
        Optional<SysAdminTokenEntity> oldAdminTokenOptional = adminTokenJpaRepo.findByCreateId(adminEntity.getId());
        oldAdminTokenOptional.ifPresent(adminTokenJpaRepo::delete);
        SysAdminTokenEntity adminTokenEntity = new SysAdminTokenEntity(adminEntity.getId(),token);
        adminTokenJpaRepo.save(adminTokenEntity);
        return buildModel(adminEntity,Optional.of(adminTokenEntity));
    }

    private SysAdminModel buildModel(Long id){
        Optional<SysAdminEntity> sysAdminEntityOptional = adminJpaRepo.findById(id);
        Optional<SysAdminTokenEntity> sysAdminTokenEntityOptional = adminTokenJpaRepo.findByCreateId(id);
        if(!sysAdminEntityOptional.isPresent()){
            throw new BizException("管理员不存在");
        }
        return buildModel(sysAdminEntityOptional.get(),sysAdminTokenEntityOptional);
    }

    private SysAdminModel buildModel(SysAdminEntity adminEntity,Optional<SysAdminTokenEntity> adminTokenEntityOptional){
        String currentToken = "";
        if(adminTokenEntityOptional.isPresent()){
            currentToken = adminTokenEntityOptional.get().getToken();
        }
        BankEntity bankEntity = bankJpaRepo.findByCode(adminEntity.getBankCode());
        JSONArray menus = buildAdminMenuJson(adminEntity.getRoleType());

        return new SysAdminModel(adminEntity.getId(),adminEntity.getName(),
        adminEntity.getBankCode(),bankEntity.getName(),currentToken,adminEntity.getStatus(),
                adminEntity.getRoleType(),menus);
    }

    private JSONArray buildAdminMenuJson(SysAdminRoleType roleType){
        List<SysModuleEntity> moduleEntityList = moduleJpaRepo.findAll();

        List<SysModuleFunctionEntity> functionEntityList;
        if(SysAdminRoleType.ADMIN.equals(roleType)) {
            functionEntityList = moduleFunctionJpaRepo.findAll();
        }else {
            List<SysModuleFunctionMappingEntity> functionMappingEntityList = moduleFunctionMappingJpaRepo.findByRoleType(roleType);
            if (CollectionUtils.isEmpty(functionMappingEntityList)) {
                return new JSONArray();
            }
            functionEntityList = moduleFunctionJpaRepo.findAllById(functionMappingEntityList
                    .stream()
                    .map(SysModuleFunctionMappingEntity::getFunctionId)
                    .collect(Collectors.toList()));
        }

        Map<Long,SysModuleEntity> modelMap = moduleEntityList
                .stream()
                .collect(Collectors.toMap(SysModuleEntity::getId,m -> m));
        Map<Long,List<SysModuleFunctionEntity>> functionModelMap = functionEntityList
                .stream()
                .collect(Collectors.groupingBy(SysModuleFunctionEntity::getModuleId));

        JSONArray menus = new JSONArray();
        functionModelMap.forEach((moduleId, moduleFunctionEntityList) -> {
            SysModuleEntity moduleEntity = modelMap.get(moduleId);

            JSONObject menu = new JSONObject();
            menu.putIfAbsent("text", moduleEntity.getTitle());
            menu.putIfAbsent("iconCls", "icon-more");

            JSONArray childMenus = new JSONArray();
            moduleFunctionEntityList.forEach(function -> {
                JSONObject childMenu = new JSONObject();
                childMenu.putIfAbsent("text", function.getTitle());
                childMenu.putIfAbsent("innerPage", function.getInnerPath());
                childMenus.add(childMenu);
            });
            menu.putIfAbsent("children", childMenus);
            menus.add(menu);
        });
        return menus;
    }
}
