package com.example.nudge.models;

import java.util.Date;

public class VisitModel {

        String farmer_id,visitTitle,visit_time,id;
        Boolean visit_status;
        Date visitDate;

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getVisitTitle() {
        return visitTitle;
    }

    public void setVisitTitle(String visitTitle) {
        this.visitTitle = visitTitle;
    }

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getVisit_status() {
        return visit_status;
    }

    public void setVisit_status(Boolean visit_status) {
        this.visit_status = visit_status;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public VisitModel() {
    }

    public VisitModel(String farmer_id, String visitTitle, String visit_time, String id, Boolean visit_status, Date visitDate) {
        this.farmer_id = farmer_id;
        this.visitTitle = visitTitle;
        this.visit_time = visit_time;
        this.id = id;
        this.visit_status = visit_status;
        this.visitDate = visitDate;
    }
}
