package me.stephenj.sqlope.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.utils.SqlCheck;
import me.stephenj.sqlope.common.utils.SqlRegistrant;
import me.stephenj.sqlope.domain.RowListParam;
import me.stephenj.sqlope.mbg.mapper.FieldMapper;
import me.stephenj.sqlope.mbg.mapper.TableMapper;
import me.stephenj.sqlope.mbg.model.*;
import me.stephenj.sqlope.service.ExcelService;
import me.stephenj.sqlope.service.RowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName ExcelServiceImpl.java
 * @Description
 * @author 张润天
 * @Time 2020/10/24 03:28
 * @Field :
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private SqlCheck sqlCheck;
    @Autowired
    @Value("${file.path}")
    private String path;
    @Autowired
    private RowService rowService;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private SqlRegistrant sqlRegistrant;

    @Override
    public String exportExcel(RowListParam rowListParam, ServletOutputStream out) throws TableNotExistException, FieldNotExistException, ConditionsException {
        List<List<Data>> tables = rowService.listRows(rowListParam);
        Table table = tableMapper.selectByPrimaryKey(rowListParam.getTableId());
        ExcelWriter writer = ExcelUtil.getWriter(path + "/" + DateUtil.format(DateUtil.date(), "yyyyMMdd-HHmmss") + "/" + table.getName() + ".xls", table.getName());
        writeExcel(writer, tables);
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
        return table.getName() + ".xls";
    }

    @Override
    public int importExcel(MultipartFile file) throws IOException, TableNotExistException, RowNotExistException, FieldNotExistException {
        Optional<MultipartFile> fileOptional = Optional.ofNullable(file);
        if (fileOptional.isPresent()) {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<String> sheets = reader.getSheetNames();
            for (String sheet: sheets) {
                Table table = new Table();
                table.setName(sheet);
                sqlCheck.checkTableExist(table);
                reader.setSheet(sheet);
                readExcel(reader, table);
            }
            return 1;
        } else {
            throw new FileNotFoundException("没有上传文件");
        }
    }

    private void readExcel(ExcelReader reader, Table table) throws FieldNotExistException, TableNotExistException, RowNotExistException {
        int columnCount = reader.getColumnCount();
        if (columnCount > 0) {
            List<Map<String, Object>> readAll = reader.readAll();
            sqlRegistrant.importExcel(readAll, table);
        }
    }

    private void writeExcel(ExcelWriter writer, List<List<Data>> table) {
        ArrayList<Map<String, String>> rows = CollUtil.newArrayList();
        int count = 1;
        Map<Integer, String> fields = new HashMap<>();
        for (List<Data> dataList : table) {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("_id", String.valueOf(dataList.get(0).getRowId()));
            for (Data data: dataList) {
                if (count == 1) {
                    fields.put(data.getFieldId(), fieldMapper.selectByPrimaryKey(data.getFieldId()).getName());
                }
                row.put(fields.get(data.getFieldId()), data.getValue());
            }
            rows.add(row);
            count ++;
        }
        if (rows.size() > 0) {
            writer.write(rows, true);
        }
    }
}
