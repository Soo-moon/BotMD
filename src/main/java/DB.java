import DTO.ApiSearchTripod;
import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.Options.SkillOption;
import DTO.Auctions.Options.Tripods;
import DTO.Market.MarketItem;
import DTO.Market.MarketList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import retrofit2.Call;
import retrofit2.Response;

import java.io.*;
import java.util.*;

public class DB {
    private static final HashMap<String, JsonArray> db = new HashMap<>();

    private final boolean debug;
    private final ServerManager serverManager;

    private API api;

    private final String path = Word.DB_DIR;


    public DB(ServerManager serverManager) {
        this.serverManager = serverManager;
        debug = serverManager.isTest;
    }

    public void create() {
        api = serverManager.getApi();
        createBookDB();
    }

    public void readData() {
        try {
            File dir = new File(Word.DB_root);
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
                jsonReader.setLenient(true);
                JsonArray jsonArray =  JsonParser.parseReader(jsonReader).getAsJsonArray();
                db.put(file.getName(), jsonArray);
                System.out.println("DB put done : " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSkillBookName(String name){
        ArrayList<String> data = new ArrayList<>();
        if (!db.containsKey("book")){
            readData();
        }
        JsonArray jsonData = db.get("book");
        for (int i = 0; i < jsonData.size(); i++){
            String bookName = String.valueOf(jsonData.get(i));
            int temp = 0;
            int index = -1;
            for (int t = 0; t < name.length(); t++) {
                for (int j = 0; j < bookName.length(); j++) {
                    if (name.charAt(t) == bookName.charAt(j)) {
                        if (index > j) {
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
                if (debug) System.out.println("add dataArray -> " + bookName);
                data.add(bookName);
            }
        }
        if (debug) System.out.println(data);
        return data;
    }

    public void createBookDB() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    int page;
                    int totalPage = 1;
                    boolean flag = false;
                    JsonArray item = new JsonArray();
                    for (page = 1; page <= totalPage; page++) {
                        Response<MarketList> response = api.requestBookData(page).execute();
                        if (response.isSuccessful()) {
                            flag = true;
                            if (page == 1) {
                                double pageD = response.body().totalCount;
                                totalPage = (int) Math.ceil(pageD / 10);
                            }
                            for (MarketItem marketItem : response.body().marketItems) {
                                System.out.println(marketItem.name);
                                item.add(marketItem.name);
                            }
                        } else {
                            flag = false;
                            System.out.println("create fail BookDB");
                            System.out.println("retry BookDB ");
                            Thread.sleep(65000);
                        }
                    }
                    if (flag) {
                        createFile("book", item.toString().getBytes());
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName("bookCreate");
        thread.start();
    }

    public void createCharacterDB(String characterClass , Work work) {
        Runnable runnable = () -> {
            while (true) {
                try {
                    boolean flag = false;
                    JsonArray characterDB = new JsonArray();
                    Call<AuctionsOption> call = api.auctionsOptions();
                    Response<AuctionsOption> response = call.execute();
                    if (response.isSuccessful()) {
                        flag = true;
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
                    } else {
                        flag = false;
                        System.out.println(characterClass + " : create fail");
                        System.out.println("retry create");
                        Thread.sleep(65000);
                    }
                    if (flag) {
                        createFile(characterClass, characterDB.toString().getBytes());
                        db.put(characterClass , characterDB);
                        work.done();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName(characterClass + " Thread");
        thread.start();
    }

    public void createFile(String name, byte[] b) {
        try {
            String fileName = Word.DB_DIR + name;
            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName, false));
            fileOutputStream.write(b);
            fileOutputStream.flush();
            System.out.println(fileName + ".create!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ApiSearchTripod> getCharacterDB(String className) {
        ArrayList<ApiSearchTripod> apiSearchTripods = new ArrayList<>();
        Work work = () -> {
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
                    apiSearchTripod.tripods = tripods;
                    apiSearchTripods.add(apiSearchTripod);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        if (!db.containsKey(className)){
            createCharacterDB(className , work);
        }else {
            work.done();
        }

        return apiSearchTripods;
    }

    public interface Work{
        void done();
    }
}
