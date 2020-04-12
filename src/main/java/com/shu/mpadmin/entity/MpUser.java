package com.shu.mpadmin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "mp_user")
public class MpUser implements Serializable {

    @TableId(value = "id",type =IdType.AUTO)
    private Integer id;
    private String name;
    private String phone;
    private String password;

    @TableField(value="head_url")
    private String headUrl;

    private Integer age;
    private Integer gender;

    @TableField(value = "address")
    private String address;

    private String email;

    @TableField(value = "login_count")
    private Integer loginCount;

    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    @TableField(value = "is_admin")
    private Integer isAdmin;

    @TableField(value = "wx_openid")
    private String openId;

    @TableField(value = "wx_unionid")
    private String unionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public MpUser() {
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", loginCount=" + loginCount +
                ", lastLoginTime=" + lastLoginTime +
                ", isAdmin=" + isAdmin +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }

    //实体类转换为json
    public JSONObject convertToJson() throws JSONException, JsonProcessingException {
        Logger logger = LoggerFactory.getLogger(MpUser.class);

        JSONObject json=new JSONObject();
        if(getId()!=null){
            json.put("userId",getId());
        }else{
            json.put("userId","");
        }
        if(StringUtils.isNotEmpty(getName())){
            //获取昵称前。先解码
            logger.info("昵称解码前="+getName());
            String newname = EmojiParser.parseToUnicode(getName());
            logger.info("昵称解码后="+newname);
            json.put("name",newname);
        }else{
            json.put("name","");
        }
        if(StringUtils.isNotEmpty(getPassword())){
            json.put("password",getPassword());
        }else{
            json.put("password","");
        }
        if(StringUtils.isNotEmpty(getPhone())){
            json.put("phone",getPhone());
        }else{
            json.put("phone","");
        }
        if(StringUtils.isNotEmpty(getOpenId())){
            json.put("openId",getOpenId());
        }else{
            json.put("openId","");
        }
        if(StringUtils.isNotEmpty(getUnionId())){
            json.put("unionId",getUnionId());
        }else{
            json.put("unionId","");
        }
        if(StringUtils.isNotEmpty(getHeadUrl())){
            json.put("headUrl",getHeadUrl());
        }else{
            json.put("headUrl","");
        }
        if(StringUtils.isNotEmpty(getAddress())){
            json.put("address",getAddress());
        }else{
            json.put("address","");
        }
        if(StringUtils.isNotEmpty(getEmail())){
            json.put("email",getEmail());
        }else{
            json.put("email","");
        }
        if(getAge()!=null){
            json.put("age",getAge());
        }else{
            json.put("age","");
        }
        if(getGender()!=null){
            json.put("gender",getGender());
        }else{
            json.put("gender","");
        }
        if(getIsAdmin()!=null){
            json.put("isAdimin",getIsAdmin());
        }else{
            json.put("isAdimin","");
        }
        return json;
    }

}
