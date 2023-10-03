package xc.investigation.base.api.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.dto.BankDto;
import xc.investigation.base.query.BankQueryService;

import java.util.List;

/**
 * @author seanx
 */
@RestController
@RequestMapping("/user/bank")
@Slf4j
public class BankUserRest {

    private final static String GET_LIST = "/list";

    private final BankQueryService bankQueryService;

    public BankUserRest(BankQueryService bankQueryService) {
        this.bankQueryService = bankQueryService;
    }

    @GetMapping(value = GET_LIST)
    public ResponseVo<List<BankDto>> bankList(){
        return ResponseVo.of(bankQueryService
        .findBankPage(0,Integer.MAX_VALUE,null,null,null,null).getContent());
    }


}
