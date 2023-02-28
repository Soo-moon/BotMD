import DTO.ApiSearchTripod;
import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.Options.SkillOption;
import DTO.Auctions.Options.Tripods;
import DTO.Auctions.items.SearchDetailOption;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import DTO.Market.RequestMarketItems;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DB {
    private static final HashMap<String, HashMap<String, String>> db = new HashMap<>();
    private boolean debug;

    private final ServerManager serverManager;
    private API api;

    private int dbSize = -1;
    private String dbPath;


    public DB(ServerManager serverManager, API api, int size, String path) {
        this.serverManager = serverManager;
        this.api = api;
        this.dbSize = size;
        this.dbPath = path;

        debug = serverManager.isTest;
    }

    public void makeFile() {
        api.makeFile(Word.rootDir + "/db/db");
    }

    public void readDB() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader((Word.rootDir + "/db/db")));
            HashMap<String, String> hashMap = new HashMap<>();
            reader.lines().forEach(
                    line -> {
                        String name = line.split("//")[0];
                        String id = line.split("//")[1];
                        hashMap.put(name, id);
                    }
            );
            db.put("SkillBook", hashMap);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getSkillBookName(String name) {
        if (db.size() == 0) {
            readDB();
        }
        long startTime = System.currentTimeMillis();
        ArrayList<String> data = new ArrayList<>();

        Set<String> set = db.get("SkillBook").keySet();
        set.forEach(skillName -> {
            if (debug) System.out.println("db name : " + skillName + " inputName : " + name);
            int temp = 0;
            int index = -1;
            for (int i = 0; i < name.length(); i++) {
                for (int j = 0; j < skillName.length(); j++) {
                    if (name.charAt(i) == skillName.charAt(j)) {
                        if (index > j) {
                            if (debug) System.out.println("index > j");
                            break;
                        }
                        temp++;
                        index = j;
                    }
                }
                if (index == -1) {
                    break;
                }
            }
            if (temp == (name.length())) {
                if (debug) System.out.println("add dataArray -> " + skillName);
                data.add(skillName);
            }
        });
        long endTime = System.currentTimeMillis();
        if (debug) System.out.println("DB : SkillBookName time : " + (endTime - startTime) + "ms");
        if (debug) System.out.println(data);
        return data;
    }

    public ArrayList<ApiSearchTripod> getCharacterDB(String className) {
        ArrayList<ApiSearchTripod> apiSearchTripods = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader((Word.rootDir + "/db/" + className)));
            String jsonObj = reader.readLine();
            JsonArray characterDB = new Gson().fromJson(jsonObj, JsonArray.class);
            for (int i = 0; i < characterDB.size() - 1; i++) {
                ApiSearchTripod apiSearchTripod = new ApiSearchTripod();
                JsonObject jsonObject = (JsonObject) characterDB.get(i);
                JsonObject tripods = jsonObject.getAsJsonObject("Tripods");
                for (String skillName : tripods.keySet()) {
                    apiSearchTripod.SkillName = skillName;
                    apiSearchTripod.FirstOption = jsonObject.get("FirstOption").getAsInt();
                }
                apiSearchTripod.tripods.add(tripods);
                apiSearchTripods.add(apiSearchTripod);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiSearchTripods;
    }


    public void createBookDB() {
        Runnable runnable = () -> {
            try {
                Response<MarketList> response = api.requestBookData(1);
                if (response.isSuccessful()){
                    double pageDouble = response.body().totalCount;
                    int page = (int) Math.ceil(pageDouble / 10);
                    JsonArray item = new JsonArray();
                    for (int i = 1; i <= page; i++) {
                        Response<MarketList> res = api.requestBookData(i);
                        if (res.isSuccessful()){
                            for (MarketItem marketItem : res.body().marketItems) {
                                item.add(marketItem.name);
                            }
                        }
                        else throw new RuntimeException(res.errorBody().string());
                    }
                    createFile("book", item.toString().getBytes());
                }
                else throw new RuntimeException(response.errorBody().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        runnable.run();
    }

    public void createCharacterDB(String characterClass) {
        try {
            Callback<AuctionsOption> callback = new Callback<>() {
                @Override
                public void onResponse(Call<AuctionsOption> call, Response<AuctionsOption> response) {
                    JsonArray characterDB = new JsonArray();
                    for (SkillOption skillOption : response.body().SkillOption) {
                        if (skillOption.Class.equals(characterClass) && !skillOption.IsSkillGroup) {
                            JsonObject jsonTripod = new JsonObject();
                            for (Tripods tripod : skillOption.Tripods) {
                                if (!tripod.IsGem) {
                                    jsonTripod.addProperty(tripod.Text, tripod.Value);
                                }
                            }
                            JsonObject jsonSkill = new JsonObject();
                            jsonSkill.addProperty("SkillName", skillOption.Text);
                            jsonSkill.addProperty("FirstOption", skillOption.Value);
                            jsonSkill.add("Tripods", jsonTripod);
                            characterDB.add(jsonSkill);
                        }
                    }
                    createFile(characterClass, characterDB.toString().getBytes());
                }

                @Override
                public void onFailure(Call<AuctionsOption> call, Throwable t) {
                    System.out.println("auctions_Options Fail " + t.getMessage());
                    t.printStackTrace();
                }
            };
            api.auctionsOptions(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFile(String name, byte[] b) {
        try {
            String fileName = Word.dbDir + name;
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
            fileOutputStream.write(b);
            System.out.println(fileName + ".create!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
