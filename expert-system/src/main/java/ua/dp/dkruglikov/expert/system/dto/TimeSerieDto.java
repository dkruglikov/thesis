package ua.dp.dkruglikov.expert.system.dto;

public class TimeSerieDto {
    
    private String timestamps;
    private String values;

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
