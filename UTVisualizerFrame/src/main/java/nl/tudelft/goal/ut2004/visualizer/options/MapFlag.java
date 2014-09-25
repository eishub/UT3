package nl.tudelft.goal.ut2004.visualizer.options;

/**
 * List of preferrences flags related to links between waypoints in unreal map.
 */
public enum MapFlag {
    WALK("mapFlagWalk", 1, true),
    FLY("mapFlagFlyFly", 2, false),
    SWIM("mapFlagSwim", 4, false),
    JUMP("mapFlagJump", 8, false),
    DOOR("mapFlagDoor", 16, true),
    SPECIAL("mapFlagSpecial", 32, true),
    LADDER("mapFlagLadder", 64, true),
    PROSCRIBED("mapFlagProscribed", 128, true),
    FORCED("mapFlagForced", 256, true),
    PLAYER_ONLY("mapFlagPlayerOnly", 512, true);

    private final String prefKey;
    private final int flag;
    private final boolean defaultValue;

    private MapFlag(String prefKey, int flag, boolean defaultValue) {
        this.prefKey = prefKey;
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    /**
     * Get preferences key for TimelinePanel.
     * <p>
     * Get the value of preference through NbPreferences.forModule(TimelinePanel.class).getBoolean(String prefKey, boolean ifNothingSet)
     * @return
     */
    public String getPrefKey() {
        return prefKey;
    }

    /**
     * Get default value of this flag
     * @return wheather to show or not show edges wioth this flag
     */
    public boolean getDefault() {
        return defaultValue;
    }

    /**
     * Return int value that is used by unreal engine to represent this flag
     * in waylinks.
     * @return 1 bit integer (2^n, like 1,2,4,8,..)
     */
    public int getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return prefKey;
    }
}