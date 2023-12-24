package xc.investigation.base.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.constant.domain.UserStatus;
import xc.investigation.base.domain.Model.UserModel;
import xc.investigation.base.dto.UserRegisterDto;
import xc.investigation.base.repo.entity.user.UserEntity;
import xc.investigation.base.repo.entity.user.UserTokenEntity;
import xc.investigation.base.repo.jpa.user.UserJpaRepo;
import xc.investigation.base.repo.jpa.user.UserTokenJpaRepo;
import xc.investigation.base.utils.IdUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 用户领域服务
 *
 * @author ibm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDomainService {

    private final UserJpaRepo userJpaRepo;
    private final UserTokenJpaRepo userTokenJpaRepo;

    public UserDomainService(UserJpaRepo userJpaRepo, UserTokenJpaRepo userTokenJpaRepo) {
        this.userJpaRepo = userJpaRepo;
        this.userTokenJpaRepo = userTokenJpaRepo;
    }

    /**
     * 用户注册
     *
     * @param name         用户名
     * @param idNo         身份证号码
     * @param portraitPath 头像
     * @param pwd          密码
     * @return 用户模型
     */

    public UserModel register(String name, String idNo, String portraitPath, String pwd) {
        Optional<UserEntity> oldUserOptional = userJpaRepo.findByIdNo(idNo);
        if (oldUserOptional.isPresent()) {
            throw new BizException(String.format("身份证：%s，已注册！", idNo));
        }
        Long id = IdUtil.generateId();
        UserEntity userEntity = new UserEntity(id, name, idNo, portraitPath, pwd, id);
        userJpaRepo.save(userEntity);
        return buildUserModel(id);
    }

    /**
     * 批量导入用户
     *
     * @param userRegisterDtoList 从excel文件中解析
     * @param adminId             管理员id
     */

    public void create(Collection<UserRegisterDto> userRegisterDtoList, Long adminId) {
        if (CollectionUtils.isEmpty(userRegisterDtoList)) {
            return;
        }
        List<UserEntity> userEntityList = new ArrayList<>();
        userRegisterDtoList.forEach(dto -> {
            Long id = IdUtil.generateId();
            userEntityList.add(new UserEntity(id, dto.getName(), dto.getIdNo(),
                    dto.getPortraitPath(), dto.getPwd(), adminId));
        });
        userJpaRepo.saveAll(userEntityList);
    }

    /**
     * 用户登录
     *
     * @param idNo 身份证号码
     * @param pwd  密码
     * @return 用户模型
     */

    public UserModel login(String idNo, String pwd) {
        Optional<UserEntity> userOptional = userJpaRepo.findByIdNoAndPwd(idNo, pwd);
        if (!userOptional.isPresent()) {
            throw new BizException("身份证号或密码错误！");
        }
        UserEntity userEntity = userOptional.get();
        if (UserStatus.DISABLE.equals(userEntity.getStatus())) {
            throw new BizException("用户已禁用！");
        }
        String token = IdUtil.generateToken();
        Optional<UserTokenEntity> oldUserTokenOptional = userTokenJpaRepo.findByCreateId(userEntity.getId());
        oldUserTokenOptional.ifPresent(userTokenJpaRepo::delete);
        UserTokenEntity userTokenEntity = new UserTokenEntity(IdUtil.generateId(), userEntity.getId(), token);
        userTokenJpaRepo.save(userTokenEntity);
        return buildUserModel(userEntity.getId());
    }

    /**
     * 用户退出
     *
     * @param userId 用户id
     */

    public void logout(Long userId) {
        Optional<UserEntity> userOptional = userJpaRepo.findById(userId);
        if (!userOptional.isPresent()) {
            return;
        }
        Optional<UserTokenEntity> userTokenOptional = userTokenJpaRepo.findByCreateId(userId);
        userTokenOptional.ifPresent(userTokenJpaRepo::delete);
    }

    /**
     * 启用用户
     *
     * @param userId 用户id
     */
    public void enable(Long userId) {
        Optional<UserEntity> userOptional = userJpaRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.enable();
            userJpaRepo.save(userEntity);
        }
    }

    /**
     * 禁用用户
     *
     * @param userId 用户id
     */
    public void disable(Long userId) {
        Optional<UserEntity> userOptional = userJpaRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.disable();
            userJpaRepo.save(userEntity);
        }
    }

    //t
    private UserModel buildUserModel(Long id) {
        Optional<UserEntity> userEntityOptional = userJpaRepo.findById(id);
        if (!userEntityOptional.isPresent()) {
            throw new BizException("用户不存在");
        }
        UserEntity userEntity = userEntityOptional.get();
        Optional<UserTokenEntity> tokenEntityOptional = userTokenJpaRepo.findByCreateId(id);
        String token = "";
        if (tokenEntityOptional.isPresent()) {
            token = tokenEntityOptional.get().getToken();
        }
        return new UserModel(id, userEntity.getName(), userEntity.getIdNo(), userEntity.getPortraitPath(),
                userEntity.getStatus(), token);
    }
}
