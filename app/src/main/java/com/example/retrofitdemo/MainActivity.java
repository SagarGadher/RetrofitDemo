package com.example.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tvResult;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getPost();
        getComment();
        //createPost();
        //updatePost();
        //deletePost();
    }

    private void getPost() {

        //this is use for map getPost
        Map<String, String> parameter = new HashMap<>();
        parameter.put("userId", "1");
        parameter.put("_sort", "id");
        parameter.put("_order", "desc");
        //
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{3, 4}, "id", "decs");
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameter);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    tvResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";

                    content += "Id: " + post.getId() + "\n";
                    content += "User Id: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    tvResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }

    private void getComment() {
        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    tvResult.setText("Code: " + response.code());
                    return;
                }

                List<Comment> comments = response.body();

                for (Comment comment : comments) {
                    String content = "";

                    content += "Id: " + comment.getId() + "\n";
                    content += "Post Id: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    tvResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }

    private void createPost() {
        //Post post = new Post(23, "new Title", "New Text");
        //Call call = jsonPlaceHolderApi.createPost(post);

        Call call = jsonPlaceHolderApi.createPost(23, "New Title", "New Text");

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    tvResult.setText("Code: " + response.code());
                    return;
                }

                Post responsePost = (Post) response.body();

                String content = "";

                content += "Code: " + response.code() + "\n";
                content += "Id: " + responsePost.getId() + "\n";
                content += "User Id: " + responsePost.getUserId() + "\n";
                content += "Title: " + responsePost.getTitle() + "\n";
                content += "Text: " + responsePost.getText() + "\n\n";

                tvResult.append(content);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12, null, "New Text");

        Call call = jsonPlaceHolderApi.putPost(5, post);
        //Call call = jsonPlaceHolderApi.patchPost(5,post);

        //Call call = jsonPlaceHolderApi.putPostWithHeader("abc",5,post);

        /*Map<String,String> header = new HashMap<>();
        header.put("Map-Header1","abc");
        header.put("Map-Header2","def");
        header.put("Map-Header3","ghi");

        Call call = jsonPlaceHolderApi.patchPostWithHeader(header,5,post);*/

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    tvResult.setText("Code: " + response.code());
                    return;
                }

                Post responsePost = (Post) response.body();

                String content = "";

                content += "Code: " + response.code() + "\n";
                content += "Id: " + responsePost.getId() + "\n";
                content += "User Id: " + responsePost.getUserId() + "\n";
                content += "Title: " + responsePost.getTitle() + "\n";
                content += "Text: " + responsePost.getText() + "\n\n";

                tvResult.append(content);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost() {
        Call call = jsonPlaceHolderApi.deletePost(2);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                tvResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }
}




























