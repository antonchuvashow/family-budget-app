package antonchuvashov.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private SimpleStringProperty username;
    private String passwordHash;
    private SimpleStringProperty fullName;
    private SimpleObjectProperty<Date> birthday;
    private BigDecimal sum = BigDecimal.ZERO;

    public User(String username, String passwordHash, String fullName, Date birthday) {
        this.username = new SimpleStringProperty(username);
        this.passwordHash = passwordHash;
        this.fullName = new SimpleStringProperty(fullName);
        this.birthday = new SimpleObjectProperty<>(birthday);
    }

    public String getUsername() {
        return username.getValue();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public String getFullName() {
        return fullName.getValue();
    }

    public void setFullName(String fullName) {
        this.fullName = new SimpleStringProperty(fullName);
    }

    public Date getBirthday() {return this.birthday.getValue();}
    public void setBirthday(Date birthday) {this.birthday = new SimpleObjectProperty<>(birthday);}

    public BigDecimal getSum() {return this.sum;}
    public void setSum(BigDecimal sum) {this.sum = sum;}

    public ObservableValue<String> usernameProperty() {
        return this.username;
    }

    public ObservableValue<String> fullNameProperty() {
        return this.fullName;
    }


    public ObservableValue<String> birthdayProperty() {
        return this.birthday.asString();
    }
}
