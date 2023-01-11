import java.io.*;
import java.util.*;

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
//한
    public ArrayList<String> getSkillBookName(String name) {
        if (db.size() == 0){
            readDB();
        }
        long startTime = System.currentTimeMillis();
        ArrayList<String> data = new ArrayList<>();

        Set<String> set = db.get("SkillBook").keySet();
        set.forEach(skillName -> {
            if(debug) System.out.println("db name : "+skillName+" inputName : "+name);
            int temp = 0;
            int index = -1;
            for (int i = 0; i <name.length(); i++){
                for (int j = 0; j < skillName.length(); j++){
                    if (name.charAt(i) == skillName.charAt(j)){
                        if (index > j){
                            if (debug) System.out.println("index > j");
                            break;
                        }
                        temp++;
                        index = j;
                    }
                }
                if (index == -1){
                    break;
                }
            }
            if (temp == (name.length())){
                if (debug) System.out.println("add dataArray -> " + skillName);
                data.add(skillName);
            }
        });
        long endTime = System.currentTimeMillis();
        if (debug) System.out.println("DB : SkillBookName time : " + (endTime - startTime) + "ms");
        if (debug) System.out.println(data);
        return data;
    }

}
