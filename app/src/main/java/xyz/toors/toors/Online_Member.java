package xyz.toors.toors;

public class Online_Member {
    private String name;
    private String uid;
    private String DpResource;



    public Online_Member() {
    }

    public Online_Member(String name, String uid, String dpResource) {
        this.name = name;
        this.uid = uid;
        DpResource = dpResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDpResource() {
        return DpResource;
    }

    public void setDpResource(String dpResource) {
        DpResource = dpResource;
    }
}
