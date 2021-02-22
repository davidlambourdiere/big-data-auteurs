package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DBHelper {
    private static final String DBNAME = "extraction_donnee";
    private static MongoDatabase database = null;
    public static final String COLLECTION_AUTHORS="authors";
    public static final String COLLECTION_LINKS="links";
    static MongoDatabase getDatabase(){
        if(database == null) {
            MongoClient mongoClient = MongoClients.create("mongodb://extraction:extraction@localhost:27017/extraction_donnee");
            database = mongoClient.getDatabase(DBNAME);
        }
        return DBHelper.database;
    }
}
