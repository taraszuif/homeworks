package me.zuif.hw17;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter(onMethod_ = {@Synchronized})
@Setter(onMethod_ = {@Synchronized})
public class Factory {
    private static volatile Factory instance;
    private static Object mutex = new Object();
    private volatile int fuelCount;
    private volatile int detailCount;
    private volatile int detailConstructionCount;
    private volatile int programmedMicrochipsCount;

    private Factory() {
        fuelCount = 0;
        detailCount = 0;
        detailConstructionCount = 0;
        programmedMicrochipsCount = 0;
    }

    public static Factory getInstance() {
        Factory result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new Factory();
            }
        }
        return result;
    }
}
