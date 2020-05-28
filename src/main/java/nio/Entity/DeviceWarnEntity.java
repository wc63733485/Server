package nio.Entity;

import com.sws.base.annotations.Column;
import com.sws.base.annotations.Entity;
import com.sws.base.annotations.Id;


@Entity("device_warn")
public class DeviceWarnEntity {

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private String source;

    private String remake;

    private Double number;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public Double getWarnTimeInterval() {
        return warnTimeInterval;
    }

    public void setWarnTimeInterval(Double warnTimeInterval) {
        this.warnTimeInterval = warnTimeInterval;
    }

}
