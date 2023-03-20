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
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.*;

public class DB {
    private final HashMap<String, JsonArray> db = new HashMap<>();
    private final Log log = new Log();

    private ServerManager serverManager;
    private API api;

    public DB(ServerManager serverManager, API api) {
        this.serverManager = serverManager;
        this.api = api;
        readDB();
    }

    private void readDB() {
        try {
            String dir = System.getProperty("user.home") + serverManager.getProperty("db.dir");
            File[] files = new File(dir).listFiles();
            if (files != null) {
                if (files.length > 0) {
                    log.d("try File read size : " + files.length);
                    for (File file : files) {
                        JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
                        jsonReader.setLenient(true);
                        JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
                        db.put(file.getName(), jsonArray);
                        log.d("read DataFile : " + file.getName());
                    }
                }
                log.d("read Done !! size : " + files.length);
            }
            updateSkillBook();
        } catch (FileNotFoundException e) {
            log.e("db file not Found", e);
        }
    }

    private void makeFile(String name, byte[] b) {
        try {
            String fileName = serverManager.getProperty("db.dir") + name;
            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName, false));
            fileOutputStream.write(b);
            fileOutputStream.flush();
            log.d(fileName + ".create !!");
        } catch (Exception e) {
            log.e("make file error.", e);
        }
    }

    public void updateSkillBook() {
        Thread worker = new Thread(() -> {
            try {
                Queue<Call<MarketList>> workList = new LinkedList<>();
                int totalPage = 1;
                JsonArray item = new JsonArray();

                for (int i = 1; i <= totalPage; i++) {
                    Call<MarketList> c = api.requestBookData(i);
                    workList.add(api.requestBookData(i));

                    if (i == 1) {
                        Response<MarketList> r = c.execute();
                        if (r.isSuccessful()){
                            double pageD = r.body().totalCount;
                            totalPage = (int) Math.ceil(pageD / 10);
                        }
                    }
                }

                log.d(Thread.currentThread().getName() + ": create !!  size : " + workList.size());

                while (!workList.isEmpty()) {
                    Call<MarketList> call = workList.peek();
                    Response<MarketList> response = call.execute();

                    if (response.code() == 200) {
                        if (response.body() != null) {
                            for (MarketItem marketItem : response.body().marketItems) {
                                item.add(marketItem.name);
                            }
                            workList.poll();
                        }
                    } else if (response.code() == 429) {
                        log.d("api code 420 ");
                        Thread.sleep(65000);
                    } else {
                        log.d("api fail code : " + response.code());
                    }
                }

                makeFile("skillBook", item.toString().getBytes());
                log.d(Thread.currentThread().getName() + " done !!");

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        worker.setName("updateSkillBook");
        worker.start();
    }

    public ArrayList<String> getSkillBook(String skillName) {
        ArrayList<String> data = new ArrayList<>();
        if (!db.containsKey("skillBook")) {
            updateSkillBook();
        }
        JsonArray jsonData = db.get("skillBook");
        for (int i = 0; i < jsonData.size(); i++) {
            String bookName = String.valueOf(jsonData.get(i));
            int temp = 0;
            int index = -1;
            for (int t = 0; t < skillName.length(); t++) {
                for (int j = 0; j < bookName.length(); j++) {
                    if (skillName.charAt(t) == bookName.charAt(j)) {
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
            if (temp == (skillName.length())) {
                data.add(bookName);
            }
        }
        return data;
    }

    public void createCharacterDB(String characterClass) {
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
                        makeFile(characterClass, characterDB.toString().getBytes());
                        db.put(characterClass, characterDB);
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

    public ArrayList<ApiSearchTripod> getCharacterDB(String className) {
        ArrayList<ApiSearchTripod> apiSearchTripods = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader((serverManager.getProperty("db.dir") + className)));
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


        if (!db.containsKey(className)) {
            createCharacterDB(className);
        }


        return apiSearchTripods;
    }

    private boolean checkData(String name) {
        return db.get(name) != null;
    }
}
