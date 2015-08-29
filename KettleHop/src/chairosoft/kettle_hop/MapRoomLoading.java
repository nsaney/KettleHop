/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * MapRoomLoading.java
 * MapRoomLoading class definition
 */

package chairosoft.kettle_hop;

import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;

public class MapRoomLoading extends GameState
{
    // Instance Fields
    public final IntPoint2D cycleBarOrigin = new IntPoint2D(7, 12);
    
    // Constructor
    public MapRoomLoading(KettleHop app) { super(app); }
    
    // Instance Methods
    @Override
    public void render(DrawingContext ctx)
    {
        // cycle bar
        ctx.setColor(Color.RED);
        ctx.drawRect(this.cycleBarOrigin.x, this.cycleBarOrigin.y, 102, 8); // outline
        ctx.fillRect(this.cycleBarOrigin.x + 1 + (int)(this.app.getFramesElapsedTotal() % 100), this.cycleBarOrigin.y, 2, 8); // cycle
    }
}