package com.asai24.golf.object;

/**
 * Created by huynq on 4/11/16.
 */
public class ObjectKeyPre {
String key;
String mValue;
    public ObjectKeyPre(){}
    public ObjectKeyPre(String Key, String value){
        this.key = Key;
        this.mValue = value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

}
