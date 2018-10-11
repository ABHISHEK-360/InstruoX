package com.appdev.abhishek360.instruox;

import java.util.HashMap;
import java.util.Map;

public class jsonResponseAdapter
{
    private String reponseStatus=null;

    //private Map<String,String> responseMessage = new HashMap<>();
    private String responseMessage=null;


    private Map<String,String> reponseData= new HashMap<>();


    //private Map<String,String> responseMetaData= new HashMap<>();
    private String responseMetaData=null;

    public String getReponseStatus()
    {
        return reponseStatus;
    }

    public void setReponseStatus(String reponseStatus)
    {
        this.reponseStatus = reponseStatus;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

    public Map<String, String> getReponseData()
    {
        return reponseData;
    }

    public void setReponseData(Map<String, String> reponseData)
    {
        this.reponseData = reponseData;
    }

    public String getResponseMetaData()
    {
        return responseMetaData;
    }

    public void setResponseMetaData(String responseMetaData)
    {
        this.responseMetaData = responseMetaData;
    }
}
