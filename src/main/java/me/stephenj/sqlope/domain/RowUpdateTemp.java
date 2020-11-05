package me.stephenj.sqlope.domain;

import java.util.HashMap;

/**
 * @ClassName RowUpdateTemp.java
 * @Description
 * @author 张润天
 * @Time 2020/11/5 21:53
 * @Field :
 */
public class RowUpdateTemp extends RowUpdateParam{
    private HashMap<Integer, Integer> targetFieldIdMap = new HashMap<>();

    private HashMap<String, Integer> targetDataIdMap = new HashMap<>();

    public HashMap<String, Integer> getTargetDataIdMap() {
        return targetDataIdMap;
    }

    public void setTargetDataIdMap(HashMap<String, Integer> targetDataIdMap) {
        this.targetDataIdMap = targetDataIdMap;
    }

    public HashMap<Integer, Integer> getTargetFieldIdMap() {
        return targetFieldIdMap;
    }

    public void setTargetFieldIdMap(HashMap<Integer, Integer> targetFieldIdMap) {
        this.targetFieldIdMap = targetFieldIdMap;
    }
}
