package me.stephenj.sqlope.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.stephenj.sqlope.Exception.ConditionsException;
import me.stephenj.sqlope.Exception.DatabaseNotExistException;
import me.stephenj.sqlope.Exception.ParameterLackException;
import me.stephenj.sqlope.Exception.TableNotExistException;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.common.utils.LogGenerator;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.service.RcService;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName RcController.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 21:12
 * @Field :
 */
@Api(tags = "RcController", description = "数据记录管理")
@Controller
@RequestMapping("/rcope")
public class RcController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TbController.class);

    @Autowired
    private RcService rcService;
    @Autowired
    private LogGenerator logGenerator;
    @ApiOperation("获取数据")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<List<ResultCell>>> getRcList(@RequestBody RcListParam rcListParam,
                                                          HttpServletRequest request) {
        List<List<ResultCell>> columns = null;
        try {
             columns = rcService.listRcs(rcListParam);
        } catch (DatabaseNotExistException | TableNotExistException | SQLException | ConditionsException e) {
            LOGGER.debug("list record failed:{}", rcListParam);
            return CommonResult.failed(e.getMessage());
        }
        logGenerator.log(request, "获取数据: 表格" + rcListParam.getName());
        return CommonResult.success(columns);
    }

    @ApiOperation("添加数据")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addRc(@RequestBody RcAddParam rcAddParam, HttpServletRequest request) {
        int count = 0;
        try {
            count = rcService.addRc(rcAddParam);
        } catch (DatabaseNotExistException | TableNotExistException e) {
            LOGGER.debug("add data failed:{}", rcAddParam);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "添加数据: 表格" + rcAddParam.getName());
            LOGGER.debug("add record success:{}", rcAddParam);
            return CommonResult.success(rcAddParam);
        } else {
            LOGGER.debug("add record failed:{}", rcAddParam);
            return CommonResult.failed("添加记录失败，其他原因");
        }
    }

    @ApiOperation("添加数据（多个）")
    @RequestMapping(value = "/addn", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addRc(@RequestBody RcAddParamList rcAddParamList, HttpServletRequest request) {
        int count = 0;
        Optional<List<List<ResultCell>>> rows = Optional.ofNullable(rcAddParamList.getRows());
        if (!rows.isPresent() || rows.get().size() <= 0 ) {
            LOGGER.debug("add data failed:{}", rcAddParamList);
            return CommonResult.failed("添加记录失败，没有记录行");
        }
        for (List<ResultCell> row: rows.get()) {
            RcAddParam rcAddParam = new RcAddParam();
            BeanUtils.copyProperties(rcAddParamList, rcAddParam);
            rcAddParam.setRow(row);
            try {
                count = rcService.addRc(rcAddParam);
            } catch (DatabaseNotExistException | TableNotExistException e) {
                LOGGER.debug("add data failed:{}", rcAddParam);
                return CommonResult.failed(e.getMessage());
            }
            if (count != 1) {
                LOGGER.debug("add record failed:{}", rcAddParam);
                return CommonResult.failed("添加记录失败，其他原因");
            }
        }
        logGenerator.log(request, "添加数据: 表格" + rcAddParamList.getName());
        LOGGER.debug("add record success:{}", rcAddParamList);
        return CommonResult.success(rcAddParamList);
    }

    @ApiOperation("更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRc(@RequestBody RcUpdateParam rcUpdateParam, HttpServletRequest request) {
        int count = 0;
        try {
            count = rcService.updateRc(rcUpdateParam);
        } catch (DatabaseNotExistException | TableNotExistException | ParameterLackException e) {
            LOGGER.debug("update record failed:{}", rcUpdateParam);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "更新数据: " + rcUpdateParam.getName());
            LOGGER.debug("update record success:{}", rcUpdateParam);
            return CommonResult.success(rcUpdateParam);
        } else {
            LOGGER.debug("update record failed:{}", rcUpdateParam);
            return CommonResult.failed("更新记录失败，其他原因");
        }
    }

    @ApiOperation("删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRc(@RequestBody RcDeleteParam rcDeleteParam, HttpServletRequest request) {
        int count = 0;
        try {
            count = rcService.deleteRc(rcDeleteParam);
        } catch (TableNotExistException | DatabaseNotExistException | ParameterLackException e) {
            LOGGER.debug("delete record failed:{}", rcDeleteParam);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "删除数据: " + rcDeleteParam.getName());
            LOGGER.debug("delete record success:{}", rcDeleteParam);
            return CommonResult.success(rcDeleteParam);
        } else {
            LOGGER.debug("delete record failed:{}", rcDeleteParam);
            return CommonResult.failed("删除记录失败，其他原因");
        }
    }

}
