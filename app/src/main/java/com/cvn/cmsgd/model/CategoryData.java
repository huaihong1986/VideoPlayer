package com.cvn.cmsgd.model;


import java.util.List;

public class CategoryData {

    /**
     * col_id : 22
     * col_name : 活动
     * col_action : vod
     * col_resource : http://10.10.1.97:8080/cmsgd/clientI!getVideosList.do?cont_column_id=22
     * csort : 0
     * status : y
     * createTime : May 11, 2015 8:08:02 PM
     * type : 1
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private int col_id;
        private String col_name;
        private String col_action;
        private String col_resource;
        private int csort;
        private String status;
        private String createTime;
        private String type;

        public void setCol_id(int col_id) {
            this.col_id = col_id;
        }

        public void setCol_name(String col_name) {
            this.col_name = col_name;
        }

        public void setCol_action(String col_action) {
            this.col_action = col_action;
        }

        public void setCol_resource(String col_resource) {
            this.col_resource = col_resource;
        }

        public void setCsort(int csort) {
            this.csort = csort;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCol_id() {
            return col_id;
        }

        public String getCol_name() {
            return col_name;
        }

        public String getCol_action() {
            return col_action;
        }

        public String getCol_resource() {
            return col_resource;
        }

        public int getCsort() {
            return csort;
        }

        public String getStatus() {
            return status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getType() {
            return type;
        }
    }
}
