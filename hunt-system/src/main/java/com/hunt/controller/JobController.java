package com.hunt.controller;

import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysRoleOrganization;
import com.hunt.service.SysRoleOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import system.ResponseCode;
import system.Result;

/**
 * @Author ouyangan
 * @Date 2016/10/17/16:21
 * @Description
 */
@Controller
@RequestMapping("job")
public class JobController extends BaseController {
    @Autowired
    private SysRoleOrganizationService roleOrganizationService;

    @RequestMapping(value = "toJob", method = RequestMethod.GET)
    public String toJob() {
        return "system/job";
    }

    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result insert(@RequestParam long roleId,
                         @RequestParam long organizationId,
                         @RequestParam long parentId,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam String fullName,
                         @RequestParam(defaultValue = "1") int isFinal) {
        boolean isExistName = roleOrganizationService.isExistName(name, parentId);
        if (isExistName) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysRoleOrganization roleOrganization = new SysRoleOrganization();
        roleOrganization.setName(name);
        roleOrganization.setDescription(description);
        roleOrganization.setFullName(fullName);
        roleOrganization.setSysRoleId(roleId);
        roleOrganization.setSysOrganizationId(organizationId);
        roleOrganization.setParentId(parentId);
        roleOrganization.setIsFinal(isFinal);
        long id = roleOrganizationService.insertRoleOrganization(roleOrganization);
        return Result.success(id);
    }

    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestParam long id,
                         @RequestParam long roleId,
                         @RequestParam long organizationId,
                         @RequestParam long parentId,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam String fullName) {
        SysRoleOrganization roleOrganization = roleOrganizationService.selectRoleOrganizationById(id);
        if (roleOrganization == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (roleOrganization.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_delete.getMsg());
        }
        boolean isExistNameExcludeId = roleOrganizationService.isExistNameExcludeId(id, name, parentId);
        if (isExistNameExcludeId) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        roleOrganization.setName(name);
        roleOrganization.setDescription(description);
        roleOrganization.setFullName(fullName);
        roleOrganization.setSysRoleId(roleId);
        roleOrganization.setSysOrganizationId(organizationId);
        roleOrganization.setParentId(parentId);
        roleOrganizationService.updateRoleOrganization(roleOrganization);
        return Result.success();
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result delete(@RequestParam long id) {
        SysRoleOrganization roleOrganization = roleOrganizationService.selectRoleOrganizationById(id);
        if (roleOrganization == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (roleOrganization.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_delete.getMsg());
        }
        roleOrganization.setStatus(2);
        roleOrganizationService.updateRoleOrganization(roleOrganization);
        return Result.success();
    }

    @ResponseBody
    @RequestMapping(value = "select", method = RequestMethod.GET)
    public Result select(@RequestParam int page,
                         @RequestParam int rows,
                         @RequestParam(defaultValue = "1") long id) {
        PageInfo pageInfo = roleOrganizationService.selectPage(page, rows, id);
        return Result.success(pageInfo);
    }

}
