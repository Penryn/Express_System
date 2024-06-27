package express;



public abstract class People {
    String address;
    String phone;
    String remark;
    Time time;

    public abstract String getAddress();

    public abstract String getPhone();

    public abstract String getRemark();

    public abstract Time getTime();

    public abstract void setAddress(String address);

    public abstract void setPhone(String phone);

    public abstract void setRemark(String remark);

    public abstract void setTime(Time time);

}


