package com.frank.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frank.commonutils.R;
import com.frank.eduservice.entity.EduTeacher;
import com.frank.eduservice.entity.vo.TeacherQuery;
import com.frank.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "讲师管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacher")
@Slf4j
public class EduTeacherController {

    //service注入
    @Autowired
    private EduTeacherService teacherService;

    //1.查询讲师表所有数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    //2.逻辑删除讲师的方法
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        boolean remove = teacherService.removeById(id);
        return remove ? R.ok() : R.error();
    }

    //3.分页查询
    @ApiOperation(value = "分页查询")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current, @PathVariable long limit) {
        //创建Page对象
        Page<EduTeacher> teacherPage = new Page<>(current, limit);

        teacherService.page(teacherPage, null);
        List<EduTeacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    //4. 条件查询带分页
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> teacherPage = new Page<>(current, limit);

        //构造条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        System.out.println(teacherQuery);
        //判断条件是否为空，如果不为空进行拼接
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现查询
        teacherService.page(teacherPage, wrapper);

        long total = teacherPage.getTotal();
        List<EduTeacher> records = teacherPage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    //5. 新增讲师
    @ApiOperation(value = "新增讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        return save ? R.ok() : R.error();
    }

    //6. 根据讲师id进行查询
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    //7. 讲师修改功能
    @ApiOperation(value = "讲师修改功能")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean update = teacherService.updateById(eduTeacher);
        return update ? R.ok() : R.error();
    }

}

