package com.cvn.cmsgd.net;


public class Urls {

    public static final String PREFIX = "http://10.9.200.11:8080/cmsgd/clientI!";
//    public static final String PREFIX = "http://203.118.192.36:80/cmsgd/clientI!";
//    public static final String PREFIX = "http://203.118.192.20:80/cmsgd/clientI!";


    public static final String GET_CATEGORY = PREFIX + "getColumnTypeList.do";

    public static final String GET_PROGRAM_LIST = PREFIX + "getProgramsList.do?cont_column_id=";

    public static final String GET_EPG_LIST = PREFIX + "getEpgList.do?";

    public static final String LOGIN = PREFIX + "getDoUser.do";

}
