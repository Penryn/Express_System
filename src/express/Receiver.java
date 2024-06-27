package express;

public class Receiver extends People {

    public Receiver() {
        time = new Time();
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getRemark() {
        return remark;
    }

    public Time getTime() {
        return time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
