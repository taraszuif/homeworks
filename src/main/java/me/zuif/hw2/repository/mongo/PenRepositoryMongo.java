package me.zuif.hw2.repository.mongo;

import com.google.gson.*;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.MongoDBConfig;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.repository.ProductRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Singleton
public class PenRepositoryMongo implements ProductRepository<Pen> {
    private static PenRepositoryMongo instance;
    private final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private final MongoCollection<Document> collection;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsString() + " 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH)))
            .create();

    @Autowired
    public PenRepositoryMongo() {
        collection = DATABASE.getCollection(Pen.class.getSimpleName());
    }

    public static PenRepositoryMongo getInstance() {
        if (instance == null) {
            instance = new PenRepositoryMongo();
        }
        return instance;
    }


    @Override
    public void save(Pen pen) {
        collection.insertOne(Document.parse(gson.toJson(pen)));
    }

    @Override
    public void saveAll(List<Pen> pens) {
        List<Document> dpens = new ArrayList<>();
        for (Pen pen : pens) {
            dpens.add(Document.parse(gson.toJson(pen)));
        }

        collection.insertMany(dpens);
    }

    @Override
    public List<Pen> findAll() {
        return collection.find()
                .map(x -> gson.fromJson(x.toJson(), Pen.class))
                .into(new ArrayList<>());
    }

    @Override
    public Optional<Pen> findById(String id) {
        Bson filter = Filters.eq("id", id);
        return collection.find(filter).map(x -> gson.fromJson(x.toJson(), Pen.class))
                .into(new ArrayList<>()).stream().findFirst();
    }

    @Override
    public boolean update(Pen pen) {
        Bson filter = Filters.eq("id", pen.getId());
        Bson updates = Updates.combine(
                Updates.set("count", pen.getCount()),
                Updates.set("title", pen.getTitle()),
                Updates.set("price", pen.getPrice()),
                Updates.set("penType", pen.getPenType().name()),
                Updates.set("color", pen.getColor().name()),
                Updates.set("brand", pen.getBrand().name()));
        try {
            collection.updateOne(filter, updates);
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }

    }

    @Override
    public boolean delete(String id) {
        Bson filter = Filters.eq("id", id);
        try {
            collection.deleteOne(filter);
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            return false;
        }

    }
}
