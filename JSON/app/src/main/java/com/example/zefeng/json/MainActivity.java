package com.example.zefeng.json;

import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private Button send;
//    private EditText editText;
//    private ImageView img;
//    private static final String TAG = "Json";
//    private String stt = "CN101281701";
//    private Bitmap bitmap;
//
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1){
//                img.setImageBitmap(bitmap);
//            }
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        send = (Button) findViewById(R.id.button);
//        editText = (EditText) findViewById(R.id.edit);
//        img = (ImageView) findViewById(R.id.img);
//        final String str = editText.getText().toString();
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "you clicked");
//                requestUrl(str);
//            }
//        });
//    }
//
//    private void requestUrl(final String str) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL("http://files.heweather.com/cond_icon/101.png");
////                    connection = (HttpURLConnection) url.openConnection();
////                    connection.setRequestMethod("GET");
////                    connection.setReadTimeout(8000);
////                    connection.setConnectTimeout(8000);
////                    Log.d(TAG,"start");
////                    InputStream in = connection.getInputStream();
//                    InputStream in = url.openStream();
//                    bitmap = BitmapFactory.decodeStream(in);
//                    handler.sendEmptyMessage(1);
//                    in.close();
//
////                    BufferedReader response = new BufferedReader(new InputStreamReader(in));
////                    StringBuilder str = new StringBuilder();
////                    String line;
////                    while ((line = response.readLine())!= null){
////                        str.append(line);
////                        Log.d(TAG, str.toString());
////                        JsonGetBack(str.toString());
////                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    if (connection !=null){
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    private void JsonGetBack(String JsonData) {
//        try {
//            JSONObject jsonObject = new JSONObject(JsonData);
//            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
//            for (int i = 0;i<jsonArray.length();i++){
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                String city = jsonObject1.getString("city");
//                Log.d(TAG,city);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
