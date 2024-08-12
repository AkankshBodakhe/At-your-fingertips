package Model;

public class User {
    String userName, email,password,lastmessage;

    public User(String userName, String email, String password, String lastmessage) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.lastmessage = lastmessage;
    }

    public User(){}

    //Sign Up constructor
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }
}
