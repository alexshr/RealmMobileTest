package mobile.realm.testapp;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ParentRealm extends RealmObject {

    RealmList<ChildRealm> childs=new RealmList<>();

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

    public void addChild(Realm realm){
        ChildRealm child=realm.copyToRealm(new ChildRealm(childs.size()));
        childs.add(child);
    }

    public void delChild(){
        if(childs.size()>0) {
            ChildRealm child=childs.get(childs.size() - 1);
            childs.remove(childs.size() - 1);
            child.deleteFromRealm();
        }
    }

    @Override
    public String toString() {
        StringBuilder res=new StringBuilder("ParentRealm{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                "} childs:\n");

        for(ChildRealm child:childs){
           res.append(child+"\n");
        }

        return res+"";
    }
}
