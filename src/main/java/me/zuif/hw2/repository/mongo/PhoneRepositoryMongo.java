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
import me.zuif.hw2.model.phone.Phone;
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
public class PhoneRepositoryMongo implements ProductRepository<Phone> {
    private static PhoneRepositoryMongo instance;
    private final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private final MongoCollection<Document> collection;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsString() + " 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH)))
            .create();

    @Autowired
    public PhoneRepositoryMongo() {
        collection = DATABASE.getCollection(Phone.class.getSimpleName());
    }

    public static PhoneRepositoryMongo getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryMongo();
        }
        return instance;
    }


    @Override
    public void save(Phone phone) {
        collection.insertOne(Document.parse(gson.toJson(phone)));
    }

    @Override
    public void saveAll(List<Phone> phones) {
        List<Document> dphones = new ArrayList<>();
        for (Phone phone : phones) {
            dphones.add(Document.parse(gson.toJson(phone)));
        }

        collection.insertMany(dphones);
    }

    @Override
    public List<Phone> findAll() {
        return collection.find()
                .map(x -> gson.fromJson(x.toJson(), Phone.class))
                .into(new ArrayList<>());
    }

    @Override
    public Optional<Phone> findById(String id) {
        Bson filter = Filters.eq("id", id);
        return collection.find(filter).map(x -> gson.fromJson(x.toJson(), Phone.class))
                .into(new ArrayList<>()).stream().findFirst();
    }

    @Override
    public boolean update(Phone phone) {
        Bson filter = Filters.eq("id", phone.getId());
        Bson updates = Updates.combine(
                Updates.set("count", phone.getCount()),
                Updates.set("title", phone.getTitle()),
                Updates.set("price", phone.getPrice()),
                Updates.set("model", phone.getModel()),
                Updates.set("manufacturer", phone.getManufacturer().name()));
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
