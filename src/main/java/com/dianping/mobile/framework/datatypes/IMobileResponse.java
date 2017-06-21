package com.dianping.mobile.framework.datatypes;


import com.dianping.mobile.framework.base.datatypes.StatusCode;

public interface IMobileResponse {

    Object getData();

    void setData(Object displayObject);

    StatusCode getStatusCode();
}