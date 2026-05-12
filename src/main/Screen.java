package main;

public class Screen extends Render {

	public Screen(int width, int height) {
		super(width, height);
	}

	public void render() {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}

		draw(0 ,0);
	}
}