package me.stephenj.sqlope.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.common.utils.LogGenerator;
import me.stephenj.sqlope.domain.TableParam;
import me.stephenj.sqlope.mbg.model.Table;
import me.stephenj.sqlope.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName TableController.java
 * @Description
 * @author 张润天
 * @Time 2020/11/3 00:58
 * @Field :
 */
@Api(tags = "TableController", description = "数据表管理")
@Controller
@RequestMapping("/tableope")
public class TableController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableService tableService;
    @Autowired
    private LogGenerator logGenerator;
    @ApiOperation("获取所有可用的数据表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Table>> getTbList(HttpServletRequest request) {
        logGenerator.log(request, "获取所有数据表");
        return CommonResult.success(tableService.listTables());
    }

    @ApiOperation("创建数据表")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createTb(@RequestBody TableParam tableParam, HttpServletRequest request) {
        int count;
        try {
            count = tableService.createTable(tableParam, request);
        } catch (TableExistException | ParameterLackException | FieldNotExistException | TableNotExistException e) {
            LOGGER.debug("create table failed:{}", tableParam.getName());
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "创建数据表: " + tableParam.getName());
            LOGGER.debug("create table success:{}", tableParam.getName());
            return CommonResult.success(tableParam.getName());
        } else {
            LOGGER.debug("create table failed:{}", tableParam.getName());
            return CommonResult.failed("建表失败，其他原因");
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation("删除数据表")
    @RequestMapping(value = "/drop", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult dropTable(@RequestParam(value = "tableId")
                                  @ApiParam("数据表序号") int tableId,
                                  HttpServletRequest request) {
        int count;
        try {
            count = tableService.dropTable(tableId);
        } catch (TableNotExistException | ForeignKeyExistException e) {
            LOGGER.debug("drop table failed:{}", tableId);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "删除数据表: " + tableId);
            LOGGER.debug("drop table success:{}", tableId);
            return CommonResult.success(tableId);
        } else {
            LOGGER.debug("drop table failed:{}", tableId);
            return CommonResult.failed("删表失败，其他原因");
        }
    }

//    @ApiOperation("重命名数据表")
//    @RequestMapping(value = "/rename", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult renameTable(@RequestParam(value = "tableId")
//                                    @ApiParam("数据表序号") int tableId,
//                                    @RequestParam(value = "newName")
//                                    @ApiParam("数据表新名") String newName,
//                                    HttpServletRequest request) {
//        int count;
//        try {
//            count = tableService.renameTable(tableId, newName);
//        } catch (TableNotExistException | TableExistException e) {
//            LOGGER.debug("rename table failed:{}", tableId);
//            return CommonResult.failed(e.getMessage());
//        }
//        if (count == 1) {
//            logGenerator.log(request, "重命名数据表: " + tableId);
//            LOGGER.debug("rename table success:{}", tableId);
//            return CommonResult.success(tableId);
//        } else {
//            LOGGER.debug("rename table failed:{}", tableId);
//            return CommonResult.failed("删表失败，其他原因");
//        }
//    }
}
