package xyz.toors.toors.Notification;

public class Data {
    private String Title;
    private String Message;
    String user;

    public Data(String user,String title, String message) {
        Title = title;
        Message = message;
        this.user=user;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
