package com.yy.my_tutor.user.controller;

import com.alibaba.fastjson.JSON;
import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.config.CustomException;
import com.yy.my_tutor.security.JwtTokenUtil;
import com.yy.my_tutor.user.domain.Parent;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import com.yy.my_tutor.user.service.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @Autowired
    private UserMapper userMapper;

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

    /**
     * 查询家长下的学生
     */
    @PostMapping("/findChild")
    public RespResult<List<User>> findChild(@RequestBody Parent parent) {
        List<User> users = parentService.findChild(parent);
        return RespResult.data(users);
    }

    /**
     * 查询未审批的老师（支持名称、电话、邮箱查询）
     * 需要管理员权限（type=9）
     */
    @PostMapping("/unapprovedTeachers")
    public RespResult<List<Parent>> findUnapprovedTeachers(@RequestBody Map<String, String> params, HttpServletRequest request) {
        // 校验管理员权限
        checkAdminPermission(request);
        
        log.info("查询未审批的老师: {}", JSON.toJSONString(params));
        String name = params.get("name");
        String tel = params.get("tel");
        String email = params.get("email");
        List<Parent> teachers = parentService.findUnapprovedTeachers(name, tel, email);
        // 隐藏敏感信息
        teachers.forEach(teacher -> teacher.setPassword(null));
        return RespResult.success(teachers);
    }

    /**
     * 审批通过老师
     * 需要管理员权限（type=9）
     */
    @PostMapping("/approveTeacher/{id}")
    public RespResult<Boolean> approveTeacher(@PathVariable Integer id, HttpServletRequest request) {
        // 校验管理员权限
        checkAdminPermission(request);
        
        log.info("审批通过老师: {}", id);
        boolean result = parentService.approveTeacher(id);
        if (result) {
            return RespResult.success("审批通过成功", true);
        }
        return RespResult.error("审批失败，老师不存在或已被审批");
    }

    /**
     * 查询已审批通过的老师列表（支持名称、电话、邮箱查询）
     * 需要管理员权限（type=9）
     */
    @PostMapping("/approvedTeachers")
    public RespResult<List<Parent>> findApprovedTeachers(@RequestBody Map<String, String> params, HttpServletRequest request) {
        // 校验管理员权限
        checkAdminPermission(request);
        
        log.info("查询已审批通过的老师: {}", JSON.toJSONString(params));
        String name = params.get("name");
        String tel = params.get("tel");
        String email = params.get("email");
        List<Parent> teachers = parentService.findApprovedTeachers(name, tel, email);
        // 隐藏敏感信息
        teachers.forEach(teacher -> teacher.setPassword(null));
        return RespResult.success(teachers);
    }

    /**
     * 校验当前登录用户是否为管理员（type=9）
     * @param request HTTP请求
     * @throws CustomException 如果不是管理员，抛出异常
     */
    private void checkAdminPermission(HttpServletRequest request) {
        // 从请求头获取token
        String token = parseToken(request);
        if (token == null || !JwtTokenUtil.validateToken(token)) {
            throw new CustomException("未登录或token无效");
        }

        // 从token中获取用户名
        String username = JwtTokenUtil.getUsernameFromToken(token);
        if (username == null) {
            throw new CustomException("无法获取用户信息");
        }

        // 查询用户信息（可能来自user表或parent表）
        User currentUser = userMapper.findByUserAccount(username);
        if (currentUser == null) {
            throw new CustomException("用户不存在");
        }

        // 检查是否为管理员（type=9）
        // 管理员在parent表中，type=9
        Integer userType = currentUser.getType();
        
        // 如果从user表查询的结果中type为null，且role为"P"（家长/老师），需要查询parent表
        if (userType == null && "P".equals(currentUser.getRole())) {
            List<Parent> parents = parentService.findByUserAccount(username);
            if (parents != null && !parents.isEmpty()) {
                userType = parents.get(0).getType();
            }
        }

        // 检查是否为管理员（type=9）
        if (userType == null || userType != 9) {
            throw new CustomException("无权限，仅管理员可操作");
        }
    }

    /**
     * 从请求头中解析token
     * @param request HTTP请求
     * @return token字符串
     */
    private String parseToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

} 