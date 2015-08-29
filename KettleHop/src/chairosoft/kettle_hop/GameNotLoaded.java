/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * GameNotLoaded.java
 * GameNotLoaded class definition
 */

package chairosoft.kettle_hop;

import chairosoft.ui.event.ButtonEvent;
import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.DrawingImage;

import chairosoft.util.Loading;

public class GameNotLoaded extends GameState
{
    // Instance Fields
    public final IntPoint2D cycleBarOrigin = new IntPoint2D(7, 20);
    // public final DrawingImage startScreenImage = Loading.getImage("/img/bg/KettleHopStartScreen.png");
    // public final int startScreenImageX = (this.app.getPanelWidth() - this.startScreenImage.getWidth()) / 2;
    // public final int startScreenImageY = (this.app.getPanelHeight() - this.startScreenImage.getHeight()) / 2;
    
    // Constructor
    public GameNotLoaded(KettleHop app)
    {
        super(app);
    }

    // Instance Methods
    @Override
    public void pointerPressed(float x, float y)
    {
        this.app.loadNormalGameplay();
    }
    
    @Override
    public void buttonPressed(ButtonEvent.Code buttonCode)
    {
        this.app.loadNormalGameplay();
    }
    
    @Override
    public void render(DrawingContext ctx)
    {
        // background color
        ctx.setColor(Color.BLACK);
        ctx.fillRect(0, 0, this.app.getPanelWidth(), this.app.getPanelHeight());
        
        // // start screen image
        // ctx.drawImage(this.startScreenImage, this.startScreenImageX, this.startScreenImageY);
        
        // cycle bar
        ctx.setColor(Color.WHITE);
        ctx.drawRect(this.cycleBarOrigin.x, this.cycleBarOrigin.y, 102, 8); // outline
        ctx.fillRect(this.cycleBarOrigin.x + 1 + (int)(this.app.getFramesElapsedTotal() % 100), this.cycleBarOrigin.y, 2, 8); // cycle
    }
}