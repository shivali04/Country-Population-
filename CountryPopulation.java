import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;   

public class Solution {
    /*
     * Complete the function below.
     * https://jsonmock.hackerrank.com/api/countries/search?name=
     */
    
    private static String ENDPOINT = "https://jsonmock.hackerrank.com/api/countries/search?name=";
   
    static int getCountries(String s, int p) throws Exception {
       
        int greaterThanGivenPopulation = 0;
        URL url = new URL(ENDPOINT + s);
        
        JSONObject jsonObject = getURLData(url);
        
        Long total_pages = 1L;
        for(Object key : jsonObject.keySet())
        {           
            String strKey = (String)key;
            if(strKey.equals("total_pages"))           
                total_pages = (Long)jsonObject.get(strKey);                                                                
        }
        
        for(int page = 1;page<=total_pages;page++)
        {
            URL pageUrl = new URL(ENDPOINT + s + "&page="+page);
            JSONObject pageUrlObject = getURLData(pageUrl);
            greaterThanGivenPopulation += countForEachPage(pageUrlObject,p);
        }
                         
        return greaterThanGivenPopulation;
    }
    
    public static int countForEachPage(JSONObject pageJsonObject,int p)
    {
        int currentPageCountryCount = 0;
        for(Object key : pageJsonObject.keySet()){
            String strKey = (String)key;
            if(strKey.equals("data"))
            { 
                JSONArray countries = (JSONArray)pageJsonObject.get(strKey);
                Iterator itr = countries.iterator();
                while(itr.hasNext()){
                    JSONObject country = (JSONObject)itr.next();
                    Long population = (Long) country.get("population");                   
                    if (population > p){
                        currentPageCountryCount++;
                    }
                }
            }        
        }
       return currentPageCountryCount;
    }
    
    public static JSONObject getURLData(URL url) throws Exception 
    {         
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);       
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = "";
        StringBuilder responseStr = new StringBuilder();
        
        while((line = reader.readLine()) != null  )
        {
             responseStr.append(line);
        }
        reader.close();
        
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject)parser.parse(responseStr.toString());
        
        return object;
    }
    public static void main(String[] args) throws IOException{
