package com.shuchi.springboot.demo.mycoolapp.rest;

import java.util.ArrayList;
import java.util.List;

public class PaginatedResult {
    private List<ContactRecords> records =new ArrayList<ContactRecords>();
       private String totalSize;
       private String done;
       private String nextRecordsUrl;
       public List<ContactRecords> getRecords() {
        return records;
    }
    public void setRecords(List<ContactRecords> records) {
        this.records = records;
    }
    public String getTotalSize() {
        return totalSize;
    }
    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }
    public String getDone() {
        return done;
    }
    public void setDone(String done) {
        this.done = done;
    }
    public String getNextRecordsUrl() {
        return nextRecordsUrl;
    }
    public void setNextRecordsUrl(String nextRecordsUrl) {
        this.nextRecordsUrl = nextRecordsUrl;
    }
        
    }