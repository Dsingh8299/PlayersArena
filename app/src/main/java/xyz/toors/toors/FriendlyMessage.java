package xyz.toors.toors;



public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String senderId;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl,String senderId) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.senderId=senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
