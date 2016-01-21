
public interface ActiveObject {
	public void update(World world, Camera.Direction direction);
	public void alignPosition();
	public void render(float shade);
	public float getX();
	public float getY();
	public float getZ();
}
