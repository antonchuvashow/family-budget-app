package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private String username;
    private String password;
    private String fullName;
    private Date birthday;
    private BigDecimal sum;

    public User(String username, String password, String fullName, Date birthday, BigDecimal sum) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthday = birthday;
        this.sum = sum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthday() {return this.birthday;}
    public void setBirthday(Date birthday) {this.birthday = birthday;}

    public BigDecimal getSum() {return this.sum;}
    public void setSum(BigDecimal sum) {this.sum = sum;}
}
