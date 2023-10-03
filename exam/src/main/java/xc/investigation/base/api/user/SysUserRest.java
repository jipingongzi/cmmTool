package xc.investigation.base.api.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.api.vo.user.UserLoginVo;
import xc.investigation.base.domain.Model.UserModel;
import xc.investigation.base.domain.UserDomainService;

/**
 * @author seanx
 */
@RestController
@RequestMapping("/user/sys")
@Slf4j
public class SysUserRest {

    private final static String POST_LOGIN = "login";

    private final UserDomainService userDomainService;

    public SysUserRest(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    @PostMapping(value = POST_LOGIN)
    public ResponseVo<UserModel> login(@RequestBody UserLoginVo loginVo){
        return ResponseVo.of(userDomainService.login(loginVo.getIdNo(),loginVo.getPwd()));
    }


}
