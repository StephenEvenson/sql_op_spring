package me.stephenj.sqlope.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.common.utils.LogGenerator;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.model.Data;
import me.stephenj.sqlope.service.RowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName RowController.java
 * @Description
 * @author 张润天
 * @Time 2020/11/4 15:12
 * @Field :
 */
@Api(tags = "RowController", description = "数据行管理")
@Controller
@RequestMapping("/rowope")
public class RowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RowController.class);

    @Autowired
    private LogGenerator logGenerator;
    @Autowired
    private RowService rowService;


    @ApiOperation("获取数据")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<List<Data>>> getRowList(@RequestBody RowListParam rowListParam,
                                                     HttpServletRequest request) {
        List<List<Data>> columns;
        try {
             columns = rowService.listRows(rowListParam);
        } catch (TableNotExistException | ConditionsException | FieldNotExistException e) {
            LOGGER.debug("list data failed:{}", rowListParam);
            return CommonResult.failed(e.getMessage());
        }
        LOGGER.debug("list data success: tableId = {}", rowListParam.getTableId());
        logGenerator.log(request, "获取数据: 表格" + rowListParam.getTableId());
        return CommonResult.success(columns);
    }

    @ApiOperation("添加数据")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addRows(@RequestBody RowAddParam rowAddParam, HttpServletRequest request) {
        int count;
        try {
            count = rowService.addRows(rowAddParam);
        } catch (TableNotExistException | FieldNotExistException | ForeignKeyExistException e) {
            LOGGER.debug("add data failed:{}", rowAddParam);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "添加数据: 表格" + rowAddParam.getTableId());
            LOGGER.debug("add data success: tableId = {}", rowAddParam.getTableId());
            return CommonResult.success(rowAddParam);
        } else {
            LOGGER.debug("add data failed: tableId = {}", rowAddParam.getTableId());
            return CommonResult.failed("添加记录失败，其他原因");
        }
    }


    @ApiOperation("更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRow(@RequestBody RowUpdateParam rowUpdateParam, HttpServletRequest request) {
        int count = 0;
        try {
            count = rowService.updateRow(rowUpdateParam);
        } catch (RowNotExistException | FieldNotExistException | ForeignKeyExistException e) {
            LOGGER.debug("update data failed: rowId = {}", rowUpdateParam.getRowId());
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "更新数据: " + rowUpdateParam.getRowId());
            LOGGER.debug("update record success: rowId = {}", rowUpdateParam.getRowId());
            return CommonResult.success(rowUpdateParam);
        } else {
            LOGGER.debug("update record failed: rowId = {}", rowUpdateParam.getRowId());
            return CommonResult.failed("更新记录失败，其他原因");
        }
    }

    @ApiOperation("删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRc(@RequestParam(value = "rowId")
                                 @ApiParam("数据行序号") int rowId,
                                 HttpServletRequest request) {
        int count;
        try {
            count = rowService.deleteRow(rowId);
        } catch (RowNotExistException | ForeignKeyExistException e) {
            LOGGER.debug("delete record failed: rowId = {}", rowId);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "删除数据: rowId = " + rowId);
            LOGGER.debug("delete record success: rowId = {}", rowId);
            return CommonResult.success(rowId);
        } else {
            LOGGER.debug("delete record failed: rowId = {}", rowId);
            return CommonResult.failed("删除记录失败，其他原因");
        }
    }
}
