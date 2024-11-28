package antonchuvashov.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private SimpleStringProperty username;
    private String password;
    private SimpleStringProperty fullName;
    private SimpleObjectProperty<Date> birthday;
    private BigDecimal sum = BigDecimal.ZERO;

    public User(String username, String password, String fullName, Date birthday) {
        this.username = new SimpleStringProperty(username);
        this.password = password;
        this.fullName = new SimpleStringProperty(fullName);
        this.birthday = new SimpleObjectProperty<>(birthday);
    }

    public String getUsername() {
        return username.getValue();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
