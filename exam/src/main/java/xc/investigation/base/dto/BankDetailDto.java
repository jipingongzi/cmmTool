package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalDouble;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankDetailDto {

    private BankDto bankDto;

    private Integer examPaperInstanceCount;

    private Integer examPaperInstanceSumPoint;

    private BigDecimal examPaperInstanceAvePoint;

    private List<ExamPaperInstanceListDto> examPaperInstanceListDtoList;

    public BankDetailDto(BankDto bankDto, List<ExamPaperInstanceListDto> examPaperInstanceListDtoList) {
        this.bankDto = bankDto;
        this.examPaperInstanceListDtoList = examPaperInstanceListDtoList;
        if(!CollectionUtils.isEmpty(examPaperInstanceListDtoList)) {
            this.examPaperInstanceCount = examPaperInstanceListDtoList.size();
            this.examPaperInstanceSumPoint = examPaperInstanceListDtoList.stream().mapToInt(ExamPaperInstanceListDto::getPoint).sum();
            OptionalDouble avePointOptional = examPaperInstanceListDtoList.stream()
                    .mapToInt(ExamPaperInstanceListDto::getPoint)
                    .average();
            if(avePointOptional.isPresent()){
                BigDecimal avePoint = BigDecimal.valueOf(avePointOptional.getAsDouble());
                this.examPaperInstanceAvePoint =  avePoint.setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        }
    }
}
