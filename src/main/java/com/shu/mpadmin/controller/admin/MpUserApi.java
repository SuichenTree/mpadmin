package com.shu.mpadmin.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shu.mpadmin.controller.MpUserController;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.mapper.MpUserMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class MpUserApi {

    @Autowired
    private MpUserMapper userMapper;

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);


    @PostMapping("/shu/admin/login")
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
            json.put("password",one.getPassword());
            json.put("headURL",one.getHeadUrl());
        }else{
            json.put("isAdmin",0);
        }
        return json.toString();
    }

    @PostMapping("/shu/admin/updatePassword")
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

    @PostMapping("/shu/admin/getAllUser")
    public String getAllUser() throws JSONException {
        logger.info("后台用户列表接口,/shu/admin/getAllUser");
        JSONObject json=new JSONObject();
        Page<MpUser> page=new Page<>();
        page.setSize(10);
        IPage<MpUser> selectPage = userMapper.selectPage(page, null);

        List<MpUser> records = selectPage.getRecords();
        JSONArray jsonArray = new JSONArray();
        for (MpUser u:records){
            JSONObject json2=new JSONObject();
            json2.put("id",u.getId());
            json2.put("name",u.getName());
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




}
