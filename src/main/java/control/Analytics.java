package main.java.control;

import main.java.util.AlphanumComparator;
import main.java.util.Util;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Provides information and filtered/sorted lists based on the database.
 *
 * @author Adrian Hardt & Logan Traffas
 */

public class Analytics {
	/**
	 * Enumerator to store which sort mode is active
	 */
	public enum SortMode{
		NAMEAZ, NAMEZA,PRIORITY,DATE;

		/**
		 * Displays a SortMode as a String
		 * @return String of SortMode
		 */
		@Override
		public String toString(){
			switch (this){
				case NAMEAZ:
					return "Name A-Z";
				case NAMEZA:
					return "Name Z-A";
				case DATE:
					return "Date";
				case PRIORITY:
					return "Priority";
				default:
					Util.nyi(Util.getFileName(),Util.getLineNumber());
					return null;//this line will never be reached
			}
		}
	}

	/**
	 * Enumerator to store which filterMode is active
	 */
    public enum FilterMode{
		NONE,HIGH_PRIORITY,MEDIUM_PRIORITY,LOW_PRIORITY,FINISHED,UNFINISHED;

		/**
		 * Shows the FilterMode as a String
		 * @return FilterMode String
		 */
		@Override
		public String toString(){
			switch (this){
				case NONE:
					return "None";
				case HIGH_PRIORITY:
					return "High Priority";
				case MEDIUM_PRIORITY:
					return "Medium Priority";
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

	/**
	 * Sorts the Database by a specified sortmode
	 * @param mode Desired SortMode
	 * @param in Database input
	 * @return Sorted ArrayList<PositionedItem>
	 */
	public static ArrayList<Database.PositionedItem> sort(SortMode mode, ArrayList<Database.PositionedItem> in){
	   if(mode==SortMode.PRIORITY){
	        return sortPriority(in);
        }
        else if(mode==SortMode.DATE){
	        return sortDate(in);
        }
        else if(mode==SortMode.NAMEAZ){
        	return sortNameAZ(in);
		}
		else if(mode==SortMode.NAMEZA){
			return sortNameZA(in);
		}
		Util.nyi(Util.getFileName(),Util.getLineNumber());
        return new ArrayList<>();//this line will never be reached
	}

	/**
	 * Filters a list of items
	 * @param mode FilterMode to filter
	 * @param in the arraylist to filter
	 * @return filtered arraylist
	 */
    public static ArrayList<Database.PositionedItem> filter(FilterMode mode,ArrayList<Database.PositionedItem> in){
        if(mode==FilterMode.NONE)return in;
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
		return new ArrayList<>();//this line will never be reached
    }

	/**
	 * Filters into a list of just unfiltered items.
	 * @param orig original ArrayList to filter
	 * @return filtered arraylist
	 */
	public static ArrayList<Database.PositionedItem> filterUnfinished(ArrayList<Database.PositionedItem> orig){
        ArrayList<Database.PositionedItem> out = new ArrayList<>();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getItem().getStatus()== Item.Status.UNFINISHED){
                out.add(orig.get(i));
                tally++;
            }
        }
        return out;
    }

	/**
	 * Gets number of unfinished items in the database
	 * @param a input database
	 * @return number of unfinished items in the database
	 */
	public int numUnfinished(Database a){
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getStatus()== Item.Status.UNFINISHED){
                tally++;
            }
        }
        return tally;
    }

	/**
	 * Filters the finished items
	 * @param orig arraylist of positioneditems to filter
	 * @return filtered list
	 */
	public static ArrayList<Database.PositionedItem> filterFinished(ArrayList<Database.PositionedItem> orig){
        ArrayList<Database.PositionedItem> out = new ArrayList<>();
        int tally=0;
        for(int i=0;i<orig.size();i++){
            if(orig.get(i).getItem().getStatus() == Item.Status.FINISHED){
                out.add(orig.get(i));
                tally++;
            }
        }
        return out;
    }

	/**
	 * Finds number of finished items
	 * @param a database to analyze
	 * @return number of finished items
	 */
	public int numFinished(Database a){
        ArrayList<Item> orig = a.getItems();
        int tally=0;
        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getStatus() == Item.Status.FINISHED){
                tally++;
            }
        }
        return tally;
    }

	/**
	 * Sorts by date
	 * @param in arraylist of positioned items to sort
	 * @return sorted arraylist
	 */
	public static ArrayList<Database.PositionedItem> sortDate(ArrayList<Database.PositionedItem> in){
        in.sort(Comparator.comparing(x -> x.getItem().getDate()));
        return in;
    }

	/**
	 * Sorts alphabetically
	 * @param in ArrayList of positioneditems to sort
	 * @return sorted arraylist
	 */
	public static ArrayList<Database.PositionedItem> sortNameAZ(ArrayList<Database.PositionedItem> in){
		in.sort((Database.PositionedItem o1, Database.PositionedItem o2) ->
				(new AlphanumComparator()).compare(o1.getItem().getDisplayName(),o2.getItem().getDisplayName())
		);
		return in;
	}

	/**
	 * Sort reverse alphabetically
	 * @param in input arraylist of positionedtems to sort
	 * @return sorted arraylist
	 */
	public static ArrayList<Database.PositionedItem> sortNameZA(ArrayList<Database.PositionedItem> in){
		in.sort((Database.PositionedItem o1, Database.PositionedItem o2) ->
				(new AlphanumComparator()).reversed().compare(o1.getItem().getDisplayName(),o2.getItem().getDisplayName())
		);
		return in;
	}

	/**
	 * Sorts by priority
	 * @param orig arraylist to sort
	 * @return sorted arraylist
	 */
    public static ArrayList<Database.PositionedItem> sortPriority(ArrayList<Database.PositionedItem> orig){
        ArrayList<Database.PositionedItem> out = new ArrayList<>();
        ArrayList<Database.PositionedItem> pA = new ArrayList<>();
        ArrayList<Database.PositionedItem> pB = new ArrayList<>();
        ArrayList<Database.PositionedItem> pC = new ArrayList<>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getItem().getPriority()== Item.Priority.HIGH){
                pA.add(orig.get(i));
            }
            else if(orig.get(i).getItem().getPriority()== Item.Priority.MEDIUM){
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

	/**
	 * Filters by HIGH priority
	 * @param orig arraylist to filter
	 * @return filtered arraylist
	 */
	public static ArrayList<Database.PositionedItem> filterHIGH(ArrayList<Database.PositionedItem> orig){
		ArrayList<Database.PositionedItem> out = new ArrayList<>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getItem().getPriority()== Item.Priority.HIGH){
                out.add(orig.get(i));
            }
        }
        return out;
    }

	/**
	 * Filters by MEDIUM priority
	 * @param orig arraylist to filter
	 * @return filtered arraylist
	 */
    public static ArrayList<Database.PositionedItem> filterMEDIUM(ArrayList<Database.PositionedItem> orig){
        ArrayList<Database.PositionedItem> out = new ArrayList<>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getItem().getPriority()== Item.Priority.MEDIUM){
                out.add(orig.get(i));
            }
        }
        return out;
    }

	/**
	 * Filters by LOW priority
	 * @param orig arraylist to filter
	 * @return filtered arraylist
	 */
    public static ArrayList<Database.PositionedItem> filterLOW(ArrayList<Database.PositionedItem> orig){
        ArrayList<Database.PositionedItem> out = new ArrayList<>();

        for(int i = 0; i < orig.size(); i++){
            if(orig.get(i).getItem().getPriority()== Item.Priority.LOW){
                out.add(orig.get(i));
            }
        }
        return out;
    }

	/**
	 * Counts number of items in Database
	 * @param a input databasse
	 * @return number of items
	 */
	public int itemCount(Database a){
        return a.getItems().size();
    }

}
