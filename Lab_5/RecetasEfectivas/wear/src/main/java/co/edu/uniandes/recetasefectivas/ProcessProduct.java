package co.edu.uniandes.recetasefectivas;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ProcessProduct extends Activity {

    private TextView name_tv;
    private TextView price_tv;
    private Button bought_btn;
    private Button skip_btn;
    private Button missing_btn;

    private String current_id;
    private WatchViewStub stub;
    public String request;
    private String full_data;
    private GoogleApiClient mGoogleApiClient;

    //ToReview
    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_product);

        //ToReview
        // Obtain the DismissOverlayView element
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                name_tv = (TextView) stub.findViewById(R.id.nameTxt);
                price_tv = (TextView) stub.findViewById(R.id.priceTxt);

                //buttons
                bought_btn = (Button) stub.findViewById(R.id.boughtBtn);
                skip_btn = (Button) stub.findViewById(R.id.skipBtn);
                missing_btn = (Button) stub.findViewById(R.id.missingBtn);

                Bundle bundle = getIntent().getExtras();
                full_data = bundle.getString(Constants.ARGUMENTS);

                try {
                    processReceivedData(new JSONObject(full_data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //ToReview
        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent event) {

                Log.d("Motion", " onLongPress: " + event.toString());
            }


            @Override
            public boolean onDown(MotionEvent event) {
                Log.d("Motion"," onDown: " + event.toString());
                return true;
            }
            //ToReview
            //Manejador de los swipes up y down
            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2,
                                   float velocityX, float velocityY) {
                Log.d("Motion", " onFling: " + event1.toString()+event2.toString());

                float dY = event1.getY()-event2.getY();
                if (dY>0) {
                    Log.d("Motion", " onFling: UP SWIPE, COMPRADO");
                    request=Constants.STATUS_BUY;
                    String msj= getMessage(Constants.STATUS_BUY,current_id);
                    new SendMessage().execute(msj);
                    return true;

                } else {

                    Log.d("Motion", " onFling: DOWN SWIPE, PENDIENTE");
                    request=Constants.STATUS_SKIP;
                    String msj= getMessage(Constants.STATUS_SKIP,current_id);
                    new SendMessage().execute(msj);
                    return true;
                }


            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                Log.d("Motion", " onScroll: " + e1.toString()+e2.toString());
                return true;
            }

            @Override
            public void onShowPress(MotionEvent event) {
                Log.d("Motion", " onShowPress: " + event.toString());
            }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                Log.d("Motion", " onSingleTapUp: " + event.toString());
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent event) {
                Log.d("Motion", " onDoubleTap: " + event.toString());
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent event) {
                Log.d("Motion", " onDoubleTapEvent: " + event.toString());
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                Log.d("Motion", " onSingleTapConfirmed: " + event.toString());
                return true;
            }
        });


        initializeGoogleApiClient();
    }

    private void initializeGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
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
            finish();
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
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

    public void processReceivedData(JSONObject obj) {

        try {

            String action = obj.getString("action");
            if(action.equalsIgnoreCase(Constants.ACTION_PROCESS_PRODUCT))
            {
                JSONObject data = obj.getJSONObject("data");
                refreshUI(data.getString("id"),data.getString("name"),data.getDouble("price"));


            }
            else if(action.equalsIgnoreCase(Constants.ACTION_EMPTY_LIST))
            {
                bought_btn.setVisibility(View.GONE);
                skip_btn.setVisibility(View.GONE);
                missing_btn.setVisibility(View.GONE);
                name_tv.setText(R.string.empty_list);
                price_tv.setText(R.string.swipe_right);
            }
            //Si lo que llega es una accion de buy now simplemente mandamos el mensaje. Esta accion solo puede llegar de la actividad de Wear "Home"
            else if(action.equalsIgnoreCase(Constants.ACTION_BUY_NOW))
            {
                //Enviamos al handheld lo que viene de home. La instruccion de "buy now"
                new SendMessage().execute(obj.toString());
            }


        } catch (JSONException e) {
            Log.e("ERROR JSON", "JSON no se pudo parsear bien");
            e.printStackTrace();
        }
    }


    private void refreshUI(String id, String name, double price)
    {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(price);

        current_id= id;
        name_tv.setText(name);
        price_tv.setText(moneyString);

    }


    public void boughtClick(View target)
    {
        request=Constants.STATUS_BUY;
        String msj= getMessage(Constants.STATUS_BUY,current_id);
        new SendMessage().execute(msj);
    }
    public void skipClick(View target)
    {
        request=Constants.STATUS_SKIP;
        String msj= getMessage(Constants.STATUS_SKIP,current_id);
        new SendMessage().execute(msj);

    }
    public void missingClick(View target)
    {
        request=Constants.STATUS_MISS;
        String msj= getMessage(Constants.STATUS_MISS,current_id);
        new SendMessage().execute(msj);

    }

    public static String getMessage(String str, String id) {
        try {
            JSONObject json = new JSONObject();
            JSONObject data_json = new JSONObject();
            data_json.put("id",id);
            data_json.put("result",str);
            json.put("action", Constants.ACTION_PROCESSED_PRODUCT);
            json.put("data", data_json);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private class SendMessage extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... msgs) {
            String message= msgs[0];
            Log.e("ATENCION", "Enviendo-> " + message);
            for(String id: getNodes()) {
                MessageApi.SendMessageResult sendMessageResult= Wearable.MessageApi.sendMessage(mGoogleApiClient, id,
                        Constants.PATH, message.getBytes()).await();
                com.google.android.gms.common.api.Status status = sendMessageResult.getStatus();

                if (status.getStatusCode() == WearableStatusCodes.SUCCESS) {
                    Log.d("Test", "Se recibió el mensaje");
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
    // Capture long presses
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

}
