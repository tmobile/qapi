package com.tmobile.ct.tep.qapi.tvaultclient;

import com.tmobile.ct.tep.qapi.exceptions.ApiFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class ServiceHelper {
    private static final Logger log = LoggerFactory.getLogger(ServiceHelper.class);

    private Integer maxRequestRetries;
    private Boolean failFast;

    public ServiceHelper(Integer maxRequestRetries, Boolean failFast) {
        this.maxRequestRetries = maxRequestRetries;
        this.failFast = failFast;
    }

    <E> E handleResponse(Call<E> call) {
        for(int i=0; i < maxRequestRetries; i++){
            try {
                Response<E> response = call.execute();
                log.debug("Response thread[{}] code[{}] raw[{}]",
                        Thread.currentThread().getId(), response.code(), response.raw());

                return response.body();
            } catch (IOException e) {
                log.warn("Request failed, retry[{}] thread[{}] e[{}] url[{}]", i,
                        Thread.currentThread().getId(), e.getMessage(), call.request().url());
                call = call.clone();
            }
        }

        if(failFast){
            throw new ApiFailureException("Failed to communicate with the Test Data Platform while authenticating");
        }else{
            return null;
        }
    }
}
