package com.asai24.golf.listener;

import com.asai24.golf.Constant;

public interface ServiceListener<Result extends Object> {
    void onPreExecute();
    void onPostExecute(Result result);
    void onError(Constant.ErrorServer errorServer);

}
