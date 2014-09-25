/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package nl.tudelft.goal.visualizer.ut3.util;

import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import java.awt.Menu;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO: JUnit does not run.
 * 
 * @author Michiel Hegemans
 */
public class MenuBuilderTest {
    private static final Point POINT = new Point(0,0);
    
    private Menu menu;    
    
    public MenuBuilderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        menu = UT3MenuBuilder.buildUT3NavPointMenu(null, POINT);
    }
    
    @After
    public void tearDown() {
    }
    /*
    @Test
    public void nameTest() {
        assertEquals("UT3", menu.getName());
    }
    
    @Test
    public void itemCountTest() {
        assertEquals(6, menu.getItemCount());
    }
    * */
}