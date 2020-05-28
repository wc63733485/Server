package nio.Entity;


import com.sws.base.annotations.Column;
import com.sws.base.annotations.Entity;
import com.sws.base.annotations.Id;

@Entity("device_type_unit")
public class DeviceTypeBindEntity {

    @Id
    private Integer id;

    @Column("device_type_id")
    private Integer deviceTypeId;

    @Column("device_unit_id")
    private Integer deviceUnitId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Integer getDeviceUnitId() {
        return deviceUnitId;
    }

    public void setDeviceUnitId(Integer deviceUnitId) {
        this.deviceUnitId = deviceUnitId;
    }
}
