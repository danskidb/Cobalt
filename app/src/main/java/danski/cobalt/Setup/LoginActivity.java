package danski.cobalt.Setup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import danski.cobalt.Defines;
import danski.cobalt.R;

/**
 * Created by Danny on 19/02/2016.
 */
public class LoginActivity extends AppCompatActivity {

    // The string will appear to the user in the login screen
    // you can put your app's name
    final String REALM_PARAM = "Cobalt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                //checks the url being loaded
                //setTitle(url);
                Uri Url = Uri.parse(url);

                setTitle(getString(R.string.title_login));

                if (Url.getAuthority().equals(REALM_PARAM.toLowerCase())) {
                    // That means that authentication is finished and the url contains user's id.
                    webView.stopLoading();

                    // Extracts user id.
                    Uri userAccountUrl = Uri.parse(Url.getQueryParameter("openid.identity"));
                    String userId = userAccountUrl.getLastPathSegment();
                    Defines.mysteamid = userId;

                    // Do whatever you want with the user's steam id
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.putLong("steamid64", Long.parseLong(userId));
                    editor.putBoolean("loggedin", true);
                    editor.apply();


                    Intent i = new Intent(Defines.CurrentContext, SetupWizard.class);
                    i.putExtra("openid", userId);
                    startActivity(i);
                    finish();
                }
            }
        });
        setContentView(webView);

        // Constructing openid url request
        String url = "https://steamcommunity.com/openid/login?" +
                "openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.identity=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.mode=checkid_setup&" +
                "openid.ns=http://specs.openid.net/auth/2.0&" +
                "openid.realm=https://" + REALM_PARAM + "&" +
                "openid.return_to=https://" + REALM_PARAM + "/signin/";

        webView.loadUrl(url);
    }
}