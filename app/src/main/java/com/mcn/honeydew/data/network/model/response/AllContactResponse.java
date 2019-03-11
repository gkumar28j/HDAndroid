package com.mcn.honeydew.data.network.model.response;

import com.mcn.honeydew.data.network.model.AllContact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by atiwari on 10/3/18.
 */

public class AllContactResponse extends Observable<AllContactResponse> {

    List<AllContact> list;

    public AllContactResponse(List<AllContact> list) {
        this.list = list;
    }

    public List<AllContact> getList() {
        return list;
    }

    public void setList(List<AllContact> list) {
        this.list = list;
    }

    @Override
    protected void subscribeActual(Observer<? super AllContactResponse> observer) {

    }
}
