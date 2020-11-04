package me.stephenj.sqlope.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.common.utils.LogGenerator;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.model.Field;
import me.stephenj.sqlope.service.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FieldController.java
 * @Description
 * @author 张润天
 * @Time 2020/11/3 23:34
 * @Field :
 */
@Api(tags = "FieldController", description = "数据列管理")
@Controller
@RequestMapping("/fieldope")
public class FieldController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldController.class);

    @Autowired
    private LogGenerator logGenerator;
    @Autowired
    private FieldService fieldService;


    @ApiOperation("获取数据列")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Field>> getFieldList(@RequestParam(value = "tableId")
                                                  @ApiParam("数据表序号") int tableId,
                                                  HttpServletRequest request) {
        List<Field> fields;
        try {
            fields = fieldService.listFields(tableId);
        } catch (TableNotExistException e) {
            LOGGER.debug("list field failed:{}", tableId);
            return CommonResult.failed("该表不存在");
        }
        logGenerator.log(request, "获取数据字段名");
        return CommonResult.success(fields);
    }

    @ApiOperation("创建数据字段")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createField(@RequestBody FieldParam fieldParam, HttpServletRequest request) {
        int count = 0;
        try {
            count = fieldService.createField(fieldParam);
        } catch (TableNotExistException | FieldExistException | ParameterLackException | FieldNotExistException e) {
            LOGGER.debug("create field failed:{}", fieldParam.getTableId());
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            List<String> names = fieldParam.getFieldDomains().stream().map(FieldDomain::getName).collect(Collectors.toList());
            logGenerator.log(request, "创建数据字段: " + names.toString());
            LOGGER.debug("create field success:{}", names.toString());
            return CommonResult.success(fieldParam);
        } else {
            LOGGER.debug("create field failed: tableId = {}", fieldParam.getTableId());
            return CommonResult.failed("创建数据字段失败，其他原因");
        }
    }


    @ApiOperation("删除数据字段")
    @RequestMapping(value = "/drop", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult dropField(@RequestParam(value = "fieldId")
                                  @ApiParam("数据字段序号") int fieldId,
                                  HttpServletRequest request) {
        int count = 0;
        try {
            count = fieldService.dropField(fieldId);
        } catch (ForeignKeyExistException | TableEmptyException | FieldNotExistException e) {
            LOGGER.debug("drop field failed: fieldId = {}", fieldId);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "删除数据字段: " + fieldId);
            LOGGER.debug("drop field success: fieldId = {}", fieldId);
            return CommonResult.success(fieldId);
        } else {
            LOGGER.debug("drop field failed: fieldId = {}", fieldId);
            return CommonResult.failed("删除数据字段失败，其他原因");
        }
    }

    @ApiOperation("修改数据列")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult modifyDt(@RequestBody FieldWithId fieldWithId,
                                 HttpServletRequest request) {
        int count = 0;
        try{
            count = fieldService.modifyField(fieldWithId);
        } catch (TableNotExistException | DataNotExistException | FieldNotExistException e) {
            LOGGER.debug("modify data failed: fieldId = {}", fieldWithId.getId());
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "修改数据列: " + fieldWithId.getId());
            LOGGER.debug("modify data success: fieldId = {}", fieldWithId.getId());
            return CommonResult.success(fieldWithId.getId());
        } else {
            LOGGER.debug("modify data failed: fieldId = {}", fieldWithId.getId());
            return CommonResult.failed("修改数据列失败，其他原因");
        }
    }
}
