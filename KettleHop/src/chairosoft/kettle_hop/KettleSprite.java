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
    public final QCollidable bottomBorder = new QCollidable();
    private KettleState kettleState = KettleState.RIGHT_BASIC;
    
    // Constructor
    public KettleSprite() 
    {
        super("KettleSprite"); 
        this.setCurrentStateCode(this.currentStateCode);
        this.recalculateBorders();
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
    
    public void recalculateBorders()
    {
        if (this.bottomBorder != null)
        {
            this.bottomBorder.reset();
            for (FloatPoint2D pt : this.points) { this.bottomBorder.addPoint(pt.x, pt.y + 1); } 
        }
    }
    
    @Override 
    public void setCurrentStateCode(String stateCode)
    {
        boolean isSame = this.currentStateCodeEquals(stateCode);
        super.setCurrentStateCode(stateCode);
        if (isSame) { return; }
        this.recalculateBorders();
    }
    
    @Override
    public void setXPosition(float px)
    {
        super.setXPosition(px);
        if (this.points.isEmpty()) { return; }
        float py = this.points.get(0).y;
        this.bottomBorder.putFirstVertexAt(px, py);
    }
    
    @Override
    public void setYPosition(float py)
    {
        super.setYPosition(py);
        if (this.points.isEmpty()) { return; }
        float px = this.points.get(0).x;
        this.bottomBorder.putFirstVertexAt(px, py + 1);
    }
}