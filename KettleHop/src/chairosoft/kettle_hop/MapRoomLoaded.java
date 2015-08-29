/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * MapRoomLoaded.java
 * MapRoomLoaded class definition
 */

package chairosoft.kettle_hop;

import chairosoft.ui.graphics.DrawingContext;

public class MapRoomLoaded extends GameState
{
    // Constructor
    public MapRoomLoaded(KettleHop app) { super(app); }
    
    // Instance Methods
    @Override
    public void update()
    {
        this.app.maproom = this.app.maproomLoaderResult.qMapRoom;
        this.app.protagonist.setPositionByQTile(this.app.maproomLoaderResult.spawnCol, this.app.maproomLoaderResult.spawnRow);
        this.app.gameState = this.app.NORMAL_GAMEPLAY;
    }
}