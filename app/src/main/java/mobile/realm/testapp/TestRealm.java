package mobile.realm.testapp;

//import com.skb.goodsapp.data.storage.dto.ProductRealm11;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TestRealm extends RealmObject {

    @PrimaryKey
    private long id;
    String date="test_date";
    String name="test_name";

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
        return "TestRealm{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
