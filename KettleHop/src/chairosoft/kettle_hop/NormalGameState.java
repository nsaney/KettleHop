/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * NormalGameState.java
 * NormalGameState class definition
 */


package chairosoft.kettle_hop;

import chairosoft.quadrado.QCompassDirection;
import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QTileset;

import chairosoft.ui.event.ButtonEvent;
import chairosoft.ui.geom.Point2D;
import chairosoft.ui.geom.FloatPoint2D;
import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.geom.Polygon;
import chairosoft.ui.geom.Rectangle;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.DrawingImage;
import chairosoft.ui.graphics.Font;

import chairosoft.util.Loading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NormalGameState extends GameState
{
    // Static Fields
    public static final String COORDINATE_FORMAT_STRING = "(%1$7.3f,%2$7.3f)";
    public static final Font MONOSPACED_PLAIN_12 = Font.create(Font.Family.MONOSPACED, Font.Style.PLAIN, 12);
    
    // Instance Fields
    public final IntPoint2D cycleBarOrigin = new IntPoint2D(7, 4);
    protected boolean setTerminalVelocityOnNextFrame = true;
    
    protected boolean show_bounding_box = false;
    
    protected boolean show_position = false;
    protected String positionString = "";
    protected String velocityString = "";
    protected String accelerationString = "";
    protected String lastMoveString = "";
    
    
    // Constructor
    public NormalGameState(KettleHop app) { super(app); }
    
    
    // Instance Methods
    @Override
    public void updateInit()
    {
        if (this.setTerminalVelocityOnNextFrame)
        {
            this.app.protagonist.setTerminalVelocity(2.0f, 3.5f); // terminal velocity
            this.setTerminalVelocityOnNextFrame = false;
        }
        
        boolean isOnGround = this.app.maproom.hasTileCollidingWith(this.app.protagonist.bottomBorder);
        if (isOnGround)
        {
            this.app.protagonist.land();
        }
        else
        {
            this.app.protagonist.jump(0f);
        }
    }
    
    @Override
    public void buttonPressed(ButtonEvent.Code buttonCode)
    {
        switch (buttonCode)
        {
            case B:
                this.app.protagonist.jump(-3.0f);
                break;
            case START:    
                this.app.protagonist.setPositionByQTile(this.app.maproomLoaderResult.spawnCol, this.app.maproomLoaderResult.spawnRow);
                break;
            case DEBUG_9:
                this.show_bounding_box = !this.show_bounding_box;
                break;
            case DEBUG_0:
                this.show_position = !this.show_position;
                break;
        }
    }
    
    @Override
    public void buttonHeld(ButtonEvent.Code buttonCode)
    {
        switch (buttonCode)
        {
            // case LEFT:
                // this.app.protagonist.instantaneouslyAccelerateX(-0.2f);
                // break;
            // case RIGHT:
                // this.app.protagonist.instantaneouslyAccelerateX(0.2f);
                // break;
        }
    }
    
    @Override
    public void update()
    {
        // info gathering
        QCompassDirection keypadDirection = this.app.keypad.getDirection();
        boolean isAgainstLeftWall = this.app.maproom.hasTileCollidingWith(this.app.protagonist.leftBorder);
        boolean isAgainstRightWall = this.app.maproom.hasTileCollidingWith(this.app.protagonist.rightBorder);
        
        // movement
        if (keypadDirection.IS_EAST && !isAgainstRightWall)
        {
            this.app.protagonist.moveRight();
        }
        else if (keypadDirection.IS_WEST && !isAgainstLeftWall)
        {
            this.app.protagonist.moveLeft();
        }
        else
        {
            this.app.protagonist.stopHorizontalMovement();
        }
        
        
        // physics
        this.app.protagonist.moveOneFrame();
        this.app.protagonist.resolveCollisionInQMapRoom(this.app.maproom, true, true);
        
        // physics debug
        if (this.show_position)
        {
            FloatPoint2D pp = this.app.protagonist.getPosition();
            FloatPoint2D pv = this.app.protagonist.getVelocity();
            FloatPoint2D pa = this.app.protagonist.getAcceleration();
            FloatPoint2D pl = this.app.protagonist.getLastMove();
            
            this.positionString = String.format(COORDINATE_FORMAT_STRING, pp.x, pp.y);
            this.velocityString = String.format(COORDINATE_FORMAT_STRING, pv.x, pv.y);
            this.accelerationString = String.format(COORDINATE_FORMAT_STRING, pa.x, pa.y);
            this.lastMoveString = String.format(COORDINATE_FORMAT_STRING, pl.x, pl.y);
        }
        
        // animation
        this.app.protagonist.advanceAnimationOneClick();
    }
    
    @Override
    public void render(DrawingContext ctx) 
    {
        // save current graphics settings
        int ctxColor = ctx.getColor();
        Font ctxFont = ctx.getFont();
        
        try
        {
            // content graphics 
            IntPoint2D p = this.app.protagonist.getIntCenterPosition();
            this.drawContent(this.app.maproomLoaderResult.contentImageContext);
            int clipX = this.app.getPanelHalfWidth() - p.x;
            int clipY = this.app.getPanelHalfHeight() - p.y;
            ctx.drawImage(this.app.maproomLoaderResult.contentImage, clipX, clipY);
            
            // position info
            if (this.show_position)
            {
                IntPoint2D pp = new IntPoint2D(this.app.PANEL_WIDTH / 2 + 8, this.app.PANEL_HEIGHT / 2 + 8);
                
                ctx.setColor(Color.create(0x7fffffff, true));
                ctx.fillRect(pp.x, pp.y - 20, 160, 160);
                
                ctx.setColor(Color.BLACK);
                ctx.setFont(MONOSPACED_PLAIN_12);
                ctx.drawString(this.positionString, pp.x, pp.y);
                ctx.drawString(this.velocityString, pp.x, pp.y + 20);
                ctx.drawString(this.accelerationString, pp.x, pp.y + 40);
                ctx.drawString(this.lastMoveString, pp.x, pp.y + 60);
            }
            
            // cycle bar
            ctx.setColor(Color.BLUE);
            ctx.drawRect(this.cycleBarOrigin.x, this.cycleBarOrigin.y, 102, 8); // outline
            ctx.fillRect(this.cycleBarOrigin.x + 1 + (int)(this.app.getFramesElapsedTotal() % 100), this.cycleBarOrigin.y, 2, 8); // cycle
        }
        finally
        {
            // put back graphics settings
            ctx.setFont(ctxFont);
            ctx.setColor(ctxColor);
        }
    }
    
    private void drawContent(DrawingContext ctx)
    {
        // save current graphics settings
        int ctxColor = ctx.getColor();
        Font ctxFont = ctx.getFont();
        
        try
        {
            // background and maproom
            ctx.setColor(this.app.maproom.getBackgroundColor());
            ctx.fillRect(0, 0, this.app.maproom.getWidthPixels(), this.app.maproom.getHeightPixels());
            this.app.maproom.drawToContext(ctx, 0, 0);
            
            // player sprite
            this.app.protagonist.drawToContextAtOwnPosition(ctx);
            
            // collision box
            if (this.show_bounding_box) 
            {
                ctx.setColor(Color.RED); ctx.fillPolygon(this.app.protagonist.bottomBorder); 
                ctx.setColor(Color.YELLOW); ctx.fillPolygon(this.app.protagonist.leftBorder);
                ctx.setColor(Color.BLUE); ctx.fillPolygon(this.app.protagonist.rightBorder);
                ctx.setColor(Color.GREEN); ctx.fillPolygon(this.app.protagonist); 
            }
        }
        finally
        {
            // put back graphics settings
            ctx.setFont(ctxFont);
            ctx.setColor(ctxColor);
        }
    }
}    