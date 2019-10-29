package com.appdev.abhishek360.instruo.ApiModels;

import java.util.HashMap;
import java.util.Map;

public class RequestModel {
    private String requestAction;
    private Map<String,String> requestParameters = new HashMap<>();
    private Map<String,String> requestData= new HashMap<>();

    public void setRequestAction(String requestAction)
    {
        this.requestAction = requestAction;
    }

    public void setRequestParameteres(String key ,String val)
    {
        requestParameters.put(key,val);
    }

    public void setRequestData(String key, String val)
    {
        requestData.put(key,val);
    }

}
