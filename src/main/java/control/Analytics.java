package main.java.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Provides information and modified lists based on the database. Will be used for sorting/filtering later.
 *
 * @author Adrian Hardt
 */

public class Analytics {
    public ArrayList<Item> sortUnfinished(Database a){
        ArrayList<Item> out = new ArrayList<Item>();
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getStatus()== Item.Status.UNFINISHED){
                out.add(orig.get(i));
                tally++;
            }
        }
        System.out.println("Found "+tally+" UNFINISHED items.");
        return out;
    }

    public int numUnfinished(Database a){
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getStatus()== Item.Status.UNFINISHED){
                tally++;
            }
        }
        System.out.println("Found "+tally+" UNFINISHED items.");
        return tally;
    }

    public ArrayList<Item> sortFinished(Database a){
        ArrayList<Item> out = new ArrayList<Item>();
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getStatus() == Item.Status.FINISHED){
                out.add(orig.get(i));
                tally++;
            }
        }
        System.out.println("Found "+tally+" FINISHED items.");
        return out;
    }

    public int numFinished(Database a){
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getStatus() == Item.Status.FINISHED){
                tally++;
            }
        }
        System.out.println("Found "+tally+" FINISHED items.");
        return tally;
    }

    public ArrayList<Item> sortDate(Database a){
        ArrayList<Item> orig = a.getItems();
        Collections.sort(orig, Comparator.comparing(Item::getDate));
        return orig;
    }


    public int itemCount(Database a){
        return a.getItems().size();
    }

}
