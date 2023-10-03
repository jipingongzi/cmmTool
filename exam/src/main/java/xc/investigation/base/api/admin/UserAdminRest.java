package xc.investigation.base.api.admin;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.domain.UserDomainService;
import xc.investigation.base.dto.UserDetailDto;
import xc.investigation.base.dto.UserDto;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.query.UserQueryService;
import xc.investigation.base.repo.entity.user.UserGroupEntity;
import xc.investigation.base.utils.WebUtil;

import java.util.List;

/**
 * @author ibm
 */
@RestController
@RequestMapping("/admin/user")
public class UserAdminRest {

    private final UserQueryService userQueryService;
    private final ExamQueryService examQueryService;
    private final UserDomainService userDomainService;
    private final WebUtil webUtil;

    public UserAdminRest(UserQueryService userQueryService, ExamQueryService examQueryService, UserDomainService userDomainService, WebUtil webUtil) {
        this.userQueryService = userQueryService;
        this.examQueryService = examQueryService;
        this.userDomainService = userDomainService;
        this.webUtil = webUtil;
    }

    private static final String GET_USER_LIST = "/list";
    private static final String GET_GROUP_LIST = "/group/list";
    private static final String GET_USER_DETAIL = "/detail/{userId}";

    @GetMapping(value = GET_USER_LIST)
    public ResponseVo<Page<UserDto>> userList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                             @RequestParam(value = "rows",defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "name",required = false)String name,
                                             @RequestParam(value = "groupId",required = false)Long groupId){
        return ResponseVo.of(userQueryService.findUserPage(pageNo - 1,pageSize,groupId,name));
    }

    @GetMapping(value = GET_GROUP_LIST)
    public ResponseVo<List<UserGroupEntity>> groupList(){
        return ResponseVo.of(userQueryService.findGroups());
    }

    @GetMapping(value = GET_USER_DETAIL)
    public ResponseVo<UserDetailDto> userDetail(@PathVariable("userId")Long userId){
        return ResponseVo.of(new UserDetailDto(
                        userQueryService.findUserDto(userId),
                        examQueryService.findPaperInstanceListDtoByUserId(userId)));
    }
}
