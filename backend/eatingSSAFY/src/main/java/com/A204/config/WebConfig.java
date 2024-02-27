package com.A204.config;

import com.A204.dispatchFilter.AccessTokenFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new AccessTokenFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/api/nocard-person");
        filterFilterRegistrationBean.addUrlPatterns("/api/preference/*");
        filterFilterRegistrationBean.addUrlPatterns("/api/daily-visit");
        filterFilterRegistrationBean.addUrlPatterns("/api/user");
        return filterFilterRegistrationBean;
    }
}
