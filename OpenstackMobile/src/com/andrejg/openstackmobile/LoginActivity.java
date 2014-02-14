package com.andrejg.openstackmobile;



import com.andrejg.openstackmobile.openstack.OpenStackLogin;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well. 
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;
	private String mUrl;
	private String mTenant;
	private String mTenantId;
	private String mCurrentConnection;
	private String mNotificationsUrl;
	
	// Token and nova url
	private String isConnToken;
	private String isNovaURL;
	private String isCinderURL;
	
	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private EditText mUrlView;
	private EditText mTenantView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private EditText mNotificationsUrlView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		setupActionBar();

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.username);
		mPasswordView = (EditText) findViewById(R.id.password);
		mUrlView	  = (EditText) findViewById(R.id.openstackurl);	
		mTenantView   = (EditText) findViewById(R.id.tenant);
		mNotificationsUrlView = (EditText) findViewById(R.id.notificationsurl);
		
				
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("CONNECTION")) {
				mCurrentConnection = extras.getString("CONNECTION");
				mUrl = extras.getString("URL");
				mUsername = extras.getString("USERNAME");
				mPassword = extras.getString("PASSWORD");
				mTenant = extras.getString("TENANT");
				mTenantId = extras.getString("TENANTID");		
				mNotificationsUrl = extras.getString("NOTIFICATIONSURL");
				
				mUsernameView.setText(mUsername);
				mPasswordView.setText(mPassword);
				mUrlView.setText(mUrl);
				mTenantView.setText(mTenant);
				mNotificationsUrlView.setText(mNotificationsUrl);
			}
		}
		
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		findViewById(R.id.delete_conn_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						deleteConn();
					}
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void deleteConn() {
		ConnectionData.DeleteConnectionData(this,mCurrentConnection);			
		Intent returnIntent = new Intent();
		setResult(10, returnIntent);        
		this.finish();
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);
		mUrlView.setError(null);
		mTenantView.setError(null);
		mNotificationsUrlView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mUrl = mUrlView.getText().toString();
		mTenant = mTenantView.getText().toString();		
		mPassword = mPasswordView.getText().toString();
		mNotificationsUrl =  mNotificationsUrlView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} 
		// Check for a valid username
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		} 
		// Check for a valid tenant
		if (TextUtils.isEmpty(mTenant)) {
			mTenantView.setError(getString(R.string.error_field_required));
			focusView = mTenantView;
			cancel = true;
		} 		
		// Check for a valid url
		if (TextUtils.isEmpty(mUrl)) {
			mUrlView.setError(getString(R.string.error_field_required));
			focusView = mUrlView;
			cancel = true;
		} 			
		

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask(this);
			mAuthTask.execute((Void) null);
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
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {*/
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		//}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
				
		Activity ioActivity;
		
		public UserLoginTask(Activity aoActivity) {
			ioActivity = 	aoActivity;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			String lsToken;
			String[] lsTokenTenantResult;
			
			try {
				// dobimo tenant ID
				if (TextUtils.isEmpty(mTenantId)) {
					lsToken  = OpenStackLogin.GetToken(mUsername,mPassword,mUrl);
					mTenantId = OpenStackLogin.GetTenantId(lsToken,mTenant,mUrl);
				}
				// prijavmo se s tenantom
				lsTokenTenantResult = OpenStackLogin.GetTokenTenant(mUsername,mPassword,mTenantId,mUrl);
			} catch (Exception e) {
				//Log.e("OM", "Exception when loggin in" , e);
				return false;			
			}
			
			//Log.i("OM", "Login successful. Token: " + lsTokenTenantResult[0]);		
			
			isConnToken =lsTokenTenantResult[0];
			isNovaURL = lsTokenTenantResult[1];
			isCinderURL = lsTokenTenantResult[2];
			
			// save Url,Username,Password,Tenant,TenantId to preferences
			SaveLogin(mCurrentConnection,mUrl,mUsername,mPassword,mTenant,mTenantId, mNotificationsUrl);

			return true;
		}

		private void SaveLogin(String asCurrentConnection, String asUrl, String asUsername, String asPassword,
				String asTenant, String asTenantId, String asNotificationsUrl) {
			
			ConnectionData.SaveConnectionData(asCurrentConnection, asUrl, asUsername, asPassword, asTenant, asTenantId, asNotificationsUrl, ioActivity);				
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				CommonUtilities.SaveCurrentConn(isConnToken,isNovaURL,isCinderURL, mUsernameView.getContext());
				// go to activity with instances
                Intent myIntent = new Intent(mUsernameView.getContext(), OpenStack.class);                
                startActivity(myIntent);
				
				
			} else {
				mUrlView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
