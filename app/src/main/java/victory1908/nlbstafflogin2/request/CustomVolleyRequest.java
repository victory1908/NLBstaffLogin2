package victory1908.nlbstafflogin2.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;

/**
 * Created by Victory1908 on 09-Dec-15.
 */
public class CustomVolleyRequest {

    private static CustomVolleyRequest customVolleyRequest;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private CustomVolleyRequest(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized CustomVolleyRequest getInstance(Context context) {
        if (customVolleyRequest == null) {
            customVolleyRequest = new CustomVolleyRequest(context);
        }
        return customVolleyRequest;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

//    //Request to get json from server we are passing an integer here
//    //This integer will used to specify the page number for the request ?page = requestcount
//    //This method would return a JsonArrayRequest that will be added to the request queue
//    private JsonArrayRequest getDataFromServer(int requestCount) {
//        //Initializing ProgressBar
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
//
//        //Displaying Progressbar
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);
//
//        //JsonArrayRequest of volley
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        //Calling method parseData to parse the json response
//                        parseData(response);
//                        //Hiding the progressbar
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
//                        //If an error occurs that means end of the list has reached
//                        Toast.makeText(context, "No More Items Available", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        //Returning the request
//        return jsonArrayRequest;
//    }
//
//    //This method will parse json data
//    private void parseData(JSONArray array) {
//        for (int i = 0; i < array.length(); i++) {
//            //Creating the superhero object
//            SuperHero superHero = new SuperHero();
//            JSONObject json = null;
//            try {
//                //Getting json
//                json = array.getJSONObject(i);
//
//                //Adding data to the superhero object
//                superHero.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
//                superHero.setName(json.getString(Config.TAG_NAME));
//                superHero.setPublisher(json.getString(Config.TAG_PUBLISHER));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            //Adding the superhero object to the list
//            listSuperHeroes.add(superHero);
//        }
//
//        //Notifying the adapter that data has been added or changed
//        adapter.notifyDataSetChanged();
//    }
}
