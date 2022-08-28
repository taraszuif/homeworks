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
import me.zuif.hw2.model.tea.Tea;
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
public class TeaRepositoryMongo implements ProductRepository<Tea> {
    private static TeaRepositoryMongo instance;
    private final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private final MongoCollection<Document> collection;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsString() + " 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH)))
            .create();

    @Autowired
    public TeaRepositoryMongo() {
        collection = DATABASE.getCollection(Tea.class.getSimpleName());
    }

    public static TeaRepositoryMongo getInstance() {
        if (instance == null) {
            instance = new TeaRepositoryMongo();
        }
        return instance;
    }

    @Override
    public void save(Tea tea) {
        collection.insertOne(Document.parse(gson.toJson(tea)));
    }

    @Override
    public void saveAll(List<Tea> teas) {
        List<Document> dteas = new ArrayList<>();
        for (Tea tea : teas) {
            dteas.add(Document.parse(gson.toJson(tea)));
        }

        collection.insertMany(dteas);
    }

    @Override
    public List<Tea> findAll() {
        return collection.find()
                .map(x -> gson.fromJson(x.toJson(), Tea.class))
                .into(new ArrayList<>());
    }

    @Override
    public Optional<Tea> findById(String id) {
        Bson filter = Filters.eq("id", id);
        return collection.find(filter).map(x -> gson.fromJson(x.toJson(), Tea.class))
                .into(new ArrayList<>()).stream().findFirst();
    }

    @Override
    public boolean update(Tea tea) {
        Bson filter = Filters.eq("id", tea.getId());
        Bson updates = Updates.combine(
                Updates.set("count", tea.getCount()),
                Updates.set("title", tea.getTitle()),
                Updates.set("price", tea.getPrice()),
                Updates.set("teaType", tea.getTeaType().name()),
                Updates.set("brand", tea.getBrand().name()));
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
