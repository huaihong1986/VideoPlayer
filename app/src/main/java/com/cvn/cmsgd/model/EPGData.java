package com.cvn.cmsgd.model;


import java.util.List;

public class EPGData {

    /**
     * id : 1389
     * channelId : 0
     * channel : 新闻综合一
     * title : 测试epg8
     * startTime : 00:47
     * endTime : 01:47
     * esort : 0
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private int id;
        private int channelId;
        private String channel;
        private String title;
        private String startTime;
        private String endTime;
        private int esort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getEsort() {
            return esort;
        }

        public void setEsort(int esort) {
            this.esort = esort;
        }
    }
}
