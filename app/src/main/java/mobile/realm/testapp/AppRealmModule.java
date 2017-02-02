package mobile.realm.testapp;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {ParentRealm.class,ChildRealm.class})
public class AppRealmModule {
}
