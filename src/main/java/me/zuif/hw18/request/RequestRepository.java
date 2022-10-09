package me.zuif.hw18.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class RequestRepository {
    private static RequestRepository instance;
    private final List<Request> requests = new ArrayList<>();


    public static RequestRepository getInstance() {
        if (instance == null) {
            instance = new RequestRepository();
        }
        return instance;
    }

    public void add(Request request) {
        requests.add(request);
    }

    public List<Request> findAll() {
        return requests;
    }
}
