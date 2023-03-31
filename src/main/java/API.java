import DTO.ApiSearchTripod;
import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.items.Auction;
import DTO.Auctions.items.AuctionItem;
import DTO.Auctions.items.RequestAuctionItems;
import DTO.Auctions.items.SearchDetailOption;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import DTO.Market.RequestMarketItems;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;


public class API {
    private static final String baseURL = "https://developer-lostark.game.onstove.com";

    private final APIService apiService;

    public API(String apiKey) throws RuntimeException {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            OkHttpClient client = new OkHttpClient.Builder()
//                    .sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .addInterceptor(chain -> {
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

        } catch (Exception e) {
            throw new RuntimeException("API");
        }
    }

    //스킬검색
    public MarketItem searchSkillBook(String skill) {
        MarketItem marketItem = null;

        RequestMarketItems requestMarketItems = new RequestMarketItems();
        requestMarketItems.categoryCode = 40000;
        requestMarketItems.itemGrade = "전설";
        requestMarketItems.itemName = skill;

        try {
            Response<MarketList> response = apiService.searchItemPrice(requestMarketItems).execute();
            if (response.code() == 200 && response.body() != null){
                marketItem = response.body().marketItems[0];
            }
            else if (response.code() == 429){
                //
            }
            else {
                Log.e(response.message());
                throw new RuntimeException("API Fail code : " + response.code());
            }

            if (marketItem == null){
                throw new RuntimeException("API Request Fail - Item is Null");
            }

        } catch (IOException e) {
            Log.e("API Request Fail");
            throw new RuntimeException(e);
        } catch (RuntimeException e){
            Log.e(e.getMessage());
        }

        return marketItem;
    }


    //test
    public void request_tripod(String className) {
        try {
            ArrayList<Call<Auction>> callList = new ArrayList<>();

            ArrayList<ApiSearchTripod> tripodList = new ArrayList<>(); //todo tripodList get
            for (ApiSearchTripod option : tripodList) {
                SearchDetailOption searchDetailOption = new SearchDetailOption();
                searchDetailOption.FirstOption = option.FirstOption;
                for (String tripodKey : option.tripods.keySet()) {
                    searchDetailOption.SecondOption = option.tripods.get(tripodKey).getAsInt();
                    Call<Auction> call = auctions_Item_Tripods(className, searchDetailOption);
                    callList.add(call);
                }
            }
            Runnable runnable = () -> {
                while (!callList.isEmpty()) {
                    for (Call<Auction> call : callList) {
                        try {
                            Response<Auction> res = call.execute();
                            System.out.println(res.raw().request().body());
                            if (res.isSuccessful()) {
                                int totalItemPrice = 0;
                                int totalCount = 0;
                                for (AuctionItem auctionItem : res.body().Items) {
                                    totalItemPrice += auctionItem.AuctionInfo.BuyPrice;
                                    System.out.println("auctionItem.AuctionInfo.BuyPrice : " + auctionItem.AuctionInfo.BuyPrice);
                                    totalCount++;
                                }
                                System.out.println("ItemPrice " + (totalItemPrice / totalCount));
                                callList.remove(call);
                            } else {
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
        } catch (Exception e) {
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
}