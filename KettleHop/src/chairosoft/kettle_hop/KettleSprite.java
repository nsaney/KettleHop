/* 
 * Nicholas Saney 
 * 
 * Created: August 26, 2015
 * 
 * KettleSprite.java
 * KettleSprite class definition
 * 
 */

package chairosoft.kettle_hop;

import chairosoft.quadrado.QCollidable;
import chairosoft.quadrado.QSprite;

import chairosoft.ui.geom.FloatPoint2D;

public class KettleSprite extends QSprite
{
    // Instance Fields
    public final QCollidable bottomBorder = new QCollidable.IntOffset(0, 1);
    public final QCollidable leftBorder = new QCollidable.IntOffset(-1, 0);
    public final QCollidable rightBorder = new QCollidable.IntOffset(1, 0);
    private final QCollidable[] borders = { this.bottomBorder, this.leftBorder, this.rightBorder };
    private KettleState kettleState = KettleState.RIGHT_BASIC;
    
    // Constructor
    public KettleSprite() 
    {
        super("KettleSprite"); 
        
        // force reset of current state code in order to set up borders
        String initialState = this.currentStateCode;
        this.currentStateCode = null;
        this.currentState = null;
        this.setCurrentStateCode(initialState);
    }
    
    // Static Inner Enum
    public static enum KettleState
    {
        // Constants
        LEFT_BASIC("left_basic"),
        LEFT_JUMP("left_jump"),
        RIGHT_BASIC("right_basic"),
        RIGHT_JUMP("right_jump");
        
        // Instance Fields
        public final String code;
        
        private KettleState afterJumping = this;
        public KettleState getAfterJumping() { return this.afterJumping; }
        
        private KettleState afterLanding = this;
        public KettleState getAfterLanding() { return this.afterLanding; }
        
        private KettleState afterMovingLeft = this;
        public KettleState getAfterMovingLeft() { return this.afterMovingLeft; }
        
        private KettleState afterMovingRight = this;
        public KettleState getAfterMovingRight() { return this.afterMovingRight; }
        
        // Constructor
        private KettleState(String _code) { this.code = _code; }
        
        // Static Initializer
        static
        {
            LEFT_BASIC.afterJumping = LEFT_JUMP;
            LEFT_BASIC.afterMovingRight = RIGHT_BASIC;
            LEFT_JUMP.afterLanding = LEFT_BASIC;
            LEFT_JUMP.afterMovingRight = RIGHT_JUMP;
            RIGHT_BASIC.afterJumping = RIGHT_JUMP;
            RIGHT_BASIC.afterMovingLeft = LEFT_BASIC;
            RIGHT_JUMP.afterLanding = RIGHT_BASIC;
            RIGHT_JUMP.afterMovingLeft = LEFT_JUMP;
        }
    }
    
    
    // Instance Methods
    
    public void jump(float ay) 
    {
        this.instantaneouslyAccelerateY(ay); 
        this.setYAcceleration(0.1f); 
        this.kettleState = this.kettleState.getAfterJumping(); 
        this.setCurrentStateCode(this.kettleState.code); 
    }
    
    public void land() 
    {
        this.setYVelocity(0f); 
        this.setYAcceleration(0f); 
        this.kettleState = this.kettleState.getAfterLanding(); 
        this.setCurrentStateCode(this.kettleState.code); 
    }
    
    public void moveLeft() 
    {
        this.setXVelocity(-1.0f); 
        this.kettleState = this.kettleState.getAfterMovingLeft(); 
        this.setCurrentStateCode(this.kettleState.code); 
    }
    
    public void moveRight() 
    {
        this.setXVelocity(1.0f); 
        this.kettleState = this.kettleState.getAfterMovingRight(); 
        this.setCurrentStateCode(this.kettleState.code); 
    }
    
    public void stopHorizontalMovement() 
    {
        this.setXVelocity(0f); 
    }
    
    @Override public void addPoint(int x, int y) 
    {
        super.addPoint(x, y); 
        if (this.borders == null) { return; }
        for (QCollidable border : this.borders) { border.addPoint(x, y); } 
    }
    @Override public void addPoint(float x, float y) 
    {
        super.addPoint(x, y); 
        if (this.borders == null) { return; }
        for (QCollidable border : this.borders) { border.addPoint(x, y); } 
    }
    @Override public void reset() 
    {
        super.reset(); 
        if (this.borders == null) { return; }
        for (QCollidable border : this.borders) { border.reset(); } 
    }
    @Override public void putFirstVertexAt(float x, float y) 
    {
        super.putFirstVertexAt(x, y); 
        if (this.borders == null) { return; }
        for (QCollidable border : this.borders) { border.putFirstVertexAt(x, y); } 
    }
}