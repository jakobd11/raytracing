package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {

	public static final int height = 800;
	public static final int width = 1000;
	public static final String title = "Ray Tracing";
	
	private Thread thread;
	private Screen screen;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private Light light = new Light(width/2-100, height/2);
	private Ball ball = new Ball(width/2+100, height/2, 50);
	
	private static int fps;
	private static int time = 0;
	
	public Display() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		
		screen = new Screen(width, height, light, ball);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
		    public void mouseDragged(java.awt.event.MouseEvent e) {
		        light.x = e.getX();
		        light.y = e.getY();
		    }
		});
	}
	
	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
		System.out.println("working");
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;
		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();
			
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}
			}
			render();
			frames++;
		}
		
	}
	
	private void tick() {
		time++;
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render();

		for (int i = 0; i < width * height; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.GREEN);
		g.drawString(fps + "fps", 10, 20);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Display dsp = new Display();
		JFrame frame = new JFrame();
		frame.add(dsp);
		frame.pack();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		dsp.start();
	}
}
