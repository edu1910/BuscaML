package br.com.ceducarneiro.buscaml;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MLParser {

    private MLParserCallback callback;
    private MLAsyncTask task;

    public MLParser(MLParserCallback callback) {
        this.callback = callback;
    }

    public void getProducts(String query) {

        if (task != null) {
            task.cancel(true);
        }

        task = new MLAsyncTask();
        task.execute(query.replace(" ", "%20"));
    }

    interface MLParserCallback {
        void onSuccess(List<MLProduct> products);
        void onError();
    }

    public class MLProduct {
        public String id;
        public String title;
        public double price;
        public boolean newProduct;
        public String link;
        public String thumbnail;
    }

    private class MLAsyncTask extends AsyncTask<String, Void, List<MLProduct>> {

        private static final String URL = "https://api.mercadolibre.com/sites/MLB/search?q=%s";

        @Override
        protected List<MLProduct> doInBackground(String... params) {
            List<MLProduct> products = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(String.format(URL, params[0]));

            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    products = parseProducts(EntityUtils.toString(entity));
                }
            } catch (Exception ignored) {
                products = null;
            }

            return products;
        }

        private List<MLProduct> parseProducts(String jsonString) {
            List<MLProduct> products = new ArrayList<MLProduct>();

            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray productsArray = jsonObj.getJSONArray("results");

                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject product = productsArray.getJSONObject(i);

                    MLProduct productObj = new MLProduct();
                    productObj.id = product.getString("id");
                    productObj.link = product.getString("permalink");
                    productObj.newProduct = product.getString("condition").equals("new");
                    productObj.price = product.getDouble("price");
                    productObj.title = product.getString("title");
                    productObj.thumbnail = product.getString("thumbnail");

                    products.add(productObj);
                }
            } catch (JSONException ignored) {
                products = null;
            }

            return products;
        }

        @Override
        protected void onPostExecute(List<MLProduct> mlProducts) {
            if (callback != null) {
                if (mlProducts != null) {
                    callback.onSuccess(mlProducts);
                } else {
                    callback.onError();
                }
            }
        }
    }

}
