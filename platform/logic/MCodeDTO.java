package platform.logic;

import java.time.LocalDate;

public class MCodeDTO {

    private String code;
    private String date;
    private int time;
    private int views;


    public MCodeDTO() {


    }

    public MCodeDTO(String code, String codeDateTime) {
        this.code = code;
        this.date = codeDateTime;
    }

    public MCodeDTO(String code, String date, int timeRestr, int viewRestr) {
        this.code = code;
        this.date = date;
        this.time = timeRestr;
        this.views = viewRestr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
