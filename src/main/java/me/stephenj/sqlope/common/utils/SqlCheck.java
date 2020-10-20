package me.stephenj.sqlope.common.utils;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.DtTemp;
import me.stephenj.sqlope.domain.TbDomain;
import me.stephenj.sqlope.domain.TbTemp;
import me.stephenj.sqlope.mbg.mapper.DbMapper;
import me.stephenj.sqlope.mbg.mapper.DtMapper;
import me.stephenj.sqlope.mbg.mapper.TbMapper;
import me.stephenj.sqlope.mbg.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName SqlCheck.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 10:52
 * @Field :
 */
@Service
public class SqlCheck {
    @Autowired
    private TbMapper tbMapper;
    @Autowired
    private DtMapper dtMapper;
    @Autowired
    private DbMapper dbMapper;

    public boolean checkCreateDt(DtTemp dtTemp) throws TableNotExistException, DataNotCompleteException, DataNotExistException, DataExistException {
        Optional<Tb> tbOp = Optional.ofNullable(tbMapper.selectByPrimaryKey(dtTemp.getTbId()));
        if (!tbOp.isPresent()) {
            throw new TableNotExistException("该表不存在");
        }
        Tb tb = tbOp.get();
        dtTemp.setTbName(tb.getName());
        dtTemp.setDbId(tb.getDbId());
        dtTemp.setDbName(dbMapper.selectByPrimaryKey(dtTemp.getDbId()).getName());

        if (dtTemp.getName().isEmpty() || dtTemp.getType().isEmpty()
            || (dtTemp.isForeignkey() && (dtTemp.getTgDt().isEmpty() || dtTemp.getTgTb().isEmpty()))) {
            throw new DataNotCompleteException("数据不完整，没有列名或类型或外键");
        }
        DtExample dtExample = new DtExample();
        dtExample.createCriteria().andTbIdEqualTo(dtTemp.getTbId()).andNameEqualTo(dtTemp.getName());
        List<Dt> dts = dtMapper.selectByExample(dtExample);
        if (!dts.isEmpty()) {
            throw new DataExistException("该表中已存在同名数据列");
        }
        if (dtTemp.isForeignkey()) {
            this.checkFk(dtTemp);
        }
        return true;
    }

    public boolean checkDropDt(DtTemp dtTemp) throws DataNotExistException, ForeignKeyExistException, TableEmptyException {
        Optional<Dt> dtOptional = Optional.ofNullable(dtMapper.selectByPrimaryKey(dtTemp.getId()));
        if (!dtOptional.isPresent()) {
            throw new DataNotExistException("该数据列不存在");
        } else {
            BeanUtils.copyProperties(dtOptional.get(), dtTemp);
        }
        DtExample srcDtExample = new DtExample();
        srcDtExample.createCriteria().andFkEqualTo(dtTemp.getId());
        List<Dt> srcDtOps = dtMapper.selectByExample(srcDtExample);
        if (!srcDtOps.isEmpty()) {
            Dt srcDt = srcDtOps.get(0);
            Tb srcTb = tbMapper.selectByPrimaryKey(srcDt.getTbId());
            throw new ForeignKeyExistException(srcTb.getName(), srcDt.getName());
        }
        DtExample dtExample = new DtExample();
        dtExample.createCriteria().andTbIdEqualTo(dtTemp.getTbId());
        List<Dt> dts = dtMapper.selectByExample(dtExample);
        if (dts.size()<2) {
            throw new TableEmptyException("数据表中的数据列数量不能为0");
        }
        Tb tb = tbMapper.selectByPrimaryKey(dtTemp.getTbId());
        dtTemp.setTbName(tb.getName());
        dtTemp.setDbName(dbMapper.selectByPrimaryKey(tb.getDbId()).getName());
        return true;
    }

    public boolean checkModifyDt(DtTemp dtTemp) throws DataNotCompleteException, DataExistException, DataNotExistException, TableNotExistException {
        Tb tb = tbMapper.selectByPrimaryKey(dtTemp.getTbId());
        dtTemp.setTbName(tb.getName());
        dtTemp.setDbId(tb.getDbId());
        dtTemp.setDbName(dbMapper.selectByPrimaryKey(dtTemp.getDbId()).getName());

        if (dtTemp.getName().isEmpty() || dtTemp.getType().isEmpty()
                || (dtTemp.isForeignkey() && (dtTemp.getTgDt().isEmpty() || dtTemp.getTgTb().isEmpty()))) {
            throw new DataNotCompleteException("数据不完整，没有列名或类型或外键");
        }
        DtExample dtExample = new DtExample();
        dtExample.createCriteria().andTbIdEqualTo(dtTemp.getTbId()).andNameEqualTo(dtTemp.getName());
        List<Dt> dts = dtMapper.selectByExample(dtExample);
        if (!dts.isEmpty()) {
            throw new DataExistException("该表中已存在同名数据列");
        }
        if (dtTemp.isForeignkey()) {
            this.checkFk(dtTemp);
        }
        dtTemp.setOldName(dtMapper.selectByPrimaryKey(dtTemp.getId()).getName());
        return true;
    }

    public boolean checkListRc(TbTemp tbTemp) throws TableNotExistException, DatabaseNotExistException {
        Optional<Db> dbOptional = Optional.ofNullable(dbMapper.selectByPrimaryKey(tbTemp.getDbId()));
        if (!dbOptional.isPresent()) {
            throw new DatabaseNotExistException("该数据库不存在");
        }
        Optional<Tb> tbOptional = Optional.ofNullable(tbMapper.selectByPrimaryKey(tbTemp.getId()));
        if (!tbOptional.isPresent()) {
            throw new TableNotExistException("该数据表不存在");
        }
        return true;
    }



    public boolean checkFk(DtTemp dtTemp) throws TableNotExistException, DataNotExistException {
        TbExample tbExample = new TbExample();
        tbExample.createCriteria().andDbIdEqualTo(dtTemp.getDbId()).andNameEqualTo(dtTemp.getTgTb());
        List<Tb> tbs = tbMapper.selectByExample(tbExample);
        if (tbs.isEmpty()) {
            throw new TableNotExistException("外键指向的表不存在");
        }
        Tb tgTb = tbs.get(0);
        DtExample tgDtExample = new DtExample();
        tgDtExample.createCriteria().andTbIdEqualTo(tgTb.getId()).andNameEqualTo(dtTemp.getTgDt());
        List<Dt> tgDts = dtMapper.selectByExample(tgDtExample);
        if (tgDts.isEmpty()) {
            throw new DataNotExistException("外键指向的数据列不存在");
        } else {
            dtTemp.setTgDtId(tgDts.get(0).getId());
        }
        return true;
    }

}
