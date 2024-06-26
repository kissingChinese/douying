package com.example.authority;

import com.example.util.JwtUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component(value = "postMappingAuthorityVerify")
public class UnifyAuthorityVerify extends DefaultAuthorityVerify{
    @Override
    public Boolean authorityVerify(HttpServletRequest request,String... permissions) {

        Long uId = JwtUtils.getUserId(request);
        for (String permission : permissions) {
            if (!AuthorityUtils.verify(uId,permission)) {
                return false;
            }
        }
        return true;
    }
}
