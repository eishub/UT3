package nl.tudelft.goal.ut3.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.tudelft.goal.unreal.messages.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import nl.tudelft.goal.ut3.messages.FireMode;
import nl.tudelft.goal.ut2004.util.Team;

import com.google.inject.Inject;

import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.communication.connection.impl.socket.SocketConnection;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.base.component.bus.IComponentBus;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Category;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Group;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.AddInventory;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.ChangeTeam;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Pause;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Respawn;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.SetGameSpeed;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.SpawnActor;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Teleport;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.GameInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.UT2004WorldView;
import cz.cuni.amis.pogamut.ut2004.server.impl.UT2004Server;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import cz.cuni.amis.utils.exception.PogamutException;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter.Type;

public class EnvironmentControllerServer extends UT2004Server {

    private IWorldEventListener<GameInfo> pausedListener = new IWorldEventListener<GameInfo>() {
        @Override
        public void notify(GameInfo event) {
        }
    };

    @Inject
    public EnvironmentControllerServer(UT2004AgentParameters params,
            IAgentLogger agentLogger, IComponentBus bus,
            SocketConnection connection, UT2004WorldView worldView, IAct act) {
        super(params, agentLogger, bus, connection, worldView, act);

        worldView.addEventListener(GameInfo.class, pausedListener);

    }

    public void sendResumeGame() {
        Pause resume = new Pause(false, false);
        getAct().act(resume);
    }

    public void sendPausegame() {
        Pause pause = new Pause(true, false);
        getAct().act(pause);

    }

    @AsAction(name = "addInventory")
    public void addInventory(UnrealId id, String item) {
        AddInventory addInventory = new AddInventory(id, item);
        getAct().act(addInventory);

        // String message = String.format("%s was not a ItemType in the %s category.", group, category);
        // throw new PogamutException(message, this);
    }

    @AsAction(name = "respawn")
    public void respawn(UnrealId id, UnrealIdOrLocation unrealIdOrLocation,
            Rotation rotation) {

        ILocated location = unrealIdOrLocation.toILocated(getWorldView());

        if (location == null) {
            String message = String.format(
                    "Could not resolve %s to a location", unrealIdOrLocation);
            throw new PogamutException(message, this);
        }

        getAct().act(new Respawn(id, location.getLocation(), rotation));

    }

    @AsAction(name = "changeTeam")
    public void changeTeam(UnrealId id) {

        Player player = getWorldView().get(id, Player.class);

        if (player == null) {
            String message = String.format("Could not resolve %s to a player",
                    id);
            throw new PogamutException(message, this);
        }

        getAct().act(new ChangeTeam(id, 1 - player.getTeam()));

    }

    @AsAction(name = "setGameSpeed")
    public void setGameSpeed(Double speed) {
        getAct().act(new SetGameSpeed(speed));
    }

    @AsAction(name = "spawnItem")
    public void spawnItem(UnrealIdOrLocation unrealIdOrLocation,
            Category category, Group group) {

        ILocated location = unrealIdOrLocation.toILocated(getWorldView());

        if (location == null) {
            String message = String.format(
                    "Could not resolve %s to a location", unrealIdOrLocation);
            throw new PogamutException(message, this);
        }

        for (ItemType item : group.getTypes()) {
            if (item.getCategory().equals(category)) {
                SpawnActor spawn = new SpawnActor(location.getLocation(),
                        Rotation.ZERO, item.getName());
                getAct().act(spawn);
                return;
            }
        }

        String message = String.format(
                "%s was not a ItemType in the %s category.", group, category);
        throw new PogamutException(message, this);

    }

    @AsAction(name = "teleport")
    public void teleport(UnrealId id, UnrealIdOrLocation unrealIdOrLocation,
            Rotation rotation) {
        ILocated location = unrealIdOrLocation.toILocated(getWorldView());

        if (location == null) {
            String message = String.format(
                    "Could not resolve %s to a location", unrealIdOrLocation);
            throw new PogamutException(message, this);
        }

        getAct().act(new Teleport(id, location.getLocation(), rotation));
    }

    /**
     * <p>
     * Information about point in the map. Together these form a directed graph
     * that spans the entire map.
     * </p>
     * <p>
     * Type: Once
     * </p>
     *
     * <p>
     * Syntax: navPoint(UnrealID, location(X,Y,Z), [NeigsUnrealID])
     * <ol>
     * <li>UnrealID: The unique id of this navpoint.</li>
     * <li>Location: The location of this navpoint in the map.</li>
     * <li>[NeigsUnrealID]: A list of Id's for the neighbouring navpoints that
     * are reachable from this navpoint.</li>
     * </ol>
     * </p>
     *
     *
     */
    @AsPercept(name = "navPoint", multiplePercepts = true, filter = Type.ONCE)
    public Collection<Percept> navPoint() {
        Collection<NavPoint> navPoints = getWorldView().getAll(NavPoint.class)
                .values();
        List<Percept> percepts = new ArrayList<Percept>(navPoints.size());

        for (NavPoint p : navPoints) {
            percepts.add(new Percept(p.getId(), p.getLocation(), p
                    .getOutgoingEdges().keySet()));
        }

        return percepts;
    }

    /**
     * <p>
     * Percept provided when another bot becomes visible to this bot.
     * </p>
     *
     * <p>
     * Type: On change with negation.
     * </p>
     *
     * <p>
     * Syntax: bot(UnrealId, Team, location(X,Y,Z), Weapon, FireMode)
     * </p>
     *
     * <ul>
     * <li>UnrealId: Unique identifier for this bot assigned by Unreal.</li>
     * <li>Team: Either red or blue.</li>
     * <li>location(X,Y,Z): Location of the bot in the world.</li>
     * <li>Weapon: The weapon the bot is holding. TODO: Any of the
     * following:</li>
     * <li>FireMode: Mode of shooting, either primary, secondary or none.</li>
     * </ul>
     * </p>
     *
     *
     */
    @AsPercept(name = "bot", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
    public Collection<Percept> bot() {
        Collection<Player> visible = getWorldView().getAll(Player.class)
                .values();
        Collection<Percept> wrapped = new ArrayList<Percept>(visible.size());

        for (Player p : visible) {
            wrapped.add(new Percept(p.getId(), p.getName(), Team.valueOf(p
                    .getTeam()), p.getLocation(), UT3ItemType.getItemType(p
                    .getWeapon()), FireMode.valueOf(p.getFiring())));
        }

        return wrapped;
    }
}
