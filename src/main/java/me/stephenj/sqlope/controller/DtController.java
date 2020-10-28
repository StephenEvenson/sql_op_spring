package me.stephenj.sqlope.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.common.utils.LogGenerator;
import me.stephenj.sqlope.domain.DtDomain;
import me.stephenj.sqlope.domain.DtParam;
import me.stephenj.sqlope.domain.DtParamList;
import me.stephenj.sqlope.domain.DtTemp;
import me.stephenj.sqlope.mbg.model.Dt;
import me.stephenj.sqlope.service.DtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName DtController.java
 * @Description
 * @author 张润天
 * @Time 2020/10/19 01:01
 * @Field :
 */
@Api(tags = "DtController", description = "数据列管理")
@Controller
@RequestMapping("/dtope")
public class DtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DtController.class);

    @Autowired
    private DtService dtService;
    @Autowired
    private LogGenerator logGenerator;
    @ApiOperation("获取数据列")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Dt>> getDtList(@RequestParam(value = "tbId")
                                            @ApiParam("数据表序号") int tbId,
                                            HttpServletRequest request) {
        List<Dt> dts;
        try {
            dts = dtService.listDts(tbId);
        } catch (TableNotExistException e) {
            LOGGER.debug("list data failed:{}", tbId);
            return CommonResult.failed("该表不存在");
        }
        logGenerator.log(request, "获取数据列名");
        return CommonResult.success(dts);
    }

    @ApiOperation("创建数据列")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createDt(@RequestBody DtParam dtParam, HttpServletRequest request) {
        int count = 0;
        DtTemp dtTemp = new DtTemp();
        BeanUtils.copyProperties(dtParam, dtTemp);
        BeanUtils.copyProperties(dtParam.getDtDomain(), dtTemp);
        try {
            count = dtService.createDt(dtTemp);
        } catch (TableNotExistException | DataNotCompleteException | DataNotExistException | DataExistException e) {
            LOGGER.debug("create data failed:{}", dtParam.getTbId());
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "创建数据列: " + dtParam.getDtDomain().getName());
            LOGGER.debug("create data success:{}", dtTemp.getName());
            return CommonResult.success(dtParam);
        } else {
            LOGGER.debug("create data failed:{}", dtTemp.getName());
            return CommonResult.failed("创建数据列失败，其他原因");
        }
    }

    @ApiOperation("创建数据列（多个）")
    @RequestMapping(value = "/createn", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createDts(@RequestBody DtParamList dtParamList, HttpServletRequest request) {
        int count = 0;
        DtTemp dtTemp = new DtTemp();
        BeanUtils.copyProperties(dtParamList, dtTemp);
        Optional<List<DtDomain>> dtDomains = Optional.ofNullable(dtParamList.getDtDomains());
        if (dtDomains.isPresent() && dtDomains.get().size() > 0) {
            for (DtDomain dtDomain: dtDomains.get()){
                BeanUtils.copyProperties(dtDomain, dtTemp);
                try {
                    count = dtService.createDt(dtTemp);
                } catch (TableNotExistException | DataNotCompleteException | DataNotExistException | DataExistException e) {
                    LOGGER.debug("create data failed:{}", dtParamList.getTbId());
                    return CommonResult.failed(e.getMessage());
                }
                if (count != 1) {
                    LOGGER.debug("create data failed:{}", dtTemp.getName());
                    return CommonResult.failed("创建数据列失败，其他原因");
                }
            }
        } else {
            LOGGER.debug("create data failed:{}", dtParamList.getTbId());
            return CommonResult.failed("创建数据列失败，缺少数据列参数");
        }
        List<String> names = dtDomains.get().stream().map(DtDomain::getName).collect(Collectors.toList());
        logGenerator.log(request, "创建数据列: " + names.toString());
        LOGGER.debug("create data success:{}", names.toString());
        return CommonResult.success(dtParamList);
    }

    @ApiOperation("删除数据列")
    @RequestMapping(value = "/drop", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult dropDt(@RequestParam(value = "dtId")
                               @ApiParam("数据列序号") int dtId,
                               HttpServletRequest request) {
        int count = 0;
        DtTemp dtTemp = new DtTemp();
        dtTemp.setId(dtId);
        try {
            count = dtService.dropDt(dtTemp);
        } catch (DataNotExistException | ForeignKeyExistException | TableEmptyException e) {
            LOGGER.debug("drop data failed:{}", dtId);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "删除数据列: " + dtId);
            LOGGER.debug("drop data success:{}", dtId);
            return CommonResult.success(dtId);
        } else {
            LOGGER.debug("drop data failed:{}", dtId);
            return CommonResult.failed("删除数据列失败，其他原因");
        }
    }

    @ApiOperation("修改数据列")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult modifyDt(@RequestBody DtParam dtParam,
                                 @RequestParam(value = "dtId")
                                 @ApiParam("数据列序号") int dtId,
                                 HttpServletRequest request) {
        int count = 0;
        DtTemp dtTemp = new DtTemp();
        BeanUtils.copyProperties(dtParam, dtTemp);
        BeanUtils.copyProperties(dtParam.getDtDomain(), dtTemp);
        dtTemp.setId(dtId);
        try{
            count = dtService.modifyDt(dtTemp);
        } catch (TableNotExistException | DataNotExistException | DataNotCompleteException | DataExistException e) {
            LOGGER.debug("modify data failed:{}", dtId);
            return CommonResult.failed(e.getMessage());
        }
        if (count == 1) {
            logGenerator.log(request, "修改数据列: " + dtId + ": " + dtParam.getDtDomain().getName());
            LOGGER.debug("modify data success:{}", dtId);
            return CommonResult.success(dtId);
        } else {
            LOGGER.debug("modify data failed:{}", dtId);
            return CommonResult.failed("修改数据列失败，其他原因");
        }
    }
}
