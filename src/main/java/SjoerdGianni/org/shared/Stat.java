package SjoerdGianni.org.shared;

public class Stat<T> {
    private final T baseValue;
    private T value;
    private long resetTimestamp;

    Stat(T baseValue){
        this.baseValue = baseValue;
        value = baseValue;
        resetTimestamp = 0;
    }

    public T getBaseValue() {
        return baseValue;
    }

    public T getValue(){
        return value;
    }

    /**
     * Overwrite the value with a temporary value which is active for a specified duration
     * @param temporaryValue temporary value to overwrite the current value with
     * @param durationInMs duration in milliseconds how long the temporary value should be active
     * @param currentTimestamp current time as a timestamp
     */
    public void applyTemporaryChange(T temporaryValue, long durationInMs, long currentTimestamp){
        value = temporaryValue;
        resetTimestamp = currentTimestamp + durationInMs;
    }

    /**
     * Update to check if the value needs to be reset to the base value
     * @param currentTimestamp current time as a timestamp
     */
    public void update(long currentTimestamp){
        if (value != baseValue && currentTimestamp >= resetTimestamp){
            value = baseValue;
        }
    }
}
