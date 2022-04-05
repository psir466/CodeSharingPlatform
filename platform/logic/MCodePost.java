package platform.logic;

public class MCodePost {

    private String code;
    private int time;
    private int views;

    public MCodePost(){


    }

    public MCodePost(String code) {
        this.code = code;
    }

    public MCodePost(String code, int timeRestr, int viewRestr) {
        this.code = code;
        this.time = timeRestr;
        this.views = viewRestr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
