import DTO.ApiSearchTripod;
import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.items.*;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import DTO.Market.RequestMarketItems;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;


public class API {
    private static final String baseURL = "https://developer-lostark.game.onstove.com";

    private final Log log = new Log();

    private APIService apiService;
    private ServerManager serverManager;
    private DiscordBot bot;

    public API(ServerManager serverManager , String URL, String Key){
        try {
            this.serverManager = serverManager;
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
            e.printStackTrace();
        }
    }

    public API(ServerManager serverManager ,String apiKey) {
        this.serverManager = serverManager;
        bot = serverManager.getBot();
        try {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .addHeader("content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(APIService.class);

            Callback<MarketList> callback = new Callback<MarketList>() {
                @Override
                public void onResponse(Call<MarketList> call, Response<MarketList> response) {
                    System.out.println("test1");
                }

                @Override
                public void onFailure(Call<MarketList> call, Throwable t) {
                    System.out.println("test2");
                }
            };

            requestBookData(1).enqueue(callback);
        }catch (Exception e){
            log.e("API create fail",e);
        }
    }

    public void create(String URL, String Key, ServerManager serverManager) throws RuntimeException {
        try {
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
            this.serverManager = serverManager;
            bot = serverManager.getBot();
        } catch (Exception e) {
            throw new RuntimeException("API create Fail");
        }
    }

    public void searchItem(String itemName, MessageChannel channel) {
        ArrayList<String> searchData = serverManager.getDB().getSkillBook(itemName);

        if (searchData.size() == 0) {
            throw new RuntimeException("데이터 없음");
        }
        for (String searchName : searchData) {
            String str = searchName.replace("각인서", "").replace("\"" , "");
            if (str.contains("[")) {
                str = str.split("] ")[1];
            }

            apiService.searchItemPrice("40000", "전설", null, str).enqueue(new Callback<MarketList>() {
                @Override
                public void onResponse(Call<MarketList> call, Response<MarketList> response) {
                    MarketItem marketItem = response.body().marketItems[0];
                    if (response.body().totalCount > 1) {
                        for (MarketItem items : response.body().marketItems) {
                            if (items.name.equals(searchName)) {
                                marketItem = items;
                            }
                        }
                    }
                    bot.divGold(channel, marketItem.recentPrice, marketItem.name);
                }

                @Override
                public void onFailure(Call<MarketList> call, Throwable t) {
                    log.d(t.getMessage());
                }
            });
        }
    }

    public void request_tripod(String className){
        try {
            ArrayList<Call<Auction>> callList = new ArrayList<>();

            ArrayList<ApiSearchTripod> tripodList = serverManager.getDB().getCharacterDB(className);
            for (ApiSearchTripod option : tripodList){
                SearchDetailOption searchDetailOption = new SearchDetailOption();
                searchDetailOption.FirstOption = option.FirstOption;
                for (String tripodKey : option.tripods.keySet()){
                    searchDetailOption.SecondOption = option.tripods.get(tripodKey).getAsInt();
                    Call<Auction> call = auctions_Item_Tripods(className , searchDetailOption);
                    callList.add(call);
                }
            }
            Runnable runnable = () -> {
              while (!callList.isEmpty()){
                  for (Call<Auction> call : callList){
                      try {
                          Response<Auction> res = call.execute();
                          System.out.println(res.raw().request().body());
                          if (res.isSuccessful()){
                              int totalItemPrice = 0;
                              int totalCount = 0;
                              for (AuctionItem auctionItem : res.body().Items) {
                                  totalItemPrice += auctionItem.AuctionInfo.BuyPrice;
                                  System.out.println("auctionItem.AuctionInfo.BuyPrice : " + auctionItem.AuctionInfo.BuyPrice);
                                  totalCount++;
                              }
                              System.out.println("ItemPrice " + (totalItemPrice / totalCount));
                              callList.remove(call);
                          }else {
                              System.out.println("error sleep 1min");
                              Thread.sleep(65000);
                          }
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                      }
                  }
              }
            };
            Thread thread = new Thread(runnable);
            thread.setName(className + "AuctionSearch_tripods");
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Call<Auction> auctions_Item_Tripods(String characterClass, SearchDetailOption searchDetailOption) {
        RequestAuctionItems requestAuctionItems = new RequestAuctionItems();
        requestAuctionItems.CharacterClass = characterClass;
        requestAuctionItems.SkillOptions = new SearchDetailOption[]{searchDetailOption};
        return apiService.auctions_Items(requestAuctionItems);
    }

    public Call<AuctionsOption> auctionsOptions() {
       return apiService.auctionsOptions();
    }

    //done
    public Call<MarketList> requestBookData(int page){
        RequestMarketItems requestMarketItems = new RequestMarketItems();
        requestMarketItems.categoryCode = 40000;
        requestMarketItems.itemGrade = "전설";
        if (page != 1) {
            requestMarketItems.pageNo = page;
        }
        return apiService.searchItemPrice(requestMarketItems);
    }
}

/*
                    Response<Auction> res = call.execute();
                    if (res.isSuccessful()){
                        int totalItemPrice = 0;
                        int totalCount = 0;
                        for (AuctionItem auctionItem : res.body().Items) {
                            totalItemPrice += auctionItem.AuctionInfo.BuyPrice;
                            System.out.println("auctionItem.AuctionInfo.BuyPrice : " + auctionItem.AuctionInfo.BuyPrice);
                            totalCount++;
                        }
                        System.out.println("className : " + className);
                        System.out.println("skillName : "+option.SkillName);
                        System.out.println("tripodName : " +tripodKey);
                        System.out.println("ItemPrice " + (totalItemPrice / totalCount));
                    }
                    else{
                        //sleep;
                    }
*/