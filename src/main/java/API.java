import DTO.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class API {
    private APIService APIService;
    private ServerManager serverManager;
    private DiscordBot bot;

    private HashMap<String , Tripods[]> testMap = new HashMap<>();

    public void create(String URL , String Key , ServerManager serverManager) throws RuntimeException{
        try {
            this.serverManager = serverManager;
            bot = serverManager.getBot();
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + Key)
                        .addHeader("content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService = retrofit.create(APIService.class);
        }catch (Exception e){
            throw new RuntimeException("API create Fail");
        }
    }

    public void searchItem(String itemName , MessageChannel channel){
        ArrayList<String> searchData = serverManager.getDB().getSkillBookName(itemName);

        if (searchData.size() == 0){
            throw new RuntimeException("데이터 없음");
        }
        for (String searchName : searchData) {
            String str = searchName.replace("각인서" , "");
            if (str.contains("[")) {
                str = str.split("] ")[1];
            }

            APIService.searchItemPrice("40000", "전설", null, str).enqueue(new Callback<ItemDTO>() {
                @Override
                public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                    Item item = response.body().getItems()[0];
                    if (Integer.parseInt(response.body().getTotalCount()) > 1){
                        for (Item items : response.body().getItems()){
                            if (items.getName().equals(searchName)){
                                item = items;
                            }
                        }
                    }
                    bot.divGold(channel , item.getRecentPrice() , item.getName());
                }

                @Override
                public void onFailure(Call<ItemDTO> call, Throwable t) {
                    System.out.println("failure "+ t.getMessage());
                }
            });
        }
    }

    public void auctionsOptions(){
        APIService.auctionsOptions().enqueue(new Callback<AuctionsOption>() {
            @Override
            public void onResponse(Call<AuctionsOption> call, Response<AuctionsOption> response) {
                String CLASS = "바드";
                for (SkillOptions_Auctions sa : response.body().getSkillOptions_Auctions()){
                    if (sa.classGet().equals(CLASS)){
                        String name = sa.getText();
                        ArrayList<Tripods> tripods = new ArrayList<>();
                        for (Tripods tripod : sa.getTripods()){
                            if (tripod.getGem()) continue;
                            tripods.add(tripod);
                        }
                        testMap.put(name, tripods.toArray(new Tripods[tripods.size()]));
                    }
                }
                int count = 0;
                for (String name : testMap.keySet()){
                    System.out.println("name : " + name + " testMap.get(name).length :" +testMap.get(name).length);
                    count += testMap.get(name).length;
                }

                System.out.println("count : " + count);
            }

            @Override
            public void onFailure(Call<AuctionsOption> call, Throwable t) {
                System.out.println("failure "+ t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void makeFile(String filePath){
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream((filePath),false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        APIService.searchItemPrice("40000","전설",null,"").enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                double pageDouble =  Double.parseDouble(response.body().getTotalCount()) / 10 ;
                int page = (int) Math.ceil(pageDouble);

                ItemSearchParam itemSearchParam = new ItemSearchParam();
                itemSearchParam.setCategoryCode("40000");
                itemSearchParam.setItemGrade("전설");
                for (int i = 1; i <= page; i++){
                    itemSearchParam.setPageNo(String.valueOf(i));
                    APIService.searchItemPrice(itemSearchParam).enqueue(new Callback<ItemDTO>() {
                        @Override
                        public void onResponse(@NotNull Call<ItemDTO> call, @NotNull Response<ItemDTO> response) {
                            for (Item item : response.body().getItems()){
                                String line = item.getName()+ "//" +item.getId() +"\n";
                                byte[] a = line.getBytes();
                                try {
                                    fileOutputStream.write(a);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ItemDTO> call, Throwable t) {
                            System.out.println("makeFile failure "+ t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ItemDTO> call, Throwable t) {
                System.out.println("failure "+ t.getMessage());
            }
        });
    }
}
