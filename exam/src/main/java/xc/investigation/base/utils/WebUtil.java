package xc.investigation.base.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.config.exception.SystemException;
import xc.investigation.base.repo.entity.sys.SysAdminEntity;
import xc.investigation.base.repo.entity.sys.SysAdminTokenEntity;
import xc.investigation.base.repo.entity.user.UserEntity;
import xc.investigation.base.repo.entity.user.UserTokenEntity;
import xc.investigation.base.repo.jpa.sys.SysAdminJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysAdminTokenJpaRepo;
import xc.investigation.base.repo.jpa.user.UserJpaRepo;
import xc.investigation.base.repo.jpa.user.UserTokenJpaRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
@Component
public class WebUtil {

    private static final ThreadLocal<SysAdminEntity> ADMIN_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<UserEntity> USER_HOLDER = new ThreadLocal<>();

    private final SysAdminJpaRepo adminJpaRepo;
    private final SysAdminTokenJpaRepo adminTokenJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final UserTokenJpaRepo userTokenJpaRepo;

    public WebUtil(SysAdminJpaRepo adminJpaRepo, SysAdminTokenJpaRepo adminTokenJpaRepo,
                   UserJpaRepo userJpaRepo, UserTokenJpaRepo userTokenJpaRepo) {
        this.adminJpaRepo = adminJpaRepo;
        this.adminTokenJpaRepo = adminTokenJpaRepo;
        this.userJpaRepo = userJpaRepo;
        this.userTokenJpaRepo = userTokenJpaRepo;
    }


    public void setCurrentAdmin(String token){
        SysAdminEntity admin = checkAdminByToken(token);
        ADMIN_HOLDER.set(admin);
    }

    public SysAdminEntity getCurrentAdmin(){
        return ADMIN_HOLDER.get();
    }

    public void removeCurrentAdmin(){
        ADMIN_HOLDER.remove();
    }

    public void setCurrentUser(String token){
        UserEntity userEntity = checkUserByToken(token);
        USER_HOLDER.set(userEntity);
    }

    public UserEntity getCurrentUser(){
        return USER_HOLDER.get();
    }

    public void removeCurrentUser(){
        USER_HOLDER.remove();
    }

    public String checkBankCode(String bankCode){
        SysAdminEntity adminEntity = getCurrentAdmin();
        if(!StringUtils.hasText(bankCode)){
            bankCode = adminEntity.getBankCode();
        }else {
            if(!bankCode.startsWith(adminEntity.getBankCode())){
                throw new BizException("无权访问此类银行数据");
            }
        }
        return bankCode;
    }

    public Boolean isStaticResource(String uri){
        List<String> staticResourceSuffixList = Arrays.asList(".html",".js",".css",".png",".jpg",".mp4",".avi",".ico"
        ,".gif","/export");
        for (String staticResourceSuffix : staticResourceSuffixList) {
            if(uri.endsWith(staticResourceSuffix)){
                return true;
            }
        }
        return false;
    }

    private UserEntity checkUserByToken(String token){
        Optional<UserTokenEntity> userTokenEntityOptional = userTokenJpaRepo.findByToken(token);
        if(!userTokenEntityOptional.isPresent()){
            throw SystemException.userNotLogin();
        }
        UserTokenEntity userTokenEntity = userTokenEntityOptional.get();
        Optional<UserEntity> userEntityOptional = userJpaRepo.findById(userTokenEntity.getCreateId());
        if(!userEntityOptional.isPresent()){
            throw SystemException.userNotExist();
        }
        return userEntityOptional.get();
    }

    private SysAdminEntity checkAdminByToken(String token){
        Optional<SysAdminTokenEntity>  adminTokenEntityOptional = adminTokenJpaRepo.findByToken(token);
        if(!adminTokenEntityOptional.isPresent()){
            throw SystemException.adminNotLogin();
        }
        SysAdminTokenEntity adminTokenEntity = adminTokenEntityOptional.get();
        Optional<SysAdminEntity> adminEntityOptional = adminJpaRepo.findById(adminTokenEntity.getCreateId());
        if(!adminEntityOptional.isPresent()){
            throw SystemException.adminNotExist();
        }
        return adminEntityOptional.get();
    }
}
