package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageData {

	private int w;
	private int h;
	
	private PixelReader pr;
	private WritableImage wi;
	private PixelWriter pw;
	
	public ImageData(Image img) {
		setW((int) img.getWidth());
		setH((int) img.getHeight());
		
		setPr(img.getPixelReader());
		setWi(new WritableImage(w, h));
		setPw(wi.getPixelWriter());
	}
	
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public PixelReader getPr() {
		return pr;
	}
	public void setPr(PixelReader pr) {
		this.pr = pr;
	}
	public WritableImage getWi() {
		return wi;
	}
	public void setWi(WritableImage wi) {
		this.wi = wi;
	}
	public PixelWriter getPw() {
		return pw;
	}
	public void setPw(PixelWriter pw) {
		this.pw = pw;
	}
}
