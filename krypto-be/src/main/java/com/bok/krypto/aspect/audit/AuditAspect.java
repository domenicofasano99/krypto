package com.bok.krypto.aspect.audit;

import com.bok.krypto.helper.AuditHelper;
import com.bok.parent.integration.dto.AccountLoginDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    AuditHelper auditHelper;

    @Before("@annotation(com.bok.krypto.aspect.audit.Audited)")
    public void persistMessage(JoinPoint joinPoint) {


        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountLoginDTO login = (AccountLoginDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountLoginDTO).findFirst().get();
        //auditHelper.auditLoginRequest(req.getRemoteAddr(), login.email);
    }
}
