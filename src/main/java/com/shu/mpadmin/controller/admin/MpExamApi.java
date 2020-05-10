package com.shu.mpadmin.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shu.mpadmin.controller.MpUserController;
import com.shu.mpadmin.entity.MpExam;
import com.shu.mpadmin.entity.MpQuestion;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.entity.MpUserExam;
import com.shu.mpadmin.mapper.MpExamMapper;
import com.shu.mpadmin.mapper.MpQuestionMapper;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(tags = "后台系统-测试Exam管理接口")
@RestController
@CrossOrigin
public class MpExamApi {

    @Autowired
    private MpExamMapper examMapper;
    @Autowired
    private MpQuestionMapper questionMapper;


    /*
     *   增加-接口类型：POST
     *   删除-接口类型：DELETE
     *   修改-接口类型：PUT
     *   查找-接口类型：GET
     * */

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    /**
     * 分页查询所有Exam测试
     * @param currentPage 查询当前页的数据
     * @param pageSize    每页包含多少条数据
     * @return
     * @throws JSONException
     */
    @ApiOperation("分页查询所有Exam测试")
    @GetMapping("/shu/admin/getAllExam")
    public String getAllExam(Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询所有Exam测试,/shu/admin/getAllExam, param: currentPage="+currentPage+", pageSize="+pageSize);
        JSONObject json=new JSONObject();
        Page<MpExam> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpExam> selectPage = examMapper.selectPage(page, null);
        List<MpExam> records = selectPage.getRecords();
        JSONArray jsonArray = new JSONArray();
        for (MpExam e:records){
            //根据examId 查询测试题库数量
            QueryWrapper<MpQuestion> query1 = new QueryWrapper<>();
            query1.eq("exam_id",e.getId());
            List<MpQuestion> qlist = questionMapper.selectList(query1);

            JSONObject json2=new JSONObject();
            json2.put("examId",e.getId());
            json2.put("examName",e.getName());
            json2.put("examType",e.getType());
            //转换时间格式
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simple.format(e.getCreateTime());
            json2.put("createTime",format);
            json2.put("examStatus",e.getStatus());
            json2.put("questionNumber",qlist.size());
            jsonArray.put(json2);
        }
        json.put("tableData",jsonArray);
        json.put("totalNumber",selectPage.getTotal());
        return json.toString();
    }

    @ApiOperation("新增Exam测试")
    @PostMapping("/shu/admin/exam")
    public String postExam(MpExam exam){
        logger.info("新增Exam测试/shu/admin/exam,参数 exam = "+exam.toString());
        JSONObject json=new JSONObject();
        //添加创建时间
        exam.setCreateTime(new Date());
        int a = examMapper.insert(exam);
        if (a==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("查询Exam测试")
    @GetMapping("/shu/admin/exam")
    public String getExam(Integer examId) throws JSONException {
        logger.info("查询Exam测试,/shu/admin/exam,参数 examId ="+examId);
        JSONObject json=new JSONObject();
        MpExam one = examMapper.selectById(examId);
        if(one!=null){
            json.put("isSuccess",1);
            json.put("examId",one.getId());
            json.put("examName",one.getName());
            json.put("examType",one.getType());
            json.put("examStatus",one.getStatus());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("更新Exam测试")
    @PutMapping("/shu/admin/exam")
    public String putExam(Integer examId,String examName,String examType,Integer examStatus){
        logger.info("/shu/admin/user,更新Exam测试。参数：examId="+examId+",examName="+examName+",examType="+examType+",examStatus="+examStatus);
        MpExam one = new MpExam();
        one.setId(examId);
        one.setCreateTime(new Date());
        one.setName(examName);
        one.setStatus(examStatus);
        one.setType(examType);
        int i = examMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("删除Exam测试")
    @DeleteMapping("/shu/admin/exam")
    public String deleteUser(Integer examId){
        logger.info("删除Exam测试/shu/admin/exam,examId = "+examId);
        JSONObject json = new JSONObject();
        int i = examMapper.deleteById(examId);
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("分页查询Question题库")
    @GetMapping("/shu/admin/getAllQuestion")
    public String getAllQuestion(Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询Exam测试下的Question题库,/shu/admin/getQuestionByExamId.param:currentPage="+currentPage+",pageSize="+pageSize);
        JSONObject json=new JSONObject();
        Page<MpQuestion> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpQuestion> selectPage = questionMapper.selectPage(page, null);
        if(selectPage!=null){
            //分页对象中的题库数据
            List<MpQuestion> records = selectPage.getRecords();
            JSONArray jsonArray = new JSONArray();
            for (MpQuestion e:records){
                JSONObject json2=new JSONObject();
                //根据examId查询exam数据
                Integer examId = e.getExamId();
                MpExam mpExam = examMapper.selectById(examId);

                json2.put("examId",mpExam.getId());
                json2.put("examName",mpExam.getName());
                json2.put("questionId",e.getId());
                json2.put("questionName",e.getName());
                json2.put("questionType",e.getType());
                //转换时间格式
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simple.format(e.getCreateTime());
                json2.put("createTime",format);
                json2.put("questionStatus",e.getStatus());
                jsonArray.put(json2);
            }
            json.put("isSuccess",1);
            json.put("tableData",jsonArray);
            json.put("totalNumber",selectPage.getTotal());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }



    /**
     * 分页查询Exam测试下的Question题库
     * @param examId
     * @param currentPage 查询当前页的数据
     * @param pageSize    每页包含多少条数据
     * @return
     * @throws JSONException
     */
    @ApiOperation("分页查询Exam测试下的Question题库")
    @GetMapping("/shu/admin/getQuestionByExamId")
    public String getQuestionByExamId(Integer examId,Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询Exam测试下的Question题库,/shu/admin/getQuestionByExamId.参数examId = "+examId+",currentPage="+currentPage+",pageSize="+pageSize);
        JSONObject json=new JSONObject();
        //查询exam信息
        MpExam mpExam = examMapper.selectById(examId);
        //分页查询题目信息
        QueryWrapper<MpQuestion> query=new QueryWrapper<>();
        query.eq("exam_id",examId);
        Page<MpQuestion> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpQuestion> selectPage = questionMapper.selectPage(page, query);
        if(selectPage!=null){
            //分页对象中的题库数据
            List<MpQuestion> records = selectPage.getRecords();
            JSONArray jsonArray = new JSONArray();
            for (MpQuestion e:records){
                JSONObject json2=new JSONObject();
                json2.put("examId",mpExam.getId());
                json2.put("examName",mpExam.getName());
                json2.put("questionId",e.getId());
                json2.put("questionName",e.getName());
                json2.put("questionType",e.getType());
                //转换时间格式
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simple.format(e.getCreateTime());
                json2.put("createTime",format);
                json2.put("questionStatus",e.getStatus());
                jsonArray.put(json2);
            }
            json.put("isSuccess",1);
            json.put("tableData",jsonArray);
            json.put("totalNumber",selectPage.getTotal());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("更新问题Question")
    @PutMapping("/shu/admin/question")
    public String putQuestion(Integer questionId,String questionName,String questionType,Integer questionStatus){
        logger.info("/shu/admin/question,更新问题Question。参数：questionId="+questionId+",questionName="+questionName+",questionType="+questionType+",questionStatus="+questionStatus);
        MpQuestion one = new MpQuestion();
        one.setId(questionId);
        one.setCreateTime(new Date());
        one.setName(questionName);
        one.setStatus(questionStatus);
        one.setType(questionType);
        int i = questionMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }


}
