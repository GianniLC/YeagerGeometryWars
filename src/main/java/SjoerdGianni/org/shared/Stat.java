package SjoerdGianni.org.shared;

public class Stat<T> {
    private final T baseValue;
    private T value;
    private long resetTimestamp;

     public Stat(T baseValue){
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

    /**
     * Check if a temporary effect is currently active
     * @return true if the stat has a temporary value applied, false otherwise
     */
    public boolean isActive(){
        return value != baseValue;
    }

    /**
     * Get the remaining duration percentage of the temporary effect
     * @param currentTimestamp current time as a timestamp
     * @param totalDuration total duration of the effect in milliseconds
     * @return percentage from 0.0 to 1.0 representing remaining time
     */
    public double getRemainingPercentage(long currentTimestamp, long totalDuration){
        if (!isActive() || totalDuration <= 0){
            return 0.0;
        }
        long remainingTime = resetTimestamp - currentTimestamp;
        if (remainingTime <= 0){
            return 0.0;
        }
        return Math.min(1.0, (double)remainingTime / totalDuration);
    }
}
