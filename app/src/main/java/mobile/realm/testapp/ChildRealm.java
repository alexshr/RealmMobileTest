package mobile.realm.testapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;

public class ChildRealm extends RealmObject {

    private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //@PrimaryKey
    private int id;

    String date="test_date";
    String name="test_name";

    public ChildRealm(int id) {
        this();
        this.id = id;
    }

    public ChildRealm() {
        date=sFormat.format(new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ChildRealm{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
