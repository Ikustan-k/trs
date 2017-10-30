package com.example.user.tdbs;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CheckPoint1 extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    //ProgressDialog mProgress;

    private static TextView res_tex = null;
    private static TextView res_tex2 = null;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String BUTTON_TEXT2 = "Call QRcode reader";
    private static final String BUTTON_TEXT3 = "Call QRcode reader";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };
    private int vide_num;
    private static int rIndex = 0;
    private static int cIndex = 0;
    private static int num1_index = 0;
    private static int num2_index = 0;
    private static int num3_index = 0;
    private static int num4_index = 0;
    private static int num_final;
    private static String result_t;
    private static String res_vid_num = "000";
    public String res_out1 = "0";
    public String res_out2 = "0";
    public String res_out3 = "0";
    public String res_out4 = "0";
    public String res_out0 = "0";
    public int c1_gen_data = 0;
    //↑スコープがサンプルのままだと閲覧だけで書き込めない
    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


        final Button button = (Button)findViewById(R.id.ent_button);
        final Button num1 = (Button)findViewById(R.id.num1);
        final Button num2 = (Button)findViewById(R.id.num2);
        final Button num3 = (Button)findViewById(R.id.num3);
        final Button num4 = (Button)findViewById(R.id.num4);
        final Button num5 = (Button)findViewById(R.id.num5);

        final TextView res_tex =(TextView)findViewById(R.id.textView);
        TextView res_tex2 =(TextView)findViewById(R.id.textView2);

        //res_tex.setText("aaa");
        //res_tex2.setText("sss");
        ////レイアウトのボタンと関連付け

        final VideoView videoView = (VideoView) findViewById(R.id.video);
        //videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video1));
        //videoView.setVideoURI(Uri.parse("android.resource://com.example.user.tdbs/raw/video1"));
        videoView.setVideoPath("/sdcard/TRS/video1.mp4");
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setNextVideo();
                videoView.start();
            }
        });
        /////動画終了のリスナー
        /////終了で次動画再生のメソッド呼び出し

        button.setText("C1:API同期");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertVideo();
                //gen_random();
                getResultsFromApi();
                //gen_random();
                //select_video();
            }
        });

        //num1.setText("insert a movie and call Sheets API");
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1_index++;
                if(num1_index == 10)
                    num1_index = 0;
                num1.setText(Integer.toString(num1_index));

            }
        });
        //num1.setText("insert a movie and call Sheets API");
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num2_index++;
                if(num2_index == 10)
                    num2_index = 0;
                num2.setText(Integer.toString(num2_index));
            }
        });
        //num1.setText("insert a movie and call Sheets API");
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num3_index++;
                if(num3_index == 10)
                    num3_index = 0;
                num3.setText(Integer.toString(num3_index));
            }
        });
        num4.setText("ID選択");
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num_final = (num1_index*100)
                        +   (num2_index*10)
                        +   (num3_index);
                addIDdata();
                //goal_video();
                //num4.setText(Integer.toString(num_final));
            }
        });
        num5.setText("動画再生");
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gen_random();
                select_video();
            }
        });


    }

    private void gen_random() {
        Random rnd = new Random();
        c1_gen_data = rnd.nextInt(2)+1;
    }

    private void getDataInt(){
        //final TextView res_tex3 =(TextView)findViewById(R.id.textView3);
        //res_tex3.setText(Integer.parseInt(res_out1)+","+
        //                 Integer.parseInt(res_out2)+","+
        //                 Integer.parseInt(res_out3)+","+
        //                 Integer.parseInt(res_out4)+",");
    }

    private void insertVideo(){
        final VideoView videoView = (VideoView) findViewById(R.id.video);
        videoView.stopPlayback();
        videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" +R.raw.video_chousei));
        videoView.start();
    }
    private void select_video(){
        TextView res_tex2 =(TextView)findViewById(R.id.textView2);
        final VideoView videoView = (VideoView) findViewById(R.id.video);
        if(res_out0 != null){
            videoView.stopPlayback();
            res_vid_num = c1_gen_data+"0"+"0";
            if(res_vid_num != "000") {
                //videoView.setVideoURI(Uri.parse("android.resource://com.example.user.tdbs/raw/video" + res_vid_num));
                videoView.setVideoPath("/sdcard/TRS/video"+res_vid_num+".mp4");
                res_tex2.setText("movie:" + res_vid_num + " running now");
                videoView.start();
            }
        }
    }

    private void addIDdata(){
        Random rnd = new Random();
        c1_gen_data = rnd.nextInt(3)+1;
        TextView res_tex2 =(TextView)findViewById(R.id.textView2);
        res_tex2.setText("選択中　>>>　ID:"+num_final+"\n"+
                         "乱数　　>>>　　 "+c1_gen_data+"\n");
    }

    private void setNextVideo()
    {
        final VideoView videoView = (VideoView) findViewById(R.id.video);
        //videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" +R.raw.video1));
        //videoView.setVideoURI(Uri.parse("android.resource://com.example.user.tdbs/raw/video1"));
        videoView.setVideoPath("/sdcard/TRS/video1.mp4");
        videoView.start();
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            //res_tex.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    //mOutputText.setText(
                    //        "This app requires Google Play Services. Please install " +
                    //                "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CheckPoint1.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        TextView res_tex1 =(TextView)findViewById(R.id.textView);
        //TextView res_tex2 =(TextView)findViewById(R.id.textView2);
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;
        //public String res_out1;
        //public String res_out2;
        //public String res_out3;
        //public String res_out4;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                putDataFromApi();
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            //res_tex2.setText("called");
            String spreadsheetId = "spreadsheet_id";
            //String range = "Class Data!A1:F5";
            //String range = "A1:G";
            String range = ("A"+(num_final)+":Z"+(num_final));
            List<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                //results.add("C1,C2,C3,");
                for (List row : values) {
                    results.add("Num:"+String.valueOf(row.get(0))
                            +" C1:"+String.valueOf(row.get(1))
                            +" C2:"+String.valueOf(row.get(3))
                            +" C3:"+String.valueOf(row.get(5))
                            +" DK:"+String.valueOf(row.get(7)));
                }
            }
            if (values != null) {
                //results.add("C1,C2,C3,");
                for (List row : values) {
                    res_out0 = String.valueOf(row.get(0));
                    res_out1 = String.valueOf(row.get(1));
                    res_out2 = String.valueOf(row.get(3));
                    res_out3 = String.valueOf(row.get(5));
                    res_out4 = String.valueOf(row.get(7));
                }
            }
            //for(int i =0; i<5; i++){
            //    result_out[i] = Integer.parseInt(String.valueOf(results.get(i)));
                //res_tex3.setText(res_out1+res_out2);
            //}
            //result_t = String.ValueOf(results.get(0));
            //res_tex2.setText("ok");
            return results;
        }



        @Override
        protected void onPreExecute() {
s            //mOutputText.setText("");
            //mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            //mProgress.hide();
            if (output == null || output.size() == 0) {
                res_tex1.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Sheets API:");
                res_tex1.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            //mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CheckPoint1.REQUEST_AUTHORIZATION);
                } else {
                    res_tex1.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                res_tex1.setText("Request cancelled.");
            }
        }

        private void putDataFromApi() throws IOException {
            Date date = new Date();
            String spreadsheetId = "spreadsheet_id";
            //String range = "A1:D";
            //String range = ("A"+cIndex+":D"+rIndex);
            String range = ("B"+num_final+":C"+num_final);
            ValueRange valueRange = new ValueRange();
            List row = new ArrayList<>();
            List col = Arrays.asList(c1_gen_data,date.toString());
            row.add(col);
            valueRange.setValues(row);
            valueRange.setRange(range);
            this.mService.spreadsheets().values()
                    .update(spreadsheetId, range, valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        }
    }
}
