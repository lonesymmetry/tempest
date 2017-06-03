package main.java.control;

import main.java.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Provides information and filtered/sorted lists based on the database.
 *
 * @author Adrian Hardt & Logan Traffas
 */

public class Analytics {
	public enum SortMode{
		NONE,NAME,PRIORITY,DATE;

		@Override
		public String toString(){
			switch (this){
				case NAME:
					return "Name";
				case DATE:
					return "Date";
				case NONE:
					return "None";
				case PRIORITY:
					return "Priority";
				default:
					Util.nyi(Util.getFileName(),Util.getLineNumber());
					return null;//this line will never be reached
			}
		}
	}
    public enum FilterMode{
		NONE,HIGH_PRIORITY,MEDIUM_PRIORITY,LOW_PRIORITY,FINISHED,UNFINISHED;

		@Override
		public String toString(){
			switch (this){
				case NONE:
					return "None";
				case HIGH_PRIORITY:
					return "High Priority";
				case MEDIUM_PRIORITY:
					return "Medium None";
				case LOW_PRIORITY:
					return "Low Priority";
				case UNFINISHED:
					return "Unfinished";
				case FINISHED:
					return "Finished";
				default:
					Util.nyi(Util.getFileName(),Util.getLineNumber());
					return null;//this line will never be reached
			}
		}
	}

    public static ArrayList<Item> sort(SortMode mode,ArrayList<Item> in){//TODO: return an array of PositionedItems not Items
       return sort(mode,new Database(in));
    }

    public static ArrayList<Item> sort(SortMode mode,Database in){//TODO: return an array of PositionedItems not Items
	    if(mode==SortMode.NONE)return in.getItems();
	    else if(mode==SortMode.PRIORITY){
	        return sortPriority(in);
        }
        else if(mode==SortMode.DATE){
	        return sortDate(in);
        }
		Util.nyi(Util.getFileName(),Util.getLineNumber());
        return null;//this line will never be reached
	}

	public static ArrayList<Item> filter(FilterMode mode,ArrayList<Item> in){//TODO: return an array of PositionedItems not Items
		return filter(mode,new Database(in));
	}

    public static ArrayList<Item> filter(FilterMode mode,Database in){//TODO: return an array of PositionedItems not Items
        if(mode==FilterMode.NONE)return in.getItems();
        else if(mode==FilterMode.HIGH_PRIORITY){
            return filterHIGH(in);
        }
        else if(mode==FilterMode.MEDIUM_PRIORITY){
            return filterMEDIUM(in);
        }
        else if(mode==FilterMode.LOW_PRIORITY){
            return filterLOW(in);
        }
        else if(mode==FilterMode.FINISHED){
            return filterFinished(in);
        }
        else if(mode==FilterMode.UNFINISHED){
            return filterUnfinished(in);
        }
		Util.nyi(Util.getFileName(),Util.getLineNumber());
		return null;//this line will never be reached
    }

    public static ArrayList<Item> filterUnfinished(Database a){
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

    public static ArrayList<Item> filterFinished(Database a){
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

    public static ArrayList<Item> sortDate(Database a){
        ArrayList<Item> orig = a.getItems();
        Collections.sort(orig, Comparator.comparing(Item::getDate));
        return orig;
    }

    public static ArrayList<Item> sortPriority(Database a){
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

    public static ArrayList<Item> filterHIGH(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.HIGH){
                out.add(orig.get(i));
            }
        }
        return out;
    }

    public static ArrayList<Item> filterMEDIUM(Database a){
        ArrayList<Item> orig = a.getItems();
        ArrayList<Item> out = new ArrayList<Item>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getPriority()== Item.Priority.MEDIUM){
                out.add(orig.get(i));
            }
        }
        return out;
    }

    public static ArrayList<Item> filterLOW(Database a){
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
