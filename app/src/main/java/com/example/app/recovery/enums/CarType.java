package com.example.app.recovery.enums;

/**
 * Created by app on 16/5/29.
 */
public enum CarType {
    bbxc("被保险车", 1),
    bdc("标的车", 2);
    private String context;
    private Integer type;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    private CarType(String context, Integer type) {
        this.context = context;
        this.type = type;
    }

    public static Integer findType(String context) {
        Integer type = null;
        for (CarType carType : CarType.values()) {
            if (carType.getContext().equals(context)) {
                type = carType.getType();
            }
        }
        return type;
    }
}
