package me.stephenj.sqlope.domain;

import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public enum TypeEnum {
    INT{
        @Override
        Object read(String data) {
            return Integer.parseInt(data);
        }

        @Override
        String write(Object data) {
            return String.valueOf((int) data);
        }
    },
    FLOAT {
        @Override
        Object read(String data) {
            return Float.parseFloat(data);
        }

        @Override
        String write(Object data) {
            return String.valueOf((float) data);
        }
    },
    DECIMAL {
        @Override
        Object read(String data) {
            return new BigDecimal(data);
        }

        @Override
        String write(Object data) {
            return data.toString();
        }
    },
    VARCHAR {
        @Override
        Object read(String data) {
            return data;
        }

        @Override
        String write(Object data) {
            return (String) data;
        }
    },
    BLOB {
        @Override
        Object read(String data) {
            return data.getBytes();
        }

        @Override
        String write(Object data) {
            return new String((byte[]) data);
        }
    },
    DATETIME {
        @Override
        Object read(String data) {
            return DateUtil.parse(data, "yyyy-MM-dd-HH:mm");
        }

        @Override
        String write(Object data) {
            return DateUtil.format((Date) data, "yyyy-MM-dd-HH:mm");
        }
    };

    abstract Object read(String data);

    abstract String write(Object data);

}
