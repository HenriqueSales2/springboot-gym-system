package br.com.application.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {

        var token = provider.resolveToken((HttpServletRequest) request);

        if (StringUtils.isNotBlank(token) && provider.validateToken(token)) {
            Authentication auth = provider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }
        }
        filter.doFilter(request, response);
    }
}