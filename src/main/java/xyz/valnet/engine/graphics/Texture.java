package xyz.valnet.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import xyz.valnet.engine.util.BufferUtils;

import static org.lwjgl.opengl.GL20.*;

public class Texture {
	
	public final int width, height;
	public final int handle;
	private final BufferedImage img;

	private boolean registered = false;
	
	public Texture(String path) {
		img = loadImageFromDisk(path);
		if(img == null) {
			width = 0;
			height = 0;
			handle = -1;
			return;
		}
		width = img.getWidth();
		height = img.getHeight();
		handle = registerImg();
	}

	private BufferedImage loadImageFromDisk (String path) {
		try {
			return ImageIO.read(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int registerImg() {
		if(registered) return handle;
		registered = true;

		int[] pixels = new int[width * height];
		int[] data = new int[width * height];

		// populate pixels
		img.getRGB(0, 0, width, height, pixels, 0, width);
		
		// convert to data
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glActiveTexture(GL_TEXTURE0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
		// return 0;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

}
