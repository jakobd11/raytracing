package main;

public class Screen extends Render {
	private Light light;
    private Ball ball;
	
	public Screen(int width, int height, Light light, Ball ball) {
		super(width, height);
		this.light = light;
		this.ball = ball;
	}

	public void render() {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		
		for (int i = 0; i < 360; i++) {
			double angle = (2 * Math.PI * i) / 360.0;
			for (int j = 0; j < 1000; j++) {
				int px = (int)(light.x + Math.cos(angle)*j);
				int py = (int)(light.y + Math.sin(angle)*j);
				if (px < 0 || px >= width || py < 0 || py >= height) break;
				int dx = px - (int)ball.x;
				int dy = py - (int)ball.y;
				if (dx*dx + dy*dy <= ball.radius * ball.radius) break;
				int brightness = (int)(255 * (1.0 - (double)j / 1000.0));
				brightness = Math.max(0, brightness);
				int color = (brightness << 16) | (brightness << 8) | brightness;
				pixels[px + py * width] = color;
			}

		}
		
		for (int y = 0; y < height; y++) {
		    for (int x = 0; x < width; x++) {
		        int dx = x - (int)ball.x;
		        int dy = y - (int)ball.y;
		        if (dx*dx + dy*dy <= (ball.radius * ball.radius)-250) {
		            pixels[x + y * width] = 0x888888; // grey ball
		        }
		    }
		}

		draw(0 ,0);
	}
}