package com.yy.my_tutor.user.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.user.domain.GuardianStudentRel;
import com.yy.my_tutor.user.service.GuardianStudentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guardian-rel")
@CrossOrigin(origins = "*")
public class GuardianRelationController {

    @Autowired
    private GuardianStudentRelService relService;

    @GetMapping("/guardian/{guardianId}/type/{guardianType}")
    public RespResult<List<GuardianStudentRel>> listByGuardian(@PathVariable Integer guardianId,
                                                               @PathVariable Integer guardianType) {
        return RespResult.success(relService.listByGuardian(guardianId, guardianType));
    }

    /**
     * 根据家长/老师ID和类型查询绑定的学生详细信息（POST请求）
     * @param guardianId 家长/老师ID
     * @param guardianType 类型：0-家长，1-老师
     * @return 学生详细信息列表
     */
    @PostMapping("/guardian/{guardianId}/type/{guardianType}")
    public RespResult<List<com.yy.my_tutor.user.domain.StudentDetailDTO>> getStudentsByGuardian(
            @PathVariable Integer guardianId,
            @PathVariable Integer guardianType) {
        return RespResult.success(relService.getStudentsWithDetailsByGuardian(guardianId, guardianType));
    }

    @GetMapping("/guardian/{guardianId}/type/{guardianType}/details")
    public RespResult<List<GuardianStudentRel>> listByGuardianWithDetails(@PathVariable Integer guardianId,
                                                                          @PathVariable Integer guardianType) {
        return RespResult.success(relService.listByGuardianWithDetails(guardianId, guardianType));
    }

    @GetMapping("/student/{studentId}")
    public RespResult<List<GuardianStudentRel>> listByStudent(@PathVariable Integer studentId) {
        return RespResult.success(relService.listByStudent(studentId));
    }

    @PostMapping("/bind")
    public RespResult<GuardianStudentRel> bind(@RequestParam Integer guardianId,
                                               @RequestParam Integer guardianType,
                                               @RequestParam Integer studentId,
                                               @RequestParam(required = false) String relation,
                                               @RequestParam(required = false, defaultValue = "system") String operator) {
        GuardianStudentRel rel = relService.bind(guardianId, guardianType, studentId, relation, operator);
        return RespResult.success("绑定成功", rel);
    }

    @PutMapping("/{id}/relation")
    public RespResult<GuardianStudentRel> updateRelation(@PathVariable Long id,
                                                         @RequestParam String relation,
                                                         @RequestParam(required = false, defaultValue = "system") String operator) {
        GuardianStudentRel rel = relService.updateRelation(id, relation, operator);
        if (rel != null) {
            return RespResult.success("更新成功", rel);
        }
        return RespResult.error("更新失败");
    }

    @GetMapping("/delete/{id}")
    public RespResult<String> unbind(@PathVariable Long id) {
        boolean ok = relService.unbind(id);
        return ok ? RespResult.success("解绑成功") : RespResult.error("解绑失败");
    }
}


