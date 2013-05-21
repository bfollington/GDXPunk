package au.com.voltic.gdx;

public class Animation {

    public final int frameCount;
    public final int[] frames;
    public final int speed;
    public final Boolean loops;
    
    public Animation(int[] frames, int speed, Boolean loops) {
        
        frameCount = frames.length;
        this.frames = frames;
        this.speed = speed;
        this.loops = loops;
    }

}
