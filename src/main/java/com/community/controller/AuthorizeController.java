package com.community.controller;

import com.community.dto.AccessTokenDTO;
import com.community.dto.GitHubUser;
import com.community.mapper.UserMapper;
import com.community.model.User;
import com.community.provider.GitHubProvider;
import com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientsecret;
    @Value("${github.client.uri}")
    private String uri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientsecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.gitUser(accessToken);

        if (gitHubUser != null) {
            request.getSession().setAttribute("user", gitHubUser);
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));

            userService.createOrUpdate(user);
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
            //登录成功，写cookie和session
        } else {
            //登陆失败
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
//删除session
        request.getSession().removeAttribute("user");
//删除cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
