package com.example.app.recovery.model;

/**
 * Created by app on 16/5/29.
 */
public class Paper {
    private String pape_code;
    private String car_license;
    private String report_code;
    private Integer garage_id;
    private Integer company_id;
    private Integer type;

    public String getPape_code() {
        return pape_code;
    }

    public void setPape_code(String pape_code) {
        this.pape_code = pape_code;
    }

    public String getCar_license() {
        return car_license;
    }

    public void setCar_license(String car_license) {
        this.car_license = car_license;
    }

    public String getReport_code() {
        return report_code;
    }

    public void setReport_code(String report_code) {
        this.report_code = report_code;
    }

    public Integer getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(Integer garage_id) {
        this.garage_id = garage_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "pape_code='" + pape_code + '\'' +
                ", car_license='" + car_license + '\'' +
                ", report_code='" + report_code + '\'' +
                ", garage_id=" + garage_id +
                ", company_id=" + company_id +
                ", type=" + type +
                '}';
    }
}
