package com.mcn.honeydew.data.db.tables;

public class ProximityItem {
    public static final String TABLE_NAME = "proximity_item";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_LIST_NAME = "list_name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_SHARED_TO = "shared_to";
    public static final String COLUMN_SHARED_BY = "shared_by";
    public static final String COLUMN_USER_PROFILE_ID = "user_profile_id";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PROXIMITY_ID = "proximity_id";
    public static final String COLUMN_CREATED_DATE = "created_date";
    public static final String COLUMN_TO_USER_PROFILE_ID = "to_user_profile_id";
    public static final String COLUMN_FROM_USER_PROFILE_ID = "from_user_profile_id";
    public static final String COLUMN_STATUS_ID = "status_id";
    public static final String COLUMN_FROM_USER_NAME = "from_user_name";
    public static final String COLUMN_LIST_HEADER_COLOR = "list_header_color";
    public static final String COLUMN_ITEM_TIME = "item_time";
    public static final String COLUMN_IS_ADDED = "is_added";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ITEM_ID + " INTEGER,"
                    + COLUMN_LIST_ID + " INTEGER,"
                    + COLUMN_ITEM_NAME + " TEXT,"
                    + COLUMN_LIST_NAME + " TEXT,"
                    + COLUMN_LATITUDE + " REAL,"
                    + COLUMN_LONGITUDE + " REAL,"
                    + COLUMN_SHARED_TO + " INTEGER,"
                    + COLUMN_SHARED_BY + " INTEGER,"
                    + COLUMN_USER_PROFILE_ID + " TEXT,"
                    + COLUMN_LOCATION + " TEXT,"
                    + COLUMN_PROXIMITY_ID + " TEXT,"
                    + COLUMN_CREATED_DATE + " TEXT,"
                    + COLUMN_TO_USER_PROFILE_ID + " TEXT,"
                    + COLUMN_FROM_USER_PROFILE_ID + " TEXT,"
                    + COLUMN_STATUS_ID + " INTEGER,"
                    + COLUMN_FROM_USER_NAME + " TEXT,"
                    + COLUMN_LIST_HEADER_COLOR + " TEXT,"
                    + COLUMN_ITEM_TIME + " TEXT,"
                    + COLUMN_IS_ADDED + " INTEGER"
                    + ")";


    //
    private int id;
    private int itemId;
    private int listId;
    private String itemName;
    private String listName;
    private double latitude;
    private double longitude;
    private int sharedTo;
    private int sharedBy;
    private String userProfileId;
    private String location;
    private String proximityId;
    private String createdDate;
    private String toUserProfileId;
    private String fromUserProfileId;
    private int statusId;
    private String fromUserName;
    private String listHeaderColor;
    private String itemTime;
    private int isAdded;

    public ProximityItem() {

    }

    public ProximityItem(int itemId, int listId, String itemName, String listName, double latitude, double longitude, int sharedTo, int sharedBy, String userProfileId, String location, String proximityId, String createdDate, String toUserProfileId, String fromUserProfileId, int statusId, String fromUserName, String listHeaderColor, String itemTime, int isAdded) {
        this.itemId = itemId;
        this.listId = listId;
        this.itemName = itemName;
        this.listName = listName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.userProfileId = userProfileId;
        this.location = location;
        this.proximityId = proximityId;
        this.createdDate = createdDate;
        this.toUserProfileId = toUserProfileId;
        this.fromUserProfileId = fromUserProfileId;
        this.statusId = statusId;
        this.fromUserName = fromUserName;
        this.listHeaderColor = listHeaderColor;
        this.itemTime = itemTime;
        this.isAdded = isAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(int sharedTo) {
        this.sharedTo = sharedTo;
    }

    public int getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(int sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProximityId() {
        return proximityId;
    }

    public void setProximityId(String proximityId) {
        this.proximityId = proximityId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getToUserProfileId() {
        return toUserProfileId;
    }

    public void setToUserProfileId(String toUserProfileId) {
        this.toUserProfileId = toUserProfileId;
    }

    public String getFromUserProfileId() {
        return fromUserProfileId;
    }

    public void setFromUserProfileId(String fromUserProfileId) {
        this.fromUserProfileId = fromUserProfileId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getListHeaderColor() {
        return listHeaderColor;
    }

    public void setListHeaderColor(String listHeaderColor) {
        this.listHeaderColor = listHeaderColor;
    }

    public String getItemTime() {
        return itemTime;
    }

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    public int getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(int isAdded) {
        this.isAdded = isAdded;
    }
}
