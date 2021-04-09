package xyz.toors.toors;

public class Row_Member {
    private String mDpResource;
    private String mMemberName;
    private String mLastMessage;
    private String uid;
    public long timestamp;
    private String time="";



    public Row_Member() {
    }


    public Row_Member( String Uid, String DpResource, String MemberName, String LastMessage) {
        mDpResource = DpResource;
        mMemberName = MemberName;
        mLastMessage = LastMessage;
        uid = Uid;


    }


    public String getUid() {
        return uid;
    }

    public String getDpResource() {
        return mDpResource;
    }

    public String getMemberName() {
        return mMemberName;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
