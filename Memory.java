import java.util.*;

class Memory {
	//scanner is stored here as a static field so it is avaiable to the execute method for factor
	public static CoreScanner data;
	
	// Class and data structures to represent variables
	static class Variable {
		Core type;
		int integerVal;
		String defaultKey;
		Map<String, Integer> mapVal;
		Variable(Core t) {
			this.type = t;
		}
	}
	
	public static HashMap<String, Variable> global;
	public static Stack<HashMap<String, Variable>> local;

	// NEW: stack of call frames, each frame owns its own 'local' stack
	public static Stack<Stack<HashMap<String, Variable>>> frames;
	
	// Helper methods to manage memory
	
	// This inializes the global memory structure
	// Called before executing the DeclSeq
	public static void initializeGlobal() {
		global = new HashMap<String, Variable>();
	}
	
	// Initializes the local data structure
	// Called before executing the main StmtSeq
	public static void initializeLocal() {
		local = new Stack<HashMap<String, Variable>>();
		frames = new Stack<Stack<HashMap<String, Variable>>>();   // NEW
	}
	
	// Pushes a "scope" for if/loop stmts
	public static void pushScope() {
		local.push(new HashMap<String, Variable>());
	}
	
	// Pops a "scope"
	public static void popScope() {
		local.pop();
	}

	// NEW: call-frame management (used by procedure calls)
	public static void pushFrame() {
		frames.push(local);
		local = new Stack<HashMap<String, Variable>>();
	}
	public static void popFrame() {
		local = frames.pop();
	}
	
	// Handles decl integer
	public static void declareInteger(String id) {
		Variable v = new Variable(Core.INTEGER);
		if (local != null) {
			local.peek().put(id, v);
		} else {
			global.put(id, v);
		}
	}
	
	// Handles decl object
	public static void declareObject(String id) {
		Variable v = new Variable(Core.OBJECT);
		if (local != null) {
			local.peek().put(id, v);
		} else {
			global.put(id, v);
		}
	}
	
	// Retrives a value from memory (integer or default key value)
	public static int load(String id) {
		int value;
		Variable v = getLocalOrGlobal(id);
		if (v.type == Core.INTEGER) {
			value = v.integerVal;
		} else {
			value = v.mapVal.get(v.defaultKey);
		}
		return value;
	}
	
	// Retrieves a value for the key
	public static int load(String id, String key) {
		Variable v = getLocalOrGlobal(id);
		if (!v.mapVal.containsKey(key)) {
			System.out.println("ERROR: key " + key + " is not valid!");
			System.exit(0);
		}
		return v.mapVal.get(key);
	}
	
	// Stores a value (integer or object at default key)
	public static void store(String id, int value) {
		Variable v = getLocalOrGlobal(id);
		if (v.type == Core.INTEGER) {
			v.integerVal = value;
		} else if (v.mapVal != null) {
			v.mapVal.put(v.defaultKey, value);
		} else {
			System.out.println("ERROR: Assigning to null object " + id);
			System.exit(0);
		}
	}
	
	// Stores a value at key
	public static void store(String id, String key, int value) {
		Variable v = getLocalOrGlobal(id);
		v.mapVal.put(key, value);
	}
	
	// Handles "new object" assignment
	public static void allocate(String id, String key, int value) {
		Variable v = getLocalOrGlobal(id);
		v.mapVal = new HashMap<>();
		v.defaultKey = key;
		v.mapVal.put(v.defaultKey, value);
	}
	
	// Handles "id : id" assignment
	public static void alias(String lhs, String rhs) {
		Variable v1 = getLocalOrGlobal(lhs);
		Variable v2 = getLocalOrGlobal(rhs);
		v1.mapVal = v2.mapVal;
		v1.defaultKey = v2.defaultKey;
	}

	// NEW: get a direct ref (used to capture caller actuals before switching frames)
	public static Variable refOf(String id) {
		return getLocalOrGlobal(id);
	}
	// NEW: alias a name in the current frame to an existing Variable's object storage
	public static void aliasToRef(String lhs, Variable rhs) {
		Variable v1 = getLocalOrGlobal(lhs);
		v1.mapVal = rhs.mapVal;
		v1.defaultKey = rhs.defaultKey;
	}
	
	// Looks up value of the variables, searches local then global
	private static Variable getLocalOrGlobal(String id) {
		Variable result;
		if (local.size() > 0) {
			if (local.peek().containsKey(id)) {
				result = local.peek().get(id);
			} else {
				HashMap<String, Variable> temp = local.pop();
				result = getLocalOrGlobal(id);
				local.push(temp);
			}
		} else {
			result = global.get(id);
		}
		return result;
	}
}
