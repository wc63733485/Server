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

    @Column("reference_range")
    private String referenceRange;

    @Column("warn_number")
    private String warnNumber;

    @Column("device_model_id")
    private Integer deviceModelId;

    @Column("device_type_id")
    private Integer deviceTypeId;

    @Column("device_edition_id")
    private Integer deviceEditionId;

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

    public String getWarnNumber() {
        return warnNumber;
    }

    public void setWarnNumber(String warnNumber) {
        this.warnNumber = warnNumber;
    }

    public Integer getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(Integer deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Integer getDeviceEditionId() {
        return deviceEditionId;
    }

    public void setDeviceEditionId(Integer deviceEditionId) {
        this.deviceEditionId = deviceEditionId;
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
