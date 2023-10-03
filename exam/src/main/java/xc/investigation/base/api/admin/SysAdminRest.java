package xc.investigation.base.api.admin;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.api.vo.sys.AdminCreateVo;
import xc.investigation.base.api.vo.sys.AdminLoginVo;
import xc.investigation.base.domain.Model.SysAdminModel;
import xc.investigation.base.domain.SysDomainService;
import xc.investigation.base.dto.SysAdminDto;
import xc.investigation.base.query.SysQueryService;
import xc.investigation.base.utils.WebUtil;

/**
 * @author ibm
 */
@RestController
@RequestMapping("/admin/sys")
public class SysAdminRest {

    private final SysDomainService sysDomainService;
    private final SysQueryService sysQueryService;
    private final WebUtil webUtil;

    private static final String POST_MANAGER = "/manager";
    private static final String GET_MANAGER_LIST = "/manager/list";
    private static final String POST_MANAGER_LOGIN = "/manager/login";
    private static final String PUT_MANAGER_DISABLE = "/manager/disable/{id}";
    private static final String PUT_MANAGER_ENABLE = "/manager/enable/{id}";

    public SysAdminRest(SysDomainService sysDomainService, SysQueryService sysQueryService, WebUtil webUtil) {
        this.sysDomainService = sysDomainService;
        this.sysQueryService = sysQueryService;
        this.webUtil = webUtil;
    }

    @PostMapping(value = POST_MANAGER)
    public ResponseVo<String> adminCreate(@RequestBody AdminCreateVo adminCreateVo){
        sysDomainService.createAdmin(webUtil.getCurrentAdmin().getId(),adminCreateVo.getName(),
        adminCreateVo.getPwd(),adminCreateVo.getBankCode(),adminCreateVo.getRoleType());
        return ResponseVo.of(null,"创建成功");
    }

    @PostMapping(value = POST_MANAGER_LOGIN)
    public ResponseVo<SysAdminModel> adminLogin(@RequestBody AdminLoginVo adminLoginVo){
        SysAdminModel sysAdminModel = sysDomainService.login(adminLoginVo.getName(),adminLoginVo.getPwd());
        return ResponseVo.of(sysAdminModel);
    }

    @GetMapping(value = GET_MANAGER_LIST)
    public ResponseVo<Page<SysAdminDto>> managerList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                                    @RequestParam(value = "row",defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "bankCode",required = false)String bankCode,
                                                     @RequestParam(value = "name",required = false)String name){
        return ResponseVo.of(sysQueryService.findAdminPage(pageNo - 1,pageSize, webUtil.checkBankCode(bankCode),name));
    }

    @PutMapping(value = PUT_MANAGER_DISABLE)
    public ResponseVo<String> managerDisable(@PathVariable("id")Long id){
        sysDomainService.disable(id,webUtil.getCurrentAdmin().getId());
        return ResponseVo.of(null,"操作成功");
    }

    @PutMapping(value = PUT_MANAGER_ENABLE)
    public ResponseVo<String> managerEnable(@PathVariable("id")Long id){
        sysDomainService.enable(id,webUtil.getCurrentAdmin().getId());
        return ResponseVo.of(null,"操作成功");
    }


}
