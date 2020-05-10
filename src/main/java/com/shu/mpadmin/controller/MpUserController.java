package com.shu.mpadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.shu.mpadmin.service.MpUserService;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "微信小程序-User用户接口")
@RestController
public class MpUserController {

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    @Autowired
    private MpUserMapper mapper;
    @Autowired
    private MpUserService userService;
    @Autowired
    private MpCommonController commonController;



    /**
     * 电话注册接口
     * @param phone
     * @param password
     * @return
     * @throws Exception
     */
    @ApiOperation("电话注册接口")
    @PostMapping("/shu/user/register")
    public String register(String phone,String password) throws Exception {
        logger.info("注册接口，参数： password ="+password+", phone = "+phone);
        JSONObject jsonObject = new JSONObject();
        MpUser entity = new MpUser();
        entity.setPassword(password);
        entity.setPhone(phone);
        int insert = mapper.insert(entity);
        //若返回0表示注册失败，否则注册成功
        if(insert==1){
            logger.info("注册成功");
        }else{
            logger.info("注册失败");
        }
        jsonObject.put("isRegister",insert);
        return jsonObject.toString();
    }

    /**
     * 电话登录接口
     * @param phone
     * @param password
     * @return
     * @throws JSONException
     */
    @ApiOperation("电话登录接口")
    @PostMapping("/shu/user/login")
    public String login(String phone,String password) throws JSONException {
        logger.info("登录接口，参数：phone="+phone+", pass="+password);
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<MpUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("password",password);
        MpUser entity = mapper.selectOne(queryWrapper);
        if(entity!=null){
            logger.info("登录成功");
            jsonObject.put("userId",entity.getId());
            jsonObject.put("isLogin",1);
        }else{
            logger.info("登录失败");
            jsonObject.put("userId",0);
            jsonObject.put("isLogin",0);
        }
        return jsonObject.toString();
    }

    /**
     * 电话登录-重置密码接口
     * @param phone
     * @param password
     * @param passNewpassword
     * @return
     * @throws JSONException
     */
    @ApiOperation("电话登录-重置密码接口")
    @PostMapping("/shu/user/resetPass")
    public String resetPass(String phone,String password,String passNewpassword) throws JSONException {
        logger.info("重置密码接口，参数：phone ="+phone+", password ="+password+", passNewword ="+passNewpassword);
        JSONObject json=new JSONObject();
        QueryWrapper<MpUser> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        queryWrapper.eq("password",password);
        MpUser entity = mapper.selectOne(queryWrapper);
        if(entity!=null){
            entity.setPassword(passNewpassword);
            int i = mapper.updateById(entity);
            if(i==1){
                logger.info("重置密码成功");
                json.put("isReset",1);
            }else{
                logger.info("重置密码失败1");
                json.put("isReset",0);
            }
        }else{
            logger.info("重置密码失败2");
            json.put("isReset",0);
        }
        return json.toString();
    }

    /**
     * 微信登录用户接口
     * 1.传入code，得到用户的openId和unionId
     * 2.注册或登录用户
     */
    @ApiOperation("微信登录用户-初始化信息接口")
    @PostMapping("/shu/user/initUserInfo")
    public String initUserInfoByWX(String code,String head,String name,Integer gender,String address) throws Exception {
        logger.info("/shu/user/initUserInfo,初始化用户接口,参数 code="+code+",head = "+head+",name="+name+",gender="+gender+",address="+address);
        //对用于微信昵称中的特殊符号进行编码
        String newname = EmojiParser.parseToAliases(name);
        String openId = null;
        String unionId = null;
        //根据code获取用户openid和unionid
        JSONObject info = commonController.getOpenIdByWX(code);
        if(!info.isNull("openid")){
            openId = info.getString("openid");
        }
        if(!info.isNull("unionid")){
            unionId = info.getString("unionid");
        }
        //注册或登录用户
        JSONObject jsonObject = new JSONObject();
        MpUser user = null;
        user = userService.findByopenId(openId);
        if(user!=null){
            logger.info("用户登录数据库,更新用户数据");
            user.setLastLoginTime(new Date());
            user.setLoginCount(user.getLoginCount()+1);
            if(StringUtils.isNotEmpty(head)){
                user.setHeadUrl(head);
            }
            if(StringUtils.isNotEmpty(newname)){
                user.setName(newname);
            }
            if(gender!=null){
                user.setGender(gender);
            }
            if(StringUtils.isNotEmpty(address)){
                user.setAddress(address);
            }
            UpdateWrapper<MpUser> update = new UpdateWrapper<>();
            userService.updateById(user);
            jsonObject.put("userId",user.getId());
            //返回用户唯一标识给前端
            return jsonObject.toString();
        }else{
            logger.info("用户注册数据库，创建用户信息数据");
            user = new MpUser();
            user.setLoginCount(1);
            user.setLastLoginTime(new Date());
            user.setOpenId(openId);
            user.setUnionId(unionId);
            if(StringUtils.isNotEmpty(head)){
                user.setHeadUrl(head);
            }
            if(StringUtils.isNotEmpty(newname)){
                user.setName(newname);
            }
            if(gender!=null){
                user.setGender(gender);
            }
            if(StringUtils.isNotEmpty(address)){
                user.setAddress(address);
            }
            boolean save = userService.save(user);
            if(save){
                jsonObject.put("userId",user.getId());
            }
            return jsonObject.toString();
        }
    }

    /**
     * 保存用户个人信息
     * **/
    @ApiOperation("保存用户个人信息接口")
    @PostMapping("/shu/user/saveUserInfo")
    public String saveUserINFO(Integer userId,String head,String name,Integer gender,Integer age,String address,String phone,String email) throws JSONException {
        logger.info("/shu/user/saveUserInfo，保存用户个人信息接口 ，参数：head="+head+", name="+name+",gender="+gender+",age="+age+",address="+address+",phone="+phone+",email="+email);
        JSONObject json = new JSONObject();
        MpUser user=new MpUser();
        if(userId!=null){
            user.setId(userId);
        }
        if(StringUtils.isNotEmpty(head)){
            user.setHeadUrl(head);
        }
        if(StringUtils.isNotEmpty(name)){
            //对用于微信昵称中的特殊符号进行编码
            String newname = EmojiParser.parseToAliases(name);
            user.setName(newname);
        }
        if(gender!=null){
            user.setGender(gender);
        }
        if(age!=null){
            user.setAge(age);
        }
        if(StringUtils.isNotEmpty(address)){
            user.setAddress(address);
        }
        if(StringUtils.isNotEmpty(phone)){
            user.setPhone(phone);
        }
        if(StringUtils.isNotEmpty(email)){
            user.setEmail(email);
        }
        userService.saveOrUpdate(user);
        json.put("code",200);
        return json.toString();
    }

    /**
     * 获取用户个人信息接口
     * @param userId
     */
    @ApiOperation("获取用户个人信息接口")
    @PostMapping("/shu/user/getUserInfo")
    public String getUserINFO(Integer userId) throws JSONException, JsonProcessingException {
        logger.info("获取用户个人信息接口，/shu/user/getUserInfo ，参数：userId = "+userId);
        JSONObject json = new JSONObject();
        MpUser user=null;
        user=userService.getById(userId);
        if(user!=null){
            json.put("user_info",user.convertToJson());
        }else{
            json.put("user_info","");
        }
        logger.info("成功获取用户个人信息 json ="+json);
        return json.toString();
    }



}
