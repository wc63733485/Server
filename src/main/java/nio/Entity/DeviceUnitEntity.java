package nio.Entity;

import com.sws.base.annotations.Column;
import com.sws.base.annotations.Entity;
import com.sws.base.annotations.Id;
import com.sws.base.annotations.IgnoreColumn;


@Entity("device_unit")
public class DeviceUnitEntity {

    @Id
    private Integer id;

    /** 设备编号. */
    private String name;

    private String unit;

    @Column("is_onoff_data")
    private String isOnOffData;

    @Column("is_number_data")
    private String isNumberData;

    @Column("is_alarm_data")
    private String isAlarmData;

    public String getIsAlarmData() {
        return isAlarmData;
    }

    public void setIsAlarmData(String isAlarmData) {
        this.isAlarmData = isAlarmData;
    }

    @Column("reference_range")
    private String referenceRange;


    @Column("data_name")
    private String dataName;

    @Column("message_number")
    private String messageNumber;

    public Integer getMove() {
        return move;
    }

    public void setMove(Integer move) {
        this.move = move;
    }

    @Column("move")
    private Integer move;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsOnOffData() {
        return isOnOffData;
    }

    public void setIsOnOffData(String isOnOffData) {
        this.isOnOffData = isOnOffData;
    }

    public String getIsNumberData() {
        return isNumberData;
    }

    public void setIsNumberData(String isNumberData) {
        this.isNumberData = isNumberData;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }
}
