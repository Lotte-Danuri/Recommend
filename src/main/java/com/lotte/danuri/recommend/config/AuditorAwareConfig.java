package com.lotte.danuri.recommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
public class AuditorAwareConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        HttpServletRequest request = null;
        long memberId = -1;

        if ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes() != null){
            request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            memberId = Long.parseLong(
                    Optional
                            .ofNullable(request.getHeader(("memberId")))
                            .orElse("-1"));
        }
        
        return Optional.ofNullable(memberId);
    }
}