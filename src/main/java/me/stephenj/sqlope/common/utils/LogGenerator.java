package me.stephenj.sqlope.common.utils;

import me.stephenj.sqlope.mbg.mapper.LogsMapper;
import me.stephenj.sqlope.mbg.model.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LogGenerator.java
 * @Description
 * @author 张润天
 * @Time 2020/10/23 06:21
 * @Field :
 */
@Service
public class LogGenerator {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private LogsMapper logsMapper;

    public void log(HttpServletRequest request, String logs) {
        log(userUtils.getUsernameByToken(request), logs);
    }

    public void log(String username, String logs){
        Logs log = new Logs();
        log.setUserId(userUtils.getIdByUsername(username));
        log.setUsername(username);
        log.setOperation(logs);
        logsMapper.insert(log);
    }
}
