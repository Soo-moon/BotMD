import DTO.ApiSearchTripod;
import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.Options.SkillOption;
import DTO.Auctions.Options.Tripods;
import DTO.Market.MarketItem;
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
    private final HashMap<String, JsonArray> db = new HashMap<>();

    private static final String rootDir = System.getProperty("user.home") + "/server/system/";

    private final Properties config = new Properties();

    private API api;

    public DB() throws IOException {
        config.load(new FileInputStream(rootDir + "config.properties"));

        File[] files = new File(rootDir + "db/").listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
                jsonReader.setLenient(true);
                JsonArray jsonArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
                db.put(file.getName(), jsonArray);
                Log.d("read DataFile : " + file.getName());
            }
        }

        Log.d("DB init !!");
    }

    public void setApi(API api) {
        this.api = api;
    }

    public String getConfig(String key) {
        return config.getProperty(key);
    }

    //스킬검색
    public ArrayList<MarketItem> getSkillBook(String name) throws RuntimeException {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<MarketItem> data = new ArrayList<>();

        if (db.containsKey("skillBook")) {
            JsonArray jsonData = db.get("skillBook");

            for (int i = 0; i < jsonData.size(); i++) {
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
                    String target = bookName.replace(" 각인서", "");
                    target = target.replace("\"", "");
                    if (target.contains("[")) {
                        target = target.split("] ")[1];
                    }
                    dataList.add(target);
                }
            }

            if (dataList.size() == 0) {
                Log.e("No such Data .." + name);
                throw new RuntimeException();
            }

            Log.d("dataList Size .. [ " + dataList.size() + " ]");

            if (api == null) {
                Log.e("API Not Found");
                throw new RuntimeException();
            }

            for (String skill : dataList) {
                MarketItem[] marketItem = api.searchSkillBook(skill);
                for (MarketItem target : marketItem) {
                    if (!data.contains(target)) {
                        Log.d("MarketItem add ! - " + target.name);
                        Log.d("info.recentPrice : " + target.recentPrice);
                        Log.d("info.currentMinPrice : " + target.currentMinPrice);
                        data.add(target);
                    }
                }

            }

        } else {
            Log.e("DB Not contains - SkillBook");
            throw new RuntimeException();
        }

        return data;
    }

    private void makeFile(String name, byte[] b) {
        try {
            String fileName = rootDir + "db/" + name;
            File file = new File(fileName);
            if (!file.exists()){
                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdir();
                }
                file.createNewFile();
            }
            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(file, false));
            fileOutputStream.write(b);
            fileOutputStream.flush();
            Log.d(fileName + ".create !!");
        } catch (Exception e) {
            Log.e("make file error.", e);
        }
    }


    //test
    public void createCharacterDB(String characterClass) {
        Runnable runnable = () -> {
            while (true) {
                try {
                    boolean flag;
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

    public void createClassData() {
        Call<AuctionsOption> call = api.auctionsOptions();

        Response<AuctionsOption> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        Tripods tripods = new Tripods();
//        tripods.IsGem = false;
//        tripods.Text = "testtest 한글";
//        tripods.Value = 1;
//        Gson gson = new Gson();
//        String test = gson.toJson(tripods);
//        System.out.println(test);
//        JsonObject jsonObject = (JsonObject) JsonParser.parseString(test);
//        System.out.println(jsonObject);


        if (response.isSuccessful() && response.body() != null) {
            long startTime = System.currentTimeMillis();

            HashMap<String, JsonArray> classData = new HashMap<>();

            for (SkillOption skillOption : response.body().SkillOption) {
                if (!skillOption.IsSkillGroup) {
                    if (classData.get(skillOption.Class) == null) {
                        JsonArray jsonArray = new JsonArray();
                        classData.put(skillOption.Class, jsonArray);
                    }

                    ApiSearchTripod apiSearchTripod = new ApiSearchTripod();
                    JsonArray apiSearchArray = new JsonArray();
                    JsonArray jsonArray = classData.get(skillOption.Class);

                    for (Tripods tripods : skillOption.Tripods) {
                        if (tripods.IsGem) {
                            continue;
                        }
                        JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(tripods));
                        apiSearchArray.add(jsonObject);
                    }

                    apiSearchTripod.FirstOption = skillOption.Value;
                    apiSearchTripod.SkillName = skillOption.Text;
                    apiSearchTripod.tripods = apiSearchArray;

                    JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(apiSearchTripod));
                    jsonArray.add(jsonObject);

                    classData.put(skillOption.Class , jsonArray);
                }
            }

            for (String className : classData.keySet()){
                byte[] b = classData.get(className).toString().getBytes();
                System.out.println("className : " +className + " size : " +b.length);
                makeFile("class/" + className ,b);
            }

            System.out.println("createClass Time " + (System.currentTimeMillis() - startTime) + "ms");
        }
    }

    public ArrayList<ApiSearchTripod> getCharacterDB(String className) {
        ArrayList<ApiSearchTripod> apiSearchTripods = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(className));
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
//                apiSearchTripod.tripods = tripods;
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
}
