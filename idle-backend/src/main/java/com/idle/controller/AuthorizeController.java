package com.idle.controller;

import com.idle.entity.RestBean;
import com.idle.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9一-龥]+$";
    @Resource
    AuthorizeService service;

    @PostMapping("/valid-register-email")
    public RestBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEX)@RequestParam("email") String email,
                                          HttpSession session) {
        String s = service.sendValidateEmail(email, session.getId(), false);
        if( s == null)
            return RestBean.success("邮件已发送，请注意查收");
        else
            return RestBean.failure(400,s);
    }
    @PostMapping("/valid-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEX)@RequestParam("email") String email,
                                          HttpSession session) {
        String s = service.sendValidateEmail(email, session.getId(), true);
        if( s == null)
            return RestBean.success("邮件已发送，请注意查收");
        else
            return RestBean.failure(400,s);
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX)
                                         @Length(min = 2, max = 8) @RequestParam("username") String username,
                                         @Length(min = 6, max = 16) @RequestParam("password") String password,
                                         @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                         @Length(min = 6, max = 6) @RequestParam("code") String code,
                                         HttpSession session){
        String s = service.validateAndRegister(username, password, email, code, session.getId());
        if(s == null)
            return RestBean.success("注册成功");
        else
            return RestBean.failure(400,s);
    }
    /**
     * 1.发送验证邮件
     * 2.验证验证码是否正确，正确就在Session中保存一个标记
     * 3.用户发起重置密码请求，如果存在标记，就成功重置密码
     */
    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session){
        String s = service.validateOnly(email, code, session.getId());
        if (s == null) {
            session.setAttribute("reset-password", email);
            return RestBean.success();
        } else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                          HttpSession session){
      String email = (String) session.getAttribute("reset-password");
      if(email == null) return RestBean.failure(401,"请先完成邮箱验证！");
      else if(service.resetPassword(password, email)){
          session.removeAttribute("reset-password");
          return RestBean.success("密码重置成功！");
      } else {
          return RestBean.failure(500,"内部错误，请联系管理员！");
      }
    }

    @PostMapping("/upgrade-info")
    public RestBean<String> upgradeInfo(String nickname,String email, String address, String username){
        boolean Info = service.upgradeInfo(nickname,email, address, username);
        if(Info) return RestBean.success("信息修改成功");
        else return RestBean.failure(500,"内部错误，请联系管理员");
    }

    @PostMapping("/upgrade-avatar")
    public RestBean<String> upgradeAvatar(String avatar, Long id){
        boolean head = service.upgradeAvatar(avatar, id);
        if(head) return RestBean.success("头像修改成功，请刷新页面查看");
        else return RestBean.failure(500,"内部错误，请联系管理员");
    }

    @PostMapping("/upgrade-address")
    public RestBean<String> upgradeAddress(String address, Long id){
        boolean location = service.upgradeAddress(address, id);
        if(location) return RestBean.success(address);
        else return RestBean.failure(500,"内部错误，请联系管理员");
    }
}
