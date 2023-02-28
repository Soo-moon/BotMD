import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.Options.SkillOption;
import DTO.Auctions.Options.Tripods;
import DTO.Auctions.items.Auction;
import DTO.Auctions.items.AuctionItem;
import DTO.Auctions.items.RequestAuctionItems;
import DTO.Auctions.items.SearchDetailOption;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import DTO.Market.RequestMarketItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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


public class API {
    private APIService apiService;
    private ServerManager serverManager;
    private DiscordBot bot;


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

            apiService = retrofit.create(APIService.class);
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

            apiService.searchItemPrice("40000", "전설", null, str).enqueue(new Callback<MarketList>() {
                @Override
                public void onResponse(Call<MarketList> call, Response<MarketList> response) {
                    MarketItem marketItem = response.body().marketItems[0];
                    if (response.body().totalCount > 1){
                        for (MarketItem items : response.body().marketItems){
                            if (items.name.equals(searchName)){
                                marketItem = items;
                            }
                        }
                    }
                    bot.divGold(channel , marketItem.recentPrice , marketItem.name);
                }

                @Override
                public void onFailure(Call<MarketList> call, Throwable t) {
                    System.out.println("failure "+ t.getMessage());
                }
            });
        }
    }

    public void make_AuctionsOptions(String characterClass , String path)  {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path , false);
            Callback<AuctionsOption> callback = new Callback<AuctionsOption>() {
                @Override
                public void onResponse(Call<AuctionsOption> call, Response<AuctionsOption> response) {
                    JsonArray characterDB = new JsonArray();
                    for (SkillOption skillOption : response.body().SkillOption){
                        if (skillOption.Class.equals(characterClass) && !skillOption.IsSkillGroup){
                            JsonObject jsonTripod = new JsonObject();
                            for (Tripods tripod : skillOption.Tripods){
                                if (!tripod.IsGem) {
                                    jsonTripod.addProperty(tripod.Text , tripod.Value);
                                }
                            }
                            JsonObject jsonSkill = new JsonObject();
                            jsonSkill.addProperty("SkillName",skillOption.Text);
                            jsonSkill.addProperty("FirstOption" , skillOption.Value);
                            jsonSkill.add("Tripods" , jsonTripod);
                            characterDB.add(jsonSkill);
                        }
                    }
                    try {
                        fileOutputStream.write(characterDB.toString().getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public void onFailure(Call<AuctionsOption> call, Throwable t) {
                    System.out.println("auctions_Options Fail "+ t.getMessage());
                    t.printStackTrace();
                }
            };


            apiService.auctionsOptions().enqueue(callback);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void auctionsOptions( Callback<AuctionsOption> callback){
        apiService.auctionsOptions().enqueue(callback);
    }

    public void auctions_Item_Tripods(String characterClass , SearchDetailOption searchDetailOption){
        RequestAuctionItems requestAuctionItems = new RequestAuctionItems();
        requestAuctionItems.CharacterClass = characterClass;
        requestAuctionItems.SkillOptions = new SearchDetailOption[]{searchDetailOption};
        apiService.auctions_Items(requestAuctionItems).enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {
                int totalItemPrice = 0;
                int totalCount = 0;
                for (AuctionItem auctionItem : response.body().Items){
                    totalItemPrice += auctionItem.AuctionInfo.BuyPrice;
                    System.out.println("auctionItem.AuctionInfo.BuyPrice : " +auctionItem.AuctionInfo.BuyPrice);
                    totalCount++;
                }
                System.out.println("className : " + characterClass);
                System.out.println("searchDetailOption " + searchDetailOption.FirstOption +" : " +searchDetailOption.SecondOption);
                System.out.println("ItemPrice " + (totalItemPrice / totalCount));
            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                System.out.println("auctions_Item_Tripods Fail"+ t.getMessage());
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
        apiService.searchItemPrice("40000","전설",null,"").enqueue(new Callback<MarketList>() {
            @Override
            public void onResponse(Call<MarketList> call, Response<MarketList> response) {
                double pageDouble =  response.body().totalCount;
                int page = (int) Math.ceil(pageDouble/10);

                RequestMarketItems requestMarketItems = new RequestMarketItems();
                requestMarketItems.categoryCode = 40000;
                requestMarketItems.itemGrade = "전설";
                for (int i = 1; i <= page; i++){
                    requestMarketItems.pageNo = i;
                    apiService.searchItemPrice(requestMarketItems).enqueue(new Callback<MarketList>() {
                        @Override
                        public void onResponse(@NotNull Call<MarketList> call, @NotNull Response<MarketList> response) {
                            for (MarketItem marketItem : response.body().marketItems){
                                String line = marketItem.name+ "//" + marketItem.id +"\n";
                                byte[] a = line.getBytes();
                                try {
                                    fileOutputStream.write(a);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MarketList> call, Throwable t) {
                            System.out.println("makeFile failure "+ t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MarketList> call, Throwable t) {
                System.out.println("failure "+ t.getMessage());
            }
        });
    }

    public Response<MarketList> requestBookData(int page) throws IOException {
        RequestMarketItems requestMarketItems = new RequestMarketItems();
        requestMarketItems.categoryCode = 40000;
        requestMarketItems.itemGrade = "전설";
        if (page != 1){
            requestMarketItems.pageNo = page;
        }
        return apiService.searchItemPrice(requestMarketItems).execute();
    }
}
