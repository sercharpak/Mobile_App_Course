package co.edu.uniandes.recetasefectivas;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends Activity {

    private static final int SPEECH_REQUEST_CODE = 0;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

            }
        });
        initializeGoogleApiClient();
    }
    private void initializeGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));
        super.onResume();
    }

    // Handler que recibe Intents for the "product" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i2= new Intent(context, ProcessProduct.class);
            i2.putExtra(Constants.ARGUMENTS, intent.getStringExtra(Constants.ARGUMENTS));
            startActivity(i2);
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }


    // Create an intent that can start the Speech Recognizer activity
    public void start(View target) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.d("DEBUG", "Google speech result: " + spokenText);
            if(spokenText.equalsIgnoreCase(Constants.ACTION_BUY_NOW))
            {
                try {
                    JSONObject json = new JSONObject();
                    json.put("action", Constants.ACTION_BUY_NOW);
                    Log.e("ATENCION", "Enviando-> ");
                    new SendMessage().execute(json.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong",
                            Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Dijiste: " + spokenText +
                        ". Prueba \"comprar ahora\".",
                        Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SendMessage extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... msgs) {
            String message= msgs[0];
            Log.e("ATENCION", "Enviendo-> " + message);
            for(String id: getNodes()) {
                MessageApi.SendMessageResult sendMessageResult=Wearable.MessageApi.sendMessage(mGoogleApiClient, id,
                        Constants.PATH, message.getBytes()).await();
                com.google.android.gms.common.api.Status status = sendMessageResult.getStatus();

                if (status.getStatusCode() == WearableStatusCodes.SUCCESS) {
                    Log.d("Wear", "El destinatario (mobile) recibió el mensaje");
                }
            }

            return "Enviado";
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Elemento enviado",
                    Toast.LENGTH_LONG).show();
        }

        private Collection<String> getNodes() {
            HashSet<String> results = new HashSet<String>();
            NodeApi.GetConnectedNodesResult nodes =
                    Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                results.add(node.getId());
            }
            return results;
        }
    }




}
