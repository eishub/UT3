/*
 * Copyright (C) 2010 Unreal Visualizer Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.visualizer.gui.widgets;

import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.GameData;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;
import nl.tudelft.goal.ut2004.visualizer.util.SelectableMapList;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.MapList;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.collections.ObservableCollection;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * Smart Map selection box.
 *
 * @author M.P. Korstanje
 *
 */
public class MapBox extends JPanel {

    private final SuggestionField suggestionField;
    private final Listener listener;
    private final ObservableCollection<MapList> maps;
    private final SuggestionModel suggestionModel;

    public MapBox() {
        this.setLayout(new GridLayout(1, 1));
        this.listener = new Listener();
        this.suggestionModel = new SuggestionModel();
        this.suggestionField = new SuggestionField(suggestionModel);
        this.add(suggestionField);

        ServerController controller = ServerController.getInstance();
        GameData data = controller.getGameData();
        maps = data.getAvailableMaps();
        maps.addCollectionListener(listener);
        addAll(maps);

        controller.getServerDefinition().getServerFlag()
                .addListener(new FlagListener<IUT2004Server>() {
            @Override
            public void flagChanged(IUT2004Server changedValue) {
                if (changedValue != null) {
                    suggestionField.setSelectedItem(changedValue
                            .getMapName());
                }
            }
        });

        IUT2004Server server = controller.getServer();
        if (server != null) {
            suggestionField.setSelectedItem(server.getMapName());
        }
    }

    /**
     * Returns the way point selected by this combo box.
     *
     * @return the selected waypoint or null if none.
     */
    public MapList getSelected() {
        SelectableMapList sw = (SelectableMapList) suggestionModel
                .getSelectedItem();
        if (sw != null) {
            return sw.getMapList();
        }

        return null;
    }

    public void setSelected(MapList item) {
        suggestionField.setSelectedItem(item);
    }

    /**
     * Adds all ways points in a representable form to the list model.
     *
     * @param maps
     */
    private void addAll(Collection<MapList> maps) {
        synchronized (maps) {
            for (MapList w : maps) {
                suggestionModel.addElement(new SelectableMapList(w));
            }
        }
    }

    /**
     * Removes all ways points in a representable form to the list model.
     *
     * @param maps
     */
    private void removeAll(Collection<MapList> maps) {
        synchronized (maps) {
            for (MapList w : maps) {
                suggestionModel.removeElement(new SelectableMapList(w));
            }
        }
    }

    /**
     * Called when this panel will no longer be used. Useful for clean up.
     *
     * TODO: Currently only exists to accommodate the {@link WaypointBox} which
     * has no meaningfull way to detect when it is diposed and should remove
     * it's listeners. The {@link ObservableCollection} should use weak
     * references.
     *
     */
    public void dispose() {
        maps.removeCollectionListener(listener);
    }

    private class Listener extends CollectionEventAdaptor<MapList> {

        @Override
        public void postRemoveEvent(final Collection<MapList> alreadyRemoved,
                Collection<MapList> whereWereRemoved) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeAll(alreadyRemoved);

                }
            });
        }

        @Override
        public void postAddEvent(final Collection<MapList> alreadyAdded,
                Collection<MapList> whereWereAdded) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    addAll(alreadyAdded);

                }
            });
        }
    }
}
