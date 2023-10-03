package xc.investigation.base.config.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import xc.investigation.base.config.mvc.interceptor.XcApiRequestInterceptor;
import xc.investigation.base.config.mvc.interceptor.XcTokenHolderInterceptor;
import xc.investigation.base.config.mvc.mapper.JacksonMapper;

import java.util.List;

/**
 * @author ibm
 */
@Configuration
public class XcWebConfig extends WebMvcConfigurationSupport {

    private final XcApiRequestInterceptor apiRequestInterceptor;
    private final XcTokenHolderInterceptor tokenHolderInterceptor;

    public XcWebConfig(XcApiRequestInterceptor apiRequestInterceptor, XcTokenHolderInterceptor tokenHolderInterceptor) {
        this.apiRequestInterceptor = apiRequestInterceptor;
        this.tokenHolderInterceptor = tokenHolderInterceptor;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.apiRequestInterceptor).addPathPatterns("/**");
        registry.addInterceptor(this.tokenHolderInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //重写这个方法，映射静态资源文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
        ;
        super.addResourceHandlers(registry);
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {


        return new MappingJackson2HttpMessageConverter(new JacksonMapper());
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter<?> messageConverter = converters.get(i);
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(i);
            }
        }
        converters.add(getMappingJackson2HttpMessageConverter());
    }
}
