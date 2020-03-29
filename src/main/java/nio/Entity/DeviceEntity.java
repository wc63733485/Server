package nio.Entity;

import com.sws.base.annotations.Column;
import com.sws.base.annotations.Entity;
import com.sws.base.annotations.Id;
import com.sws.base.annotations.IgnoreColumn;


@Entity("device")
public class DeviceEntity {

    @Id
    private Integer id;

    /** 设备编号. */
    private String code;

    /** SIM卡号. */
    @Column("sim_code")
    private String simCode;

    /** 设备类型. */
    @Column("device_type_id")
    private Integer deviceTypeId;

    /** 在线状况. */
    @IgnoreColumn
    private String onlineStatus;

    /** 设备型号. */
    @Column("device_model_id")
    private Integer deviceModelId;

    /** 设备版本. */
    @Column("device_edition_id")
    private Integer deviceEditionId;

    /** 设备尺寸. */
    @Column("overall_dimension")
    private String overallDimension;

    /** 产品标准号. */
    @Column("standard_no")
    private String standardNo;


    /** 绑定日期. */
    @Column("bind_date")
    private String bindDate;

    /** PLC型号. */
    @Column("plc_type")
    private String plcType;

    /** plc密码. */
    @Column("plc_pwd")
    private String plcPwd;

    /** 绑定泵房档案编号. */
    @Column("bind_pumpHouse_code")
    private String bindPumpHouseCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(Integer deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public Integer getDeviceEditionId() {
        return deviceEditionId;
    }

    public void setDeviceEditionId(Integer deviceEditionId) {
        this.deviceEditionId = deviceEditionId;
    }

    public String getOverallDimension() {
        return overallDimension;
    }

    public void setOverallDimension(String overallDimension) {
        this.overallDimension = overallDimension;
    }

    public String getStandardNo() {
        return standardNo;
    }

    public void setStandardNo(String standardNo) {
        this.standardNo = standardNo;
    }

    public String getBindDate() {
        return bindDate;
    }

    public void setBindDate(String bindDate) {
        this.bindDate = bindDate;
    }

    public String getPlcType() {
        return plcType;
    }

    public void setPlcType(String plcType) {
        this.plcType = plcType;
    }

    public String getPlcPwd() {
        return plcPwd;
    }

    public void setPlcPwd(String plcPwd) {
        this.plcPwd = plcPwd;
    }

    public String getBindPumpHouseCode() {
        return bindPumpHouseCode;
    }

    public void setBindPumpHouseCode(String bindPumpHouseCode) {
        this.bindPumpHouseCode = bindPumpHouseCode;
    }
}
