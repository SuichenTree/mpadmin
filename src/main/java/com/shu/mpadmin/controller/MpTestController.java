package com.shu.mpadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.shu.mpadmin.service.MpUserService;
import org.apache.catalina.User;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MpTestController {

    @Autowired
    private MpUserMapper userMapper;

    @Autowired
    private MpUserService service;

    @RequestMapping(value = "/shu/test/save")
    public void test1(){
        MpUser entity=new MpUser();
        entity.setName("小明");
        entity.setPhone("13256987452");
        int insert = userMapper.insert(entity);
        // insert 是指对表的行数影响记录
        System.out.println("insert ="+insert);
        // 直接对实体对象id属性打印，就能得到该对象对应的主键值
        System.out.println("插入记录的主键值 id = "+entity.getId());
    }

    @RequestMapping(value = "/shu/test/update")
    public void testUpdate(){
        UpdateWrapper<MpUser> updateWrapper = new UpdateWrapper();
        updateWrapper.set("password","aaaaaa").set("phone","110").eq("name","小明");
        int update = userMapper.update(null, updateWrapper);
        System.out.println("update = > "+update);
    }


    /**
     * 将删除条件保存在map中，根据多条件来删除数据
     */
    @RequestMapping("/shu/test/deleteByMap")
    public void testdeleteByMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("phone","13563569856");
        map.put("password","321654");
        int i = userMapper.deleteByMap(map);
        System.out.println("i = "+i);
    }


    /**
     * 分页查询
     */
    @RequestMapping("/shu/test/selectPage")
    public void testSelectPage() throws JSONException, JsonProcessingException {
        Page<MpUser> page = new Page<>();
        //current 当前页,size 每页显示条数
        page.setCurrent(1);
        page.setSize(2);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.gt("id",1);
        IPage iPage = userMapper.selectPage(page, queryWrapper);

        List<MpUser> records = iPage.getRecords();
        System.out.println("当前页查询记录 records = "+records);
        for(MpUser u:records){
            System.out.println(u.convertToJson().toString());
        }
        System.out.println("一共查询的数据数 total = "+iPage.getTotal());
        System.out.println("当前页数是第 "+iPage.getCurrent()+" 页");
        System.out.println("一共的页数为 = "+iPage.getPages());
    }

    /**
     * 测试and，or
     */
    @RequestMapping("/shu/test/andOr")
    public void testAndOr(){
        QueryWrapper<MpUser> queryWrapper = new QueryWrapper<>();
        //FROM mp_user WHERE (phone = ? OR password = ?)
        queryWrapper.eq("phone","18271801652").or().eq("password","aaaaaa");
        List<MpUser> list = userMapper.selectList(queryWrapper);
        for(MpUser u:list){
            System.out.println(u.toString());
        }
    }
}
