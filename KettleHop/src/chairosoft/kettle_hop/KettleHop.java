/* 
 * Nicholas Saney
 * 
 * Created: August 25, 2015
 * 
 * KettleHop.java 
 * KettleHop main and auxiliary methods
 */

package chairosoft.kettle_hop;

import static chairosoft.quadrado.QCompassDirection.*;
import chairosoft.quadrado.QApplication;
import chairosoft.quadrado.QCompassDirection;
import chairosoft.quadrado.QCompassKeypad;
import chairosoft.quadrado.QMapRoom;
import chairosoft.quadrado.QMapRoomLoader;
import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QTileset;

import chairosoft.ui.audio.MultitrackBackgroundAudio;
// import chairosoft.ui.geom.*;
import chairosoft.ui.event.ButtonEvent;
import chairosoft.ui.event.PointerEvent;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.Font;
import chairosoft.util.function.Consumer;

public class KettleHop extends QApplication
{
    //
    // Main Method
    // 
    
    public static void main(String[] args)
    {
        System.err.println("Starting game... ");
        KettleHop app = new KettleHop();
        app.setFrameRateInHz(100);
        app.setUseButtonListener(true);
        app.setUsePointerListener(true);
        app.gameStart();
    }
    
    
    //
    // Constructor
    //
    
    public KettleHop() 
    {
        super("Kettle Hop"); 
    }
    
    
    //
    // Constants
    //
    
    public static final int PANEL_WIDTH = QTileset.getTileWidth() * 8 * 2;
    public static final int PANEL_HEIGHT = QTileset.getTileHeight() * 7 * 2;
    @Override public int getPanelWidth() { return PANEL_WIDTH; }
    @Override public int getPanelHeight() { return PANEL_HEIGHT; }
    
    public static final int X_SCALING = 2;
    public static final int Y_SCALING = 2;
    @Override public int getXScaling() { return X_SCALING; }
    @Override public int getYScaling() { return Y_SCALING; }

    
    
    //
    // Instance Fields
    //
    
    // Audio
    public final MultitrackBackgroundAudio bgAudio = MultitrackBackgroundAudio.create();
    
    // Game State
    public final GameState GAME_NOT_LOADED = new GameNotLoaded(this);
    public final GameState MAPROOM_LOADING = new MapRoomLoading(this);
    public final GameState MAPROOM_LOADED = new MapRoomLoaded(this);
    public final GameState NORMAL_GAMEPLAY = new NormalGameState(this);
    public volatile GameState gameState = this.GAME_NOT_LOADED;
    
    // Keypad
    protected QCompassKeypad keypad = new QCompassKeypad();
    
    // Map Room
    public QMapRoom maproom = null;
    public QMapRoom.MapLink currentMapLink = new QMapRoom.MapLink(0, 0, "000", -3, 2);
    public volatile QMapRoomLoader.Result maproomLoaderResult = null;
    
    // Protagonist
    public KettleSprite protagonist = new KettleSprite();
    
    
    //
    // Instance Methods 
    //
    
    public long getFramesElapsedTotal() { return this.framesElapsedTotal; }
    
    public void loadNormalGameplay()
    {
        this.gameState = this.MAPROOM_LOADING;
        QMapRoomLoader loader = new QMapRoomLoader(
            this.currentMapLink, 
            new Consumer<QMapRoomLoader.Result>()
            {
                @Override public void accept(QMapRoomLoader.Result result)
                {
                    KettleHop self = KettleHop.this;
                    self.maproomLoaderResult = result;
                    
                    //self.bgAudio.loadLoopingTracks(MS_START, MS_END, "/snd/musicFile1.mp3", "/snd/musicFile2.mp3", "...");
                    //self.bgAudio.playAll();
                    
                    self.gameState = self.MAPROOM_LOADED;
                }
            }
        );
        loader.startLoading();
    }
    
    
    
    @Override
    protected void qGameInitialize() 
    {
        System.err.println("GAME INITIALIZED");
        //Loading.startVerbose();
    }
    
    @Override protected void qGameUpdateInit() { this.gameState.updateInit(); }
    
    @Override protected void qPointerPressed(float x, float y) { this.gameState.pointerPressed(x, y); }
    @Override protected void qPointerMoved(float x, float y) { this.gameState.pointerMoved(x, y); }
    @Override protected void qPointerReleased(float x, float y) { this.gameState.pointerReleased(x, y); }
    
    @Override protected void qButtonPressed(ButtonEvent.Code buttonCode) 
    {
        switch (buttonCode)
        {
            case LEFT:  keypad.activateValue(WEST); break;
            case RIGHT: keypad.activateValue(EAST); break;
            case UP:    keypad.activateValue(NORTH); break;
            case DOWN:  keypad.activateValue(SOUTH); break;
        }
        this.gameState.buttonPressed(buttonCode); 
    }
    @Override protected void qButtonHeld(ButtonEvent.Code buttonCode) { this.gameState.buttonHeld(buttonCode); }
    @Override protected void qButtonReleased(ButtonEvent.Code buttonCode) 
    {
        switch (buttonCode)
        {
            case LEFT:  keypad.deactivateValue(WEST); break;
            case RIGHT: keypad.deactivateValue(EAST); break;
            case UP:    keypad.deactivateValue(NORTH); break;
            case DOWN:  keypad.deactivateValue(SOUTH); break;
        }
        this.gameState.buttonReleased(buttonCode); 
    }
    @Override protected void qButtonNotHeld(ButtonEvent.Code buttonCode) { this.gameState.buttonNotHeld(buttonCode); }
    
    @Override protected void qGameUpdate() { this.gameState.update(); }
    @Override protected void qGameRender(DrawingContext ctx) { this.gameState.render(ctx); }
    
    @Override
    protected void qGameFinish()
    {
        System.err.println("GAME FINISHED");
    }
    
}