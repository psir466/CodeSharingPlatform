package platform.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity(name = "Mcode")
public class MCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long codeId;

    private String uuidCode;
    private String code;
    private LocalDate codeDate;
    private LocalTime codeTime;
    private int time;
    private int views;
    boolean secretCodeTime;
    boolean secretCodeViews;

    public MCode() {

    }

    public MCode(String code, LocalDate codeDate, LocalTime codeTime) {
        this.code = code;
        this.codeDate = codeDate;
        this.codeTime = codeTime;
    }

    public MCode(String code, LocalDate codeDate, LocalTime codeTime, int timeRestr, int viewRestr) {
        this.uuidCode = uuIdGenerate();
        this.code = code;
        this.codeDate = codeDate;
        this.codeTime = codeTime;
        this.time = timeRestr;
        this.views = viewRestr;

        if(this.time !=0){

            this.secretCodeTime = true;
        }else{

            this.secretCodeTime = false;
        }

        if(this.views !=0){

            this.secretCodeViews = true;
        }else{

            this.secretCodeViews = false;
        }
    }

    public long getCodeId() {
        return codeId;
    }

    public void setCodeId(long codeId) {
        this.codeId = codeId;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getCodeDate() {
        return codeDate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeDate(LocalDate codeDate) {
        this.codeDate = codeDate;
    }

    public LocalTime getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(LocalTime codeTime) {
        this.codeTime = codeTime;
    }


    public String getUuidCode() {
        return uuidCode;
    }

    public void setUuidCode(String uuidCode) {
        this.uuidCode = uuidCode;
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

    public boolean isSecretCodeTime() {
        return secretCodeTime;
    }

    public void setSecretCodeTime(boolean secretCodeTime) {
        this.secretCodeTime = secretCodeTime;
    }

    public boolean isSecretCodeViews() {
        return secretCodeViews;
    }

    public void setSecretCodeViews(boolean secretCodeViews) {
        this.secretCodeViews = secretCodeViews;
    }

    static String uuIdGenerate() {

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        return randomUUIDString;
    }
}

