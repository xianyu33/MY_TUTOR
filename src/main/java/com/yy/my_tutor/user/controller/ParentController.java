package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.service.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService parentService;

    /**
     * 新增家长
     */
    @PostMapping("/add")
    public RespResult<Boolean> addParent(@RequestBody Parent parent) {
        log.info("新增家长: {}", JSON.toJSONString(parent));
        boolean result = parentService.addParent(parent);
        if (result) {
            return RespResult.success("新增成功", true);
        }
        return RespResult.error("新增失败");
    }

    /**
     * 根据ID查询家长
     */
    @GetMapping("/{id}")
    public RespResult<Parent> findById(@PathVariable Integer id) {
        log.info("查询家长: {}", id);
        Parent parent = parentService.findById(id);
        if (parent != null) {
            return RespResult.success(parent);
        }
        return RespResult.error("家长不存在");
    }

    /**
     * 查询所有家长
     */
    @GetMapping("/list")
    public RespResult<List<Parent>> findAll() {
        log.info("查询所有家长");
        List<Parent> parents = parentService.findAll();
        return RespResult.success(parents);
    }

    /**
     * 更新家长
     */
    @PutMapping("/update")
    public RespResult<Boolean> updateParent(@RequestBody Parent parent) {
        log.info("更新家长: {}", JSON.toJSONString(parent));
        boolean result = parentService.updateParent(parent);
        if (result) {
            return RespResult.success("更新成功", true);
        }
        return RespResult.error("更新失败");
    }

    /**
     * 删除家长
     */
    @DeleteMapping("/{id}")
    public RespResult<Boolean> deleteById(@PathVariable Integer id) {
        log.info("删除家长: {}", id);
        boolean result = parentService.deleteById(id);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 根据账号查询家长
     */
    @GetMapping("/account/{userAccount}")
    public RespResult<List<Parent>> findByUserAccount(@PathVariable String userAccount) {
        log.info("查询家长账号: {}", userAccount);
        List<Parent> parents = parentService.findByUserAccount(userAccount);
        return RespResult.success(parents);
    }

    /**
     * 新增家长及其学生信息
     */
    @PostMapping("/addWithUsers")
    public RespResult<Boolean> addParentWithUsers(@RequestBody Map<String, Object> payload) {
        Parent parent = JSON.parseObject(JSON.toJSONString(payload.get("parent")), Parent.class);
        List<User> users = JSON.parseArray(JSON.toJSONString(payload.get("users")), User.class);
        boolean result = parentService.addParentWithUsers(parent, users);
        if (result) {
            return RespResult.success("新增家长和学生成功", true);
        }
        return RespResult.error("新增家长和学生失败");
    }


} 