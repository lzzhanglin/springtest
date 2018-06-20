package com.mmall.service.impl;

import com.google.zxing.oned.UPCAReader;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service("iUserService")
public class UserSeviceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        // todo MD5 use password login
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessMsg("登录成功",user);
    }
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNoneBlank(type)) {
            if (Objects.equals(Const.USERNAME,type)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已被注册！");
                }

            }
            if (Objects.equals(Const.EMAIL,type)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已被注册！");
                }

            }
        }else {
            return ServerResponse.createByErrorMsg("参数错误！");
        }
        return ServerResponse.createBySuccessMsg("校验成功！");
    }

    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //username already exists
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("未查询到找回密码的问题！");
    }


    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
//        System.out.println("the resultCount is: "+resultCount);
        return ServerResponse.createByErrorMsg("问题答案错误！");
    }

    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
//        System.out.println("forgetToken is: "+forgetToken);
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("参数错误,token未传递！");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //username already exists
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
//        System.out.println("token is: "+token);

        if (StringUtils.isBlank(token)) {
           return ServerResponse.createByErrorMsg("token无效或者已过期！");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (resultCount > 0) {
              return  ServerResponse.createBySuccessMsg("修改密码成功！");
            }
        }else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取重置密码的token！");

        }
        return ServerResponse.createByErrorMsg("修改密码失败！");



    }

    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("密码错误！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("密码更新成功！");

        }
        return ServerResponse.createByErrorMsg("密码更新失败！");
    }

    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMsg("email已被注册！请更换email");

        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("个人信息更新成功！",updateUser);
        }
        return ServerResponse.createByErrorMsg("个人信息更新失败！");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("找不到当前用户！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backend
    //校验是否是管理员用户
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccessStatus();
        }
        return ServerResponse.createByError();
    }
}
