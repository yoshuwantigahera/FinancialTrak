package com.pluralsight;

public class Search {
    String startDate;
    String endDate;
    String description;
    double amount;

    public Search(String _startDate, String _endDate, String _description, double _amount){
        this.startDate = _startDate;
        this.endDate = _endDate;
        this.description = _description;
        this.amount = _amount;
    }

    public Search(){


    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
