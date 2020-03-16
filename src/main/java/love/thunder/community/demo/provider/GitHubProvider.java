package love.thunder.community.demo.provider;

import com.alibaba.fastjson.JSON;
import love.thunder.community.demo.dto.AccessTokenDTO;
import love.thunder.community.demo.dto.GitHubUser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;

import static okhttp3.RequestBody.*;

//个人代码test：25e58e0b7a56835b8c75c7d14a1de5419baf2563
//test1:43402391f0313b7f4e89b3418768c7f40d4e0a98
//ctrl+alt+v 可以创建变量，用了就知道
@Component
public class GitHubProvider {


    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String[] split = string.split("&");
            String tokenstr = split[0];
            String tonken = tokenstr.split("=")[1];
            return tonken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GitHubUser getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        Response response = client.newCall(request).execute();
        String string = response.body().string();
        GitHubUser gitHubUser = JSON.parseObject(string,GitHubUser.class);
        return gitHubUser;
    }

}
