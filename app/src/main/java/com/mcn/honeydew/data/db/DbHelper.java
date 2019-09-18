package com.mcn.honeydew.data.db;


import com.mcn.honeydew.data.network.model.response.MyListResponseData;

import java.util.ArrayList;

/**
 * Created by amit on 14/2/18.
 */

public interface DbHelper {
    /*long insertProximityItem(ProximityItem item);

    void deleteAllProximityRecords();

    List<GetProximityResponse.Result> getProximityItemList();*/


    void insertListData(int listId,String response);

    ArrayList<MyListResponseData> getListData(int listId);

    void deleteAllRecords();

}
