package com.example.mitu.realmmemoryleakegcheck;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mitu on 4/26/16.
 */
public class ContactModel extends RealmObject {

    @PrimaryKey
    private long mId;
    private String mName;

    private String mPhone;

    public ContactModel() {

    }

    public ContactModel(long mId, String mName, String mPhone) {
        this.mId = mId;
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public ContactModel(String mName, String mPhone) {
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public long getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
