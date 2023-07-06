package com.example.cardiacrecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that keeps track of a list of city objects
 */
public class DetailsClass {
    private List<Details> detailsList = new ArrayList<>();

    /**
     * This adds a record to the list if that record does not exist
     * @param details
     *      This is the record to add
     */
    public void addRecord(Details details) {
        if (detailsList.contains(details)) {
            throw new IllegalArgumentException();
        }
        detailsList.add(details);
    }

    /**
     * This deletes a record from the list if that record exists
     * @param city
     *      This is the record to delete
     */
    public void deleteRecord(Details city) {
        if (!detailsList.contains(city)) {
            throw new IllegalArgumentException();
        }
        detailsList.remove(city);
    }

    /**
     * This returns the record list
     * @return
     * This is the record to be return
     */
    public List<Details> getDetails() {
        List<Details> detailsList1 = detailsList;
//        Collections.sort(detailsList1);
        return detailsList1;
    }

    /**
     * This returns the total number of record
     * @return
     * This is the number that returns number of record
     */
    public int detailscount(){
        return detailsList.size();
    }
}
