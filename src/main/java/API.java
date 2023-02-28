import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.Options.SkillOption;
import DTO.Auctions.Options.Tripods;
import DTO.Auctions.items.Auction;
import DTO.Auctions.items.AuctionItem;
import DTO.Auctions.items.RequestAuctionItems;
import DTO.Auctions.items.SearchDetailOption;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import DTO.Market.requestMarketItems;
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

            APIService.searchItemPrice("40000", "전설", null, str).enqueue(new Callback<MarketList>() {
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

    public void auctionsOptions(){
        Callback<AuctionsOption> callback = new Callback<AuctionsOption>() {
            @Override
            public void onResponse(Call<AuctionsOption> call, Response<AuctionsOption> response) {
                String CLASS = "바드";
                for (SkillOption sa : response.body().SkillOption){
                    if (sa.Class.equals(CLASS)){
                        String name = sa.Text;
                        ArrayList<Tripods> tripods = new ArrayList<>();
                        for (Tripods tripod : sa.Tripods){
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
        };
    }

    public void auctions_Item_Tripods(String characterClass , SearchDetailOption searchDetailOption){
        RequestAuctionItems requestAuctionItems = new RequestAuctionItems();
        requestAuctionItems.CharacterClass = characterClass;
        requestAuctionItems.SkillOptions = new SearchDetailOption[]{searchDetailOption};
        APIService.auctions_Items(requestAuctionItems).enqueue(new Callback<Auction>() {
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
        APIService.searchItemPrice("40000","전설",null,"").enqueue(new Callback<MarketList>() {
            @Override
            public void onResponse(Call<MarketList> call, Response<MarketList> response) {
                double pageDouble =  response.body().totalCount / 10 ;
                int page = (int) Math.ceil(pageDouble);

                requestMarketItems requestMarketItems = new requestMarketItems();
                requestMarketItems.categoryCode = 40000;
                requestMarketItems.itemGrade = "전설";
                for (int i = 1; i <= page; i++){
                    requestMarketItems.pageNo = i;
                    APIService.searchItemPrice(requestMarketItems).enqueue(new Callback<MarketList>() {
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
}
