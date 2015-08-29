/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * GameState.java
 * GameState abstract class definition
 * 
 */

package chairosoft.kettle_hop;

import chairosoft.ui.event.ButtonEvent;
import chairosoft.ui.graphics.DrawingContext;

public abstract class GameState
{
    // Instance Fields
    protected KettleHop app;
    
    // Constructor
    public GameState(KettleHop app) { this.app = app; }
    
    // Instance Methods
    
    public void updateInit() { }
    
    public void pointerPressed(float x, float y) { }
    public void pointerMoved(float x, float y) { }
    public void pointerReleased(float x, float y) { }
    
    public void buttonPressed(ButtonEvent.Code buttonCode) { }
    public void buttonHeld(ButtonEvent.Code buttonCode) { }
    public void buttonReleased(ButtonEvent.Code buttonCode) { }
    public void buttonNotHeld(ButtonEvent.Code buttonCode) { }
    
    public void update() { }
    public void render(DrawingContext ctx) { }
}