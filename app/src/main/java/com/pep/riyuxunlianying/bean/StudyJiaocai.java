package com.pep.riyuxunlianying.bean;

import java.io.Serializable;
import java.util.List;

public class StudyJiaocai implements Serializable {
    public String className;
    public String classNumber;
    public List<ClassSection> classSectionList;

    public static class ClassSection implements Serializable {
        public String classNumber;
        public String classSection;
        public String introduction;
        public double rate;
        public String serialNumber;
        public String showPictures;
        public String sorts;
        public String teachCode;
        public String title;
    }
}