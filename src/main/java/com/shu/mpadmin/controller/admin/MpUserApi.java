package com.shu.mpadmin.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shu.mpadmin.controller.MpUserController;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Api(tags = "后台系统-用户管理接口")
@RestController
@CrossOrigin
public class MpUserApi {
    @Autowired
    private MpUserMapper userMapper;

    /*
    *   增加-接口类型：POST
    *   删除-接口类型：DELETE
    *   修改-接口类型：PUT
    *   查找-接口类型：GET
    * */

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", defaultValue = "admin"),
            @ApiImplicitParam(name = "passWord", value = "密码", defaultValue = "123456")
    })
    @GetMapping("/shu/admin/login")
    public String Login(String userName,String passWord) throws JSONException {
        logger.info("后台登录接口,/shu/admin/login,参数：userName = "+userName+", passWord="+passWord);
        JSONObject json=new JSONObject();

        QueryWrapper<MpUser> query1=new QueryWrapper<>();
        query1.eq("name",userName).eq("password",passWord);
        MpUser one = userMapper.selectOne(query1);
        if(one!=null&&one.getIsAdmin()==1){
            json.put("isAdmin",1);
            json.put("userId",one.getId());
            json.put("userName",one.getName());
            json.put("passWord",one.getPassword());
            json.put("headUrl",one.getHeadUrl());
            if(one.getGender()==null) {
                json.put("gender", 0);
            }else if(one.getGender()==1){
                json.put("gender",1);
            }else if(one.getGender()==2){
                json.put("gender",2);
            }
            json.put("age",one.getAge());
            json.put("phone",one.getPhone());
            json.put("email",one.getEmail());
            json.put("address",one.getAddress());
        }else{
            json.put("isAdmin",0);
        }
        return json.toString();
    }

    @ApiOperation("用户修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", defaultValue = "123456")
    })
    @PutMapping("/shu/admin/updatePassword")
    public String UpdatePassword(String userName,String password,String newPassword) throws JSONException {
        logger.info("后台修改密码接口,/shu/admin/updatePassword,参数：userName = "+userName+", password="+password+", newPassword="+newPassword);
        JSONObject json=new JSONObject();
        QueryWrapper<MpUser> query1=new QueryWrapper<>();
        query1.eq("name",userName).eq("password",password);
        MpUser one = userMapper.selectOne(query1);
        if(one!=null){
            one.setPassword(newPassword);
            int i = userMapper.updateById(one);
            if(i==1){
                json.put("isSuccess",1);
            }else{
                json.put("isSuccess",0);
            }
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    /**
     * 分页查询所有用户信息
     * @param currentPage 查询当前页的数据
     * @param pageSize    每页包含多少条数据
     * @return
     * @throws JSONException
     */
    @ApiOperation("分页查询所有用户信息")
    @GetMapping("/shu/admin/getAllUser")
    public String getAllUser(Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询所有用户信息,/shu/admin/getAllUser，param: currentPage="+currentPage+", pageSize="+pageSize);
        JSONObject json=new JSONObject();
        Page<MpUser> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpUser> selectPage = userMapper.selectPage(page, null);
        List<MpUser> records = selectPage.getRecords();
        JSONArray jsonArray = new JSONArray();
        for (MpUser u:records){
            //对用户昵称中的emoj表情进行转码
            String s_name = EmojiParser.parseToUnicode(u.getName());

            JSONObject json2=new JSONObject();
            json2.put("id",u.getId());
            json2.put("name",s_name);
            json2.put("gender",u.getGender());
            if(u.getGender()==null) {
                json2.put("gender", "未知");
            }else if(u.getGender()==1){
                json2.put("gender","男");
            }else if(u.getGender()==2){
                json2.put("gender","女");
            }else if(u.getGender() == 0){
                json2.put("gender","未知");
            }
            json2.put("age",u.getAge());
            json2.put("phone",u.getPhone());
            json2.put("email",u.getEmail());
            json2.put("address",u.getAddress());
            json2.put("headUrl",u.getHeadUrl());

            jsonArray.put(json2);
        }
        json.put("tableData",jsonArray);
        json.put("totalNumber",selectPage.getTotal());
        return json.toString();
    }

    @ApiOperation("修改用户个人信息")
    @PutMapping("/shu/admin/updateUserInfo")
    public String updateUserInfo(Integer userId,String headUrl,String userName,String passWord,
                                 String phone,Integer age,Integer gender,String address,String email){
        logger.info("/shu/admin/updateUserInfo,修改用户个人信息接口。参数：userId="+userId+",headUrl="+headUrl+",userName="+userName+",passWord="+passWord
        +",phone="+phone+",age="+age+",gender="+gender+",address="+address+",email="+email);
        //将emoji符号转换为字符
        String s_name = EmojiParser.parseToAliases(userName);
        MpUser one = new MpUser();
        one.setId(userId);
        one.setName(s_name);
        one.setAddress(address);
        one.setGender(gender);
        one.setHeadUrl(headUrl);
        one.setPhone(phone);
        one.setPassword(passWord);
        one.setAge(age);
        one.setEmail(email);
        int i = userMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("获取单个用户信息")
    @GetMapping("/shu/admin/user")
    public String getUser(Integer userId){
        logger.info("获取单个用户信息,userId = "+userId);
        JSONObject json = new JSONObject();
        MpUser one = userMapper.selectById(userId);
        if(one!=null){
            json.put("isSuccess",1);
            json.put("headUrl",one.getHeadUrl());
            json.put("userId",one.getId());
            json.put("passWord",one.getPassword());
            //对用户昵称中的emoj表情进行转码
            String s_name = EmojiParser.parseToUnicode(one.getName());
            json.put("userName",s_name);
            json.put("gender",one.getGender());
            json.put("isAdmin",one.getIsAdmin());
            json.put("age",one.getAge());
            json.put("phone",one.getPhone());
            json.put("email",one.getEmail());
            json.put("address",one.getAddress());
            return json.toString();
        }else{
            json.put("isSuccess",0);
            return json.toString();
        }
    }

    @ApiOperation("删除单个用户")
    @DeleteMapping("/shu/admin/user")
    public String deleteUser(Integer userId){
        logger.info("删除单个用户/shu/admin/user,userId = "+userId);
        JSONObject json = new JSONObject();
        int i = userMapper.deleteById(userId);
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("/shu/admin/deleteUsers")
    public String deleteUsers(String userIds){
        logger.info("批量删除用户/shu/admin/deleteUsers,userIds = "+userIds);
        JSONObject json = new JSONObject(userIds);
        List<Integer> userIdList = new ArrayList<>();
        Iterator iterator = json.keys();
        String key = null;
        while(iterator.hasNext()){
            key = (String) iterator.next();
            userIdList.add(json.getInt(key));
        }
        JSONObject jsonObject =new JSONObject();
        try{
            for(Integer i:userIdList){
                userMapper.deleteById(i);
            }
            jsonObject.put("isSuccess",1);
        }catch (Exception e){
            jsonObject.put("isSuccess",0);
        }
        return jsonObject.toString();
    }

    @ApiOperation("新增单个用户")
    @PostMapping("/shu/admin/user")
    public String postUser(MpUser user){
       logger.info("新增单个用户/shu/admin/user,参数 user = "+user.toString());
       JSONObject json=new JSONObject();
        int a = userMapper.insert(user);
        if (a==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("更新单个用户")
    @PutMapping("/shu/admin/user")
    public String putUser(Integer userId,String headUrl,String userName,String passWord,
                          String phone,Integer age,Integer gender,String address,String email,Integer isAdmin){
        logger.info("/shu/admin/user,更新单个用户。参数：userId="+userId+",headUrl="+headUrl+",userName="+userName+",passWord="+passWord
                +",phone="+phone+",age="+age+",gender="+gender+",address="+address+",email="+email);
        //将emoji符号转换为字符
        String s_name = EmojiParser.parseToAliases(userName);
        MpUser one = new MpUser();
        one.setId(userId);
        one.setName(s_name);
        one.setAddress(address);
        one.setGender(gender);
        one.setHeadUrl(headUrl);
        one.setPhone(phone);
        one.setPassword(passWord);
        one.setAge(age);
        one.setEmail(email);
        one.setIsAdmin(isAdmin);
        int i = userMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }



}
