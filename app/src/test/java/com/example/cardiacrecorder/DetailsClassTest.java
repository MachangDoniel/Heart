package com.example.cardiacrecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DetailsClassTest {
    private DetailsClass mockdetailsList() {
        DetailsClass detailsList = new DetailsClass();
        detailsList.addRecord(MockDetails());
        return detailsList;
    }

    private Details MockDetails() {
        return new Details(80,120,80,1,"Normal","test1","28/05/2024","10.22");
    }

    @Test
    public void testAdd() {
        DetailsClass detailsList = mockdetailsList();
        assertEquals(1, detailsList.getDetails().size());

        Details details = new Details(81,121,81,2,"Normal","test2","28/05/2025","10.23");
        detailsList.addRecord(details);

        assertEquals(2, detailsList.getDetails().size());
        assertTrue(detailsList.getDetails().contains(details));
    }

    @Test
    public void testAddException() {
        DetailsClass detailsList = new DetailsClass();
        Details details = MockDetails();
        detailsList.addRecord(details);

        assertThrows(IllegalArgumentException.class, () -> {
            detailsList.addRecord(details);
        });
    }

    @Test
    public void testDelete() {
        DetailsClass detailsList = mockdetailsList();
        assertEquals(1, detailsList.getDetails().size());

        Details details = new Details(81,121,81,2,"Normal","test2","28/05/2025","10.23");
        detailsList.addRecord(details);
        assertEquals(2, detailsList.getDetails().size());

        detailsList.deleteRecord(details);
        assertEquals(1, detailsList.getDetails().size());
        assertTrue(!detailsList.getDetails().contains(details));
    }

    @Test
    public void testDeleteException() {
        DetailsClass detailsList = new DetailsClass();

        Details details = new Details(81,121,81,2,"Normal","test2","28/05/2025","10.23");
        detailsList.addRecord(details);
        assertEquals(1, detailsList.getDetails().size());
        detailsList.deleteRecord(details);

        assertThrows(IllegalArgumentException.class, () -> {
            detailsList.deleteRecord(details);
        });
    }
    @Test
    public void testDetailsCount(){
        DetailsClass detailsList = new DetailsClass();
        Details details = new Details(81,121,81,2,"Normal","test2","28/05/2025","10.23");
        detailsList.addRecord(details);
        assertEquals(1, detailsList.detailscount());
        detailsList.deleteRecord(details);
        assertEquals(0, detailsList.detailscount());
    }
}
