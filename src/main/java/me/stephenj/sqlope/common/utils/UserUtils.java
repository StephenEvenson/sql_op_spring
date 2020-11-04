package me.stephenj.sqlope.common.utils;

import me.stephenj.sqlope.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserUtils {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AdminService adminService;

    public String getUsernameByToken(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
        return jwtTokenUtil.getUserNameFromToken(authToken);
    }

    public int getIdByUsername(String username) {
        return adminService.getAdminByUsername(username).getId();
    }

    public int getIdByToken(HttpServletRequest request) {
        return getIdByUsername(getUsernameByToken(request));
    }
}
