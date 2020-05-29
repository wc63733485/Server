package nio.Entity;

import com.sws.base.annotations.Column;
import com.sws.base.annotations.Entity;
import com.sws.base.annotations.Id;


@Entity("device_warn_log")
public class DeviceWarnLogEntity {

    @Id
    private Integer id;

    @Column("data_name")
    private String dataName;

    @Column("IOT_code")
    private String IOTCode;

    private String level;

    @Column("warn_time_interval")
    private Double warnTimeInterval;

    private String sign;


    private String source;

    private String remake;

    private Double number;

    @Column("warn_number")
    private Double warnNumber;

    private String Date;

    private String time;

    private int status;

    public Double getWarnNumber() {
        return warnNumber;
    }

    public void setWarnNumber(Double warnNumber) {
        this.warnNumber = warnNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getIOTCode() {
        return IOTCode;
    }

    public void setIOTCode(String IOTCode) {
        this.IOTCode = IOTCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getWarnTimeInterval() {
        return warnTimeInterval;
    }

    public void setWarnTimeInterval(Double warnTimeInterval) {
        this.warnTimeInterval = warnTimeInterval;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }
}
