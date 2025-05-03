package data;

import android.content.Context;

import com.davidfreemangames.idleindustrialist.MainFactory;
import com.google.gson.Gson;
import java.io.*;
public class FileHelper {
    private static final String FILE_NAME = "mainFactory.json";
    public static void saveMainFactory(Context context, MainFactory mainFactory){
        Gson gson = new Gson();
        String json = gson.toJson(mainFactory);
        
        try(FileWriter writer = new FileWriter(new File(context.getFilesDir(), FILE_NAME))){
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static MainFactory loadMainFactory(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if(!file.exists()) return null;
        
        try (FileReader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, MainFactory.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


//        appDatabase = DatabaseHelper.getDatabase(this);
//        userInfoDao = appDatabase.userInfoDao();

// Check if products table has rows if so add them
// Add code to take product fields and create those products and add them to
// the products array list that will be sent
// Do the same for the technologies array list


//        if(userInfoDao.getNumUserInfoRows() < 1){
//            Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat,0);
//            mainFactory = new MainFactory(wheatProduct);
//        } else {
//            System.out.println("ALL OF THE DATA IN THE DATABASE");
//            System.out.println(userInfoDao.getUserSettings());
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    final UserInfo userInfo = userInfoDao.getUserSettings();
//                    // Handle the result on the main thread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mainFactory.setProduct(userInfo.productId);
//                        }
//                    });
//                }
//            });
//        }