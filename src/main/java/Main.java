import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.lang.reflect.Array;
import java.security.KeyPair;
import java.util.*;

public class Main {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public User() {
        }

        public String type;
        public Actor actor;
        public Repo repo;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Actor {
            public String login;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Repo {
            public String name;
        }
    }


    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        boolean init = false;
        String dictAdress = "";
        boolean user = false;
        String userName = "";
        String repoName = "";
        boolean event = false;
        String eventName = "";
        boolean repo = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-i") || args[i].equals("--init")) {
                init = true;
                dictAdress = args[i + 1];
            }
            if (args[i].equals("-u") || args[i].equals("--user")) {
                user = true;
                userName = args[i + 1];
            }
            if (args[i].equals("-e") || args[i].equals("--event")) {
                event = true;
                eventName = args[i + 1];
            }
            if (args[i].equals("-r") || args[i].equals("--repo")) {
                repo = true;
                repoName = args[i + 1];
            }
        }

        if(!init&&!user&&!repo){
            System.out.println("Hello World!");
        }

        if (init) {
            String fileName = getNameFromOneJsonFile(dictAdress);
            getJsonFile(fileName);
        }

        if (user && !repo) {

            System.out.println(find1(userName, eventName));
        }

        if (user && repo) {
            System.out.println(find3(userName, repoName, eventName));
        }

        if (repo && !user) {
            System.out.println(find2(repoName, eventName));
        }
    }

    public static Integer find1(String userName, String eventName) {
        String str = readJsonFile("1.txt");
        String[] piece = str.split(", ");
        for (String item : piece) {
            String[] i = item.split("=");
            String str1 = userName + eventName;
            if (i[0].equals(str1)) return Integer.parseInt(i[1]);
        }
        return 0;
    }

    public static Integer find2(String repoName, String eventName) {
        String str = readJsonFile("2.txt");
        String[] piece = str.split(", ");
        for (String item : piece) {
            String[] i = item.split("=");
            String str1 = repoName + eventName;
            if (i[0].equals(str1)) return Integer.parseInt(i[1]);
        }
        return 0;
    }

    public static Integer find3(String userName, String repoName, String eventName) {
        String str = readJsonFile("3.txt");
        String[] piece = str.split(", ");
        for (String item : piece) {
            String[] i = item.split("=");
            String str1 = userName + repoName + eventName;
            if (i[0].equals(str1)) return Integer.parseInt(i[1]);
        }
        return 0;
    }

    public static void getJsonFile(String dict_address) throws IOException {

        String str = readJsonFile(dict_address);

        ObjectMapper mapper = new ObjectMapper();

        String[] items = str.split("\n");

        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();
        for (int i = 0; i < items.length; ++i) {
            User user = mapper.readValue(items[i], User.class);

            String str1 = user.actor.login + user.type;
            if (map1.containsKey(str1)) {
                Integer j = map1.get(str1);
                map1.put(str1, j + 1);
            } else map1.put(str1, 1);

            String str2 = user.repo.name + user.type;
            if (map2.containsKey(str2)) {
                Integer z = map2.get(str2);
                map2.put(str2, z + 1);
            } else map2.put(str2, 1);

            String str3 = user.actor.login + user.repo.name + user.type;
            if (map3.containsKey(str3)) {
                Integer o = map3.get(str3);
                map3.put(str3, o + 1);
            } else map3.put(str3, 1);
        }
        //写入文件
        FileWriter fw = null;
        fw = new FileWriter("1.txt");
        fw.write(map1.toString().substring(1, map1.toString().length() - 1));
        fw = new FileWriter("2.txt");
        fw.write(map2.toString().substring(1, map2.toString().length() - 1));
        fw = new FileWriter("3.txt");
        fw.write(map3.toString().substring(1, map3.toString().length() - 1));
        fw.close();

    }

    public static String getNameFromOneJsonFile(String dict_address) {
        File directory = new File(dict_address);
        if (!directory.isDirectory()) {
            throw new RuntimeException('"' + dict_address + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");
        } else {
            //首先将第一层目录扫描一遍
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().startsWith(".")){
                        continue;
                    }
                if (files[i].getName().endsWith("json")) {
                    return files[i].getName();
                }
            }

        }
            return null;
    }
}


