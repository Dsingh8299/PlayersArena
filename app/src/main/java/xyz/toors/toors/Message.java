package xyz.toors.toors;

public class Message {
    private String messageId,message,senderId;
    private String timestamp;
    private boolean isseen=false;

    public Message() {
    }

    public Message(String message, String senderId, String timestamp) {

        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;

    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
