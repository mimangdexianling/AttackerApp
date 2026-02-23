package com.pep.riyuxunlianying.bean;

import java.io.Serializable;
import java.util.List;

public class SessionKewen implements Serializable {
    // 保持 serialVersionUID 一致性（如果有的话，但反编译代码没显示，通常默认生成）
    // 为了保险，我们尽量保持结构一致
    public List<ClassData> classData;

    public static class ClassData implements Serializable {
        public String audioFile;
        public String classNumber;
        public String contents; // 这是我们要注入 Payload 的地方
        public String contentsTransfer;
        public String createTime;
        public String dataGroups;
        public String endTime;
        public String id;
        public String lineType;
        public String pageNo;
        public String pageSize;
        public String serialNumber;
        public String startOfPage;
        public String startTime;
        public String teachCode;
        public String updateTime;
    }
}