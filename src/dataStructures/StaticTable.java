package dataStructures;

import java.util.HashMap;


/**
 * 
 * Data Structure for the static table used in code generation
 * @author Andrew Langford
 *
 */
public class StaticTable {
	
	//map from temp to static entry
	private HashMap<String, StaticEntry> table;
	
	//map from idiable to temp for convenience
	private HashMap<String,String> entryLookup;
	
	public StaticTable(){
		this.table = new HashMap<String, StaticEntry>();
		this.entryLookup = new HashMap<String, String>();
	}
	
	
	
	public String addEntry(String id, int scope, long offset, String type){
		String temp = "T" + (this.table.size());
		StaticEntry entry = new StaticEntry(temp, id, scope, offset ,type);
		table.put(temp, entry);
		entryLookup.put(id + "@" + scope, temp);
		return temp;
	}
	
	public StaticEntry getEntryFromTable(String temp){
		return table.get(temp);
	}
	
	public String getTempLocationFromVar(String id){
		return entryLookup.get(id);
	}
	
	public void setTemp(String oldTemp, String newTemp){
		StaticEntry entry = table.get(oldTemp);
		table.remove(oldTemp);
		table.put(newTemp, entry);
		entryLookup.put(entry.getVar() + "@" + entry.getScope(), newTemp);
	}
	
	
	/**
	 * 
	 * Class to hold data about the entries in a Static entry.
	 * @author Andrew Langford
	 *
	 */
	 public class StaticEntry{
		 
		 private String temp;
		 private String id;
		 private int scope;
		 private String type;
		 private long offset;
		 
		 public StaticEntry(String temp, String id, int scope, long offset, String type ){
			 this.temp = temp;
			 this.id = id;
			 this.scope = scope;
			 this.type = type;
			 this.offset = offset;
		 }

		/**
		 * @return the temp
		 */
		public String getTemp() {
			return temp;
		}

		/**
		 * @param temp the temp to set
		 */
		public void setTemp(String temp) {
			this.temp = temp;
		}

		/**
		 * @return the id
		 */
		public String getVar() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setVar(String id) {
			this.id = id;
		}

		/**
		 * @return the scope
		 */
		public int getScope() {
			return scope;
		}

		/**
		 * @param scope the scope to set
		 */
		public void setScope(int scope) {
			this.scope = scope;
		}

		/**
		 * @return the offset
		 */
		public long getOffset() {
			return offset;
		}

		/**
		 * @param offset the offset to set
		 */
		public void setOffset(long offset) {
			this.offset = offset;
		}
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			this.type = type;
		}
	
	}
	 
	 public int getSize(){
		 return table.size();
	 }
}
