package pwr.android_app.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceIdService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pwr.android_app.R;
import pwr.android_app.dataStructures.UserData;
import pwr.android_app.network.notifications.MyFirebaseInstanceIDService;
import pwr.android_app.network.rest.ApiService;
import pwr.android_app.network.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestingFragment extends Fragment {

    /* ========================================== DATA ========================================== */
    // Used in REST requests
    private ApiService client = null;

    // UI references
    @BindView(R.id.test_json_view)
    TextView testJsonView;

    /* ========================================= METHODS ======================================== */
    // --- CONSTRUCTOR --- //
    public TestingFragment() {
        // Required empty public constructor
    }

    // --- ON CREATE VIEW --- //
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_testing, container, false);

        ButterKnife.bind(this, view);

        // [Retrofit]
        this.client = ServiceGenerator.createService(ApiService.class);

        // Inflate the layout for this fragment
        return view;
    }

    /* ======================================== LISTENERS ======================================= */
    @OnClick(R.id.clear_cookies_button)
    void clearCookies() {
        SharedPreferences preferences = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookie", null);
        editor.commit();
    }

    @OnClick(R.id.test_json_button)
    void TestJson() {
        Call<UserData> call = client.doTestJson();

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                testJsonView.setText(response.body().getUserEmail());
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                testJsonView.setText("ERROR");
            }
        });
    }
}