package me.stephenj.sqlope.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.stephenj.sqlope.common.api.CommonResult;
import me.stephenj.sqlope.domain.LogParam;
import me.stephenj.sqlope.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName LogController.java
 * @Description
 * @author 张润天
 * @Time 2020/10/23 22:56
 * @Field :
 */
@Api(tags = "LogController", description = "日志管理")
@Controller
@RequestMapping("/log")
public class LogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);
    @Autowired
    private LogService logService;

    @ApiOperation("获取日志")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<String>> getLogList(@RequestBody LogParam logParam) {
        return CommonResult.success(logService.getLogList(logParam));
    }

    @ApiOperation("获取日志文件")
    @RequestMapping(value = "file", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getLogFile(HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ClassPathResource resource = new ClassPathResource("sqlope.log");
        IoUtil.copy(resource.getStream(), out);
        IoUtil.close(out);
        IoUtil.close(resource.getStream());
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=sqlope.log");
        return CommonResult.success("获取日志文件成功");
    }
}
