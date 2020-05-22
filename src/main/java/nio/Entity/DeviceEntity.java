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

    /** 设备尺寸. */
    @Column("overall_dimension")
    private String overallDimension;

    /** 设备尺寸. */
    @Column("IOT_code")
    private String IOTCode;

    public String getIOTCode() {
        return IOTCode;
    }

    public void setIOTCode(String IOTCode) {
        this.IOTCode = IOTCode;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

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

    /** 绑定泵房档案编号. */
    @IgnoreColumn
    private String projectName;

    @IgnoreColumn
    private String typeName;

    @IgnoreColumn
    private String modelName;

    @IgnoreColumn
    private String editionName;

}
