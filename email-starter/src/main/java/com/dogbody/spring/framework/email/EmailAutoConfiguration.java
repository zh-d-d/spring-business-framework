package com.dogbody.spring.framework.email;

import com.dogbody.spring.framework.email.support.EmailSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author zhangdd on 2024/4/29
 */

public class EmailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EmailSupport emailSupport() {
        return new EmailSupport();
    }

}
