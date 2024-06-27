package express;

public class Express {
    private String waybillNumber;
    private String waybillType;
    private Sender sender;
    private Receiver receiver;
    private Boolean isSign;
    private Boolean isDifficult;
    private String difficultReason;
    private double amount;


    public Express() {
        sender = new Sender();
        receiver = new Receiver();
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public String getWaybillType() {
        return waybillType;
    }

    public Sender getSender() {
        return sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public Boolean getIsSign() {
        return isSign;
    }

    public Boolean getIsDifficult() {
        return isDifficult;
    }

    public String getDifficultReason() {
        return difficultReason;
    }

    public double getAmount() {
        return amount;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public void setWaybillType(String waybillType) {
        this.waybillType = waybillType;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    public void setIsDifficult(Boolean isDifficult) {
        this.isDifficult = isDifficult;
    }

    public void setDifficultReason(String difficultReason) {
        this.difficultReason = difficultReason;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
