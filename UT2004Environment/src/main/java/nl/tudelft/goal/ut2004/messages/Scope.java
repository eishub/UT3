package nl.tudelft.goal.ut2004.messages;

/**
 * Scope for chatting.
 * @author Michiel Hegemans
 */
public enum Scope {
    
    GLOBAL, TEAM;
    
    public static Scope valueOfIgnoreCase(String scope) {
        return valueOf(scope.toUpperCase());
    }
    
}
