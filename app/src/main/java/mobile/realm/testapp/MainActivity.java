package mobile.realm.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncSession;
import io.realm.SyncUser;

public class MainActivity extends AppCompatActivity {

    public static final String REALM_USER = "alexshr@yandex.ru";
    public static final String REALM_PASSWORD = "commport";
    public static final String REALM_AUTH_URL = "http://192.168.1.59:9080/auth";
    public static final String REALM_DB_URL = "realm://192.168.1.59:9080/~/default";

    private static String TAG = "APP_LOG";

    private static SyncCredentials sCredentials = SyncCredentials.usernamePassword(REALM_USER,
            REALM_PASSWORD, false);


    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private SyncConfiguration mSyncConfig;

    @OnClick(R.id.button)
    void submit() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mTestRealm.setName(mEditText.getText().toString());
            }
        });
    }

    Realm mRealm;

    TestRealm mTestRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.VISIBLE);
        Realm.init(this);
        SyncUser.loginAsync(sCredentials, REALM_AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                Log.d(TAG, "loginRealmServer onSuccess");
                mProgressBar.setVisibility(View.INVISIBLE);
                mSyncConfig = new SyncConfiguration.Builder(user, REALM_DB_URL)
                        .initialData(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.createObject(TestRealm.class, 1);
                                Log.d(TAG, "initData called");
                            }
                        })
                        .modules(new AppRealmModule())
                        .name("test2")
                        .errorHandler(new SyncSession.ErrorHandler() {
                            @Override
                            public void onError(SyncSession session, ObjectServerError error) {
                                Log.e(TAG, "SyncSession errorHandler: error=" + error + " session" + session);
                            }
                        })
                        .build();

                // This will automatically sync all changes in the background for as long as the Realm is open
                mRealm = Realm.getInstance(mSyncConfig);

                //startSessionSupportProcess();

                mProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "TestRealm count=" + mRealm.where(TestRealm.class).count());

                mTestRealm = mRealm.where(TestRealm.class).findFirst();
                mTextView.setText("init: " + mTestRealm.getName());

                mTestRealm.addChangeListener(new RealmChangeListener<RealmModel>() {
                    @Override
                    public void onChange(RealmModel element) {
                        Log.d(TAG, "onChange element=" + element);
                        mTextView.setText(((TestRealm) element).getName());
                    }
                });
            }

            @Override
            public void onError(ObjectServerError error) {
                Log.e(TAG, "loginRealmServer onError", error);
            }
        });
    }
/* выдает /bad request!!!
    private void refreshUser() {
        SyncUser user = SyncUser.currentUser();

        if (user != null) {
            String userStr = user.toJson();
            try {
                JSONObject obj = new JSONObject(userStr);
                URL authUrl = new URL(obj.getString("authUrl"));
                Token userToken = Token.from(obj.getJSONObject("userToken"));
                Log.d(TAG, "before refreshUser userToken=" + userToken + " authUrl=" + authUrl);
                AuthenticateResponse resp = SyncManager.getAuthServer().refreshUser(userToken, authUrl);//bad request!!!
                Log.d(TAG, "after refreshUser AuthenticateResponse=" + resp+" accessToken="+resp.getAccessToken()+" refreshToken="+resp.getRefreshToken());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

    }
*/
    //async
    private void startSessionSupportProcess() {
        Log.d(TAG, "userSessionSupportProcess started");
        Observable.interval(20, 20, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(TestRealm.class).count();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
