package com.appdev.abhishek360.instruox;

import java.util.HashMap;
import java.util.Map;

public class jsonRequestAdapter
{



    private String requestAction;

    private Map<String,String> requestParameters = new HashMap<>();
   // private String requestParameteres=null;


    private Map<String,String> requestData= new HashMap<>();


    //private Map<String,String> requestMetaData= new HashMap<>();
    private String requestMetaData=null;



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

    public void setRequestMetaData(String key, String val)
    {
        //requestMetaData.put(key,val);
    }
}
