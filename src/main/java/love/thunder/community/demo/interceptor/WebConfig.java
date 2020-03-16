package love.thunder.community.demo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//下面这个注解加上之后可能会拦截css样式,我在添加了这个注释后发现jQuery被拦截了无法生效
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设定哪些地址需要被拦截，.excludePathPatterns("/admin/**")加在后面设定哪些地址不需要被拦截
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}