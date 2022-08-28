package me.zuif.hw2.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConfig {
    private static final String URL = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE = "test";
    private static MongoClient mongoClient;
    private static MongoDatabase db;

    public static MongoDatabase getMongoDatabase() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(URL, PORT);
            db = mongoClient.getDatabase(DATABASE);
            db.drop();
        }

        return db;
    }
}
