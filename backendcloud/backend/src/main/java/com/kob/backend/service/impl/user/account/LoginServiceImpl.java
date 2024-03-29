package com.kob.backend.service.impl.user.account;

import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
//import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.kob.backend.pojo.User ;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager ;

    @Override
    public Map<String, String> getToken(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<>();
        //  实体化类
        UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(username, password);
        try {
            //  验证
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

            User user = loginUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());
            map.put("error_message", "success");
            map.put("token", jwt);
            return map;
        } catch (Exception e) {
            map.put("error_message", "not found") ;
            return  map ;
        }
    }
}
