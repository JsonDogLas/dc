package com.cb.platform.yq.base.permission.test;

import abc.util.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Iterator;

public class Test {
    private static String PATH="D:\\我的文档\\Documents\\Tencent Files\\1308189967\\FileRecv\\点位2\\8989";
    private static String DATAJSON="Data.json";
    private static String outPath="test.json";
    public static String path1="C:\\Users\\Administrator\\Desktop\\临时\\user\\com";
    public static String path="C:\\Users\\Administrator\\Desktop\\临时\\oauth\\com";

    public  void mains(String[] str) throws IOException {
        String strs=FileUtils.readFileToString(new File(PATH+"/"+DATAJSON));
        JSONArray jsonArray=JSONArray.parseArray(strs);
        Iterator<Object> objectIterator= jsonArray.iterator();
        while (objectIterator.hasNext()){
            JSONObject jsonObject=(JSONObject)objectIterator.next();
            String path=jsonObject.getString("inner");
            File file=new File(PATH+path);
            if(file.exists()){
                jsonObject.put("exist","文件存在");
            }else{
                jsonObject.put("exist","文件不存在");
            }
        }
        FileUtils.writeStringToFile(new File(PATH+"/"+outPath),jsonArray.toJSONString());
    }



    public static void main(String[] agrs){
        file(new File(path1));
    }

    /**
     *
     * @param file
     */
    public static void file(File file){
        if(file.exists()){
            if(file.isDirectory()){
                File[] files=file.listFiles();
                for(File fileItem:files){
                    file(fileItem);
                }
            }else{
                changeCode(file);
            }
        }
    }

    public static void changeCode(File file){
        try {
            Reader reader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String info=bufferedReader.readLine();
            StringBuffer stringBuffer=new StringBuffer();
            while (StringUtils.isNotEmpty(info)){
                String test=info(info);
                if(StringUtils.isNotEmpty(test)){
                    stringBuffer.append(test);
                }else{
                    stringBuffer.append(info);
                }
                info=bufferedReader.readLine();
            }
            FileUtils.writeStringToFile(file,stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String info(String test){
        int start=test.indexOf("/* ");
        int end=test.indexOf("*/")+2;
        if(start != -1 && end != -1){
            return test.substring(end,test.length())+"\n";
        }
        return null;
    }
}
