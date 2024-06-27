package express;

public class Time {
    int year;
    int month;
    int day;

    public Time() {
        year = 0;
        month = 0;
        day = 0;
    }
    public Time(int i, int i1, int i2) {
        this.year = i;
        this.month = i1;
        this.day = i2;
    }

    public void setTime(Time time) {
        this.year = time.year;
        this.month = time.month;
        this.day = time.day;
    }

    public String getTime() {
        return year + "-" + month + "-" + day;
    }

    public int compare(Time time) {
        if (this.year > time.year) {
            return 1;
        } else if (this.year < time.year) {
            return -1;
        } else {
            if (this.month > time.month) {
                return 1;
            } else if (this.month < time.month) {
                return -1;
            } else {
                if (this.day > time.day) {
                    return 1;
                } else if (this.day < time.day) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
