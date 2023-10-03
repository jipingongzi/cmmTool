package xc.investigation.base.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author ibm
 */
public class PageUtil {

    public static <T> Page<T> pageData(List<T> content, Integer pageNo, Integer pageSize, Long totalCount){
        return new PageImpl<>(content, PageRequest.of(pageNo, pageSize), totalCount);
    }
}
