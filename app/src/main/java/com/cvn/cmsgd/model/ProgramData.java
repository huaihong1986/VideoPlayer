package com.cvn.cmsgd.model;


import java.util.List;

public class ProgramData {


    /**
     * cont_id : 12
     * title : 新闻综合一
     * cont_pic : http://10.9.200.11:8011//programImage/1449490290112.png
     * cont_play : http://121.40.184.131:8011/advideo/fenda/fenda.m3u8
     * cont_column_id : 6
     * gscores : 0
     * isPay : n
     * price : 0.0
     * payScores : 0
     * timeslong : 0
     * timeFreelong : 0
     * egpUrl :
     * seeTimes : 0
     * psort : 0
     * cont_play_high : http://10.9.200.11:8011/advideo/baiwei/baiwei.m3u8
     * epgList : [{"id":1395,"channelId":0,"channel":"新闻综合一","title":"测试epg4","startTime":"20:49","endTime":"21:49","esort":0}]
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private int cont_id;
        private String title;
        private String cont_pic;
        private String cont_play;
        private int cont_column_id;
        private int gscores;
        private String isPay;
        private double price;
        private int payScores;
        private int timeslong;
        private int timeFreelong;
        private String egpUrl;
        private int seeTimes;
        private int psort;
        private String cont_play_high;
        /**
         * id : 1395
         * channelId : 0
         * channel : 新闻综合一
         * title : 测试epg4
         * startTime : 20:49
         * endTime : 21:49
         * esort : 0
         */

        private List<EpgListEntity> epgList;

        public int getCont_id() {
            return cont_id;
        }

        public void setCont_id(int cont_id) {
            this.cont_id = cont_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCont_pic() {
            return cont_pic;
        }

        public void setCont_pic(String cont_pic) {
            this.cont_pic = cont_pic;
        }

        public String getCont_play() {
            return cont_play;
        }

        public void setCont_play(String cont_play) {
            this.cont_play = cont_play;
        }

        public int getCont_column_id() {
            return cont_column_id;
        }

        public void setCont_column_id(int cont_column_id) {
            this.cont_column_id = cont_column_id;
        }

        public int getGscores() {
            return gscores;
        }

        public void setGscores(int gscores) {
            this.gscores = gscores;
        }

        public String getIsPay() {
            return isPay;
        }

        public void setIsPay(String isPay) {
            this.isPay = isPay;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getPayScores() {
            return payScores;
        }

        public void setPayScores(int payScores) {
            this.payScores = payScores;
        }

        public int getTimeslong() {
            return timeslong;
        }

        public void setTimeslong(int timeslong) {
            this.timeslong = timeslong;
        }

        public int getTimeFreelong() {
            return timeFreelong;
        }

        public void setTimeFreelong(int timeFreelong) {
            this.timeFreelong = timeFreelong;
        }

        public String getEgpUrl() {
            return egpUrl;
        }

        public void setEgpUrl(String egpUrl) {
            this.egpUrl = egpUrl;
        }

        public int getSeeTimes() {
            return seeTimes;
        }

        public void setSeeTimes(int seeTimes) {
            this.seeTimes = seeTimes;
        }

        public int getPsort() {
            return psort;
        }

        public void setPsort(int psort) {
            this.psort = psort;
        }

        public String getCont_play_high() {
            return cont_play_high;
        }

        public void setCont_play_high(String cont_play_high) {
            this.cont_play_high = cont_play_high;
        }

        public List<EpgListEntity> getEpgList() {
            return epgList;
        }

        public void setEpgList(List<EpgListEntity> epgList) {
            this.epgList = epgList;
        }

        public static class EpgListEntity {
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
}
