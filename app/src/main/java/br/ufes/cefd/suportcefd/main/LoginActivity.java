package br.ufes.cefd.suportcefd.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.utils.Util;
import br.ufes.cefd.suportcefd.webservice.AccessServiceAPI;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences configPrefs;
    Person person;
    String email;
    String password;
    boolean remember;
    CheckBox ch_remember;
    boolean logged = false;
    boolean neterror = false;
    static int RESULT_NEW_USER = 1;
    private AccessServiceAPI m_AccessServiceAPI;
    private AlertDialog connError;
    String wsUrl;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        prefs = getSharedPreferences(getString(R.string.sp_user), Context.MODE_PRIVATE);
        configPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the login form.
        setUpLoginForm();
    }

    private void setUpLoginForm() {
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        connError = Util.showDialog(this, "Erro de conexão!", "Não foi possivel conectar ao servidor!");
    }

    protected void restoreInfo() {
        email = prefs.getString(getString(R.string.sp_email), "");
        password = prefs.getString(getString(R.string.sp_password), "");

        remember = prefs.getBoolean(getString(R.string.sp_remember), false);
        logged = prefs.getBoolean(getString(R.string.sp_logged), false);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        ch_remember = (CheckBox) findViewById(R.id.ch_remember);
        ch_remember.setChecked(remember);

        if (email != null) mEmailView.setText(email);

        if (ch_remember.isChecked() && password != null) mPasswordView.setText(password);
    }

    protected void setRemember() {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(getString(R.string.sp_remember), ch_remember.isChecked());
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreInfo();

        if (remember && logged) {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(LoginActivity.this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            setRemember();
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }


    public void newUser(View v) {

        Intent it = new Intent(LoginActivity.this, NewUser.class);
        startActivityForResult(it, RESULT_NEW_USER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_NEW_USER) {
            if (resultCode == RESULT_OK) {
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("email", data.getExtras().getString("email"));
                ed.putString("password", "");
                ed.putBoolean("logged", false);
                ed.commit();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void startMain() {
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        it.putExtra(getString(R.string.sp_person), person);
        startActivity(it);
        finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getperson");
            postParam.put("email", mEmail);

            String url = configPrefs.getString("webservice","");

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(url, postParam);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonArray != null) {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                    person = new Person(jsonObject);
                    if (person != null) {
                        return person.getPassword().equals(mPassword);
                    }
                }
            } catch (java.net.ConnectException | IllegalArgumentException e) {
                neterror = true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString(getString(R.string.sp_email), mEmail);
                ed.putString(getString(R.string.sp_password), mPassword);
                ed.putBoolean(getString(R.string.sp_logged), true);
                ed.commit();
                startMain();

            } else if (neterror) {
                //Toast.makeText(getBaseContext(), getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                connError.show();

            } else {
                mEmailView.setError(getString(R.string.error_invalid_email_or_password));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

