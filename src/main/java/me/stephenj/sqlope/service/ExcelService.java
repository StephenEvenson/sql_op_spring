package me.stephenj.sqlope.service;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.RowListParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public interface ExcelService {
    String exportExcel(RowListParam rowListParam, ServletOutputStream out) throws TableNotExistException, FieldNotExistException, ConditionsException;

    int importExcel(MultipartFile file) throws IOException, TableNotExistException, RowNotExistException, FieldNotExistException, ForeignKeyExistException;
}
