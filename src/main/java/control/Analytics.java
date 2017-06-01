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
    public ArrayList<Item> filterUnfinished(Database a){
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

    public ArrayList<Item> filterFinished(Database a){
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
        for(int i = 0; i < orig.size(); i++){
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

    public ArrayList<Item> sortPriority(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();
        ArrayList<Item> pA = new ArrayList<Item>();
        ArrayList<Item> pB = new ArrayList<Item>();
        ArrayList<Item> pC = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.HIGH){
                pA.add(orig.get(i));
            }
            else if(orig.get(i).getPriority()== Item.Priority.MEDIUM){
                pB.add(orig.get(i));
            }
            else{
                pC.add(orig.get(i));
            }
        }

        for(int i=0;i<pA.size();i++){
            out.add(pA.get(i));
        }
        for(int i=0;i<pB.size();i++){
            out.add(pB.get(i));
        }
        for(int i=0;i<pC.size();i++){
            out.add(pC.get(i));
        }

        return out;

    }

    public ArrayList<Item> filterHIGH(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.HIGH){
                out.add(orig.get(i));
            }
        }
        return out;
    }

    public ArrayList<Item> filterMEDIUM(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.MEDIUM){
                out.add(orig.get(i));
            }
        }
        return out;
    }

    public ArrayList<Item> filterLOW(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.LOW){
                out.add(orig.get(i));
            }
        }
        return out;
    }



    public int itemCount(Database a){
        return a.getItems().size();
    }

}
