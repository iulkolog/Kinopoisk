package objects;


import java.io.Serializable;

/**
 * Created by Iulkolog on 19.05.2016.
 */

@TableInfo(tableName = "objects.User", primaryKey = "user_id")
public class User implements Serializable {
    @FieldInfo(fieldName = "user_id", isPrimaryKey = true)
    private int userId = 0;
    @FieldInfo(fieldName = "login")
    private String login;
    @FieldInfo(fieldName = "first_name")
    private String firstName;
    @FieldInfo(fieldName = "last_name")
    private String lastName;
    @FieldInfo(fieldName = "email")
    private String email;
    private int age;

    public User(){};

    //setters and getters
    public void setUserId(int userId){
        this.userId = userId;
    }
    public long getUserId(){
        return userId;
    }

    public void setLogin(String login){
        this.login = login;
    }
    public String getLogin(){
        return login;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){

        this.lastName = lastName;
    }
    public String getLastName(){
        return lastName;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (!login.equals(user.login)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        return email != null ? email.equals(user.email) : user.email == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + login.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
