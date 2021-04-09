package xyz.toors.toors;

public class Row_Post {
    private String name,dpUrl,imageUrl,description,date,time;
    String likesReference;
    boolean liked;

    public Row_Post() {
    }

    public Row_Post(String name, String dpUrl, String imageUrl, String description,String date,String time,String likesReference) {
        this.name = name;
        this.dpUrl = dpUrl;
        this.imageUrl = imageUrl;
        this.description = description;
        this.date=date;
        this.time=time;
        this.likesReference=likesReference;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getLikesReference() {
        return likesReference;
    }

    public void setLikesReference(String likesReference) {
        this.likesReference = likesReference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
