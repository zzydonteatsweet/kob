package com.kob.backend.service.impl.user.account;

import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.kob.backend.pojo.User ;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements com.kob.backend.service.user.account.LoginService {
    @Autowired
    private AuthenticationManager authenticationManager ;

    @Override
    public Map<String, String> getToken(String username, String password) throws Exception {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(username, password);
        System.out.println("Second" + username + " " + password);
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            System.out.println(username + " " + password);
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

            System.out.println("Impl here");
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
