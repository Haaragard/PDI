package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

public class Pdi {
	
	public static Image cinzaMediaAritmetica(Image img, int pcr, int pcg, int pcb) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData = new ImageData(img);
			
			for(int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					double media = ((corA.getRed() + corA.getGreen() + corA.getBlue()) / 3);
					
					if((pcr != 0) || (pcg != 0) || (pcb != 0)) {
						media = (((corA.getRed() * pcr) + (corA.getGreen() * pcg) + (corA.getBlue() * pcb)) / 100);
					}
						
					Color corN = new Color(media, media, media, corA.getOpacity());
					
					imgData.getPw().setColor(i, j, corN);
				}
			}
			
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image limiarizar(Image img, double limiar) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData = new ImageData(img);
			
			for (int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					Color corN;
					
					if((corA.getRed() >= limiar) || (corA.getBlue() >= limiar) || (corA.getGreen() >= limiar)) {
						corN = new Color(1, 1, 1, corA.getOpacity());
					} else {
						corN = new Color(0, 0, 0, corA.getOpacity());
					}
					imgData.getPw().setColor(i, j, corN);
				}
			}
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image negativar(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData = new ImageData(img);
			
			for (int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					Color corN = new Color(
						(1 - corA.getRed()),
						(1 - corA.getGreen()),
						(1 - corA.getBlue()),
						corA.getOpacity());
					
					imgData.getPw().setColor(i, j, corN);
				}
			}
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Image eliminarRuido(Image img, String tipo) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData = new ImageData(img);
			
			
			if(tipo.equals("X")) {
				ruidoX(imgData);
			} else if(tipo.equals("C")) {
				ruidoC(imgData);
			}else if(tipo.equals("3")) {
				ruido3(imgData);
			}
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image adicao(Image img1, Image img2, double valor1, double valor2) {
		if(!checkImg(img1)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		if(!checkImg(img2)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 2 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData1 = new ImageData(img1);
			ImageData imgData2 = new ImageData(img2);
			
			int w = Math.min(imgData1.getW(), imgData2.getW());
			int h = Math.min(imgData1.getH(), imgData2.getH());
			
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corImg1 = imgData1.getPr().getColor(i, j);
					Color corImg2 = imgData2.getPr().getColor(i, j);
					
					double r = ((corImg1.getRed() * valor1) + (corImg2.getRed() * valor2));
					double g = ((corImg1.getGreen() * valor1) + (corImg2.getGreen() * valor2));
					double b = ((corImg1.getBlue() * valor1) + (corImg2.getBlue() * valor2));
					
					r = (r > 1) ? 1 : r;
					g = (g > 1) ? 1 : g;
					b = (b > 1) ? 1 : b;
					
					Color novaCor = new Color(r, g, b, corImg1.getOpacity());
					pw.setColor(i, j, novaCor);
				}
			}
			
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Image subtracao(Image img1, Image img2) {
		if(!checkImg(img1)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		if(!checkImg(img2)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 2 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData1 = new ImageData(img1);
			ImageData imgData2 = new ImageData(img2);
			
			int w = Math.min(imgData1.getW(), imgData2.getW());
			int h = Math.min(imgData1.getH(), imgData2.getH());
			
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corImg1 = imgData1.getPr().getColor(i, j);
					Color corImg2 = imgData2.getPr().getColor(i, j);
					
					double r = (corImg1.getRed() - corImg2.getRed());
					double g = (corImg1.getGreen() - corImg2.getGreen());
					double b = (corImg1.getBlue() - corImg2.getBlue());
					
					r = (r < 0) ? 0 : r;
					g = (g < 0) ? 0 : g;
					b = (b < 0) ? 0 : b;
					
					Color novaCor = new Color(r, g, b, corImg1.getOpacity());
					pw.setColor(i, j, novaCor);
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void valorizaGrafico(Image img, BarChart<String, Number> grafico) {
		int [] hist = histogramaUnico(img);
		XYChart.Series vlr = new XYChart.Series();
		for (int i = 0; i < hist.length; i++) {
			vlr.getData().add(new XYChart.Data(i+"", hist[i]));
		}
		grafico.getData().addAll(vlr);
		
		for(Node n : grafico.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: red;");
		}
	}
	
	public static Image marcacao(Image img, int xi, int yi, int xf, int yf) {
		ImageData imgData = new ImageData(img);
		if((xi < 0) || (xf < 0) || (xi > imgData.getW()) || (xf > imgData.getW()) ||
			(yi < 0) || (yf < 0) || (yi > imgData.getH()) || (yf > imgData.getH())) {
			JOptionPane.showMessageDialog(null, "A seleção não pode extrapolar o campo da imagem!");
			return null;
		}

		Color corNova = new Color(1,0,0,1);
		
		for (int i = 0; i < imgData.getW(); i++) {
			for (int j = 0; j < imgData.getH(); j++) {
				imgData.getPw().setColor(i, j, imgData.getPr().getColor(i, j));
			}
		}
		
		for (int i = xi; i <= xf; i++) {
			imgData.getPw().setColor(i, yi, corNova);
			imgData.getPw().setColor(i, yf, corNova);
			
			if((yi+1) <= imgData.getH()) {
				imgData.getPw().setColor(i, yi+1, corNova);
			}
			
			if((yi-1) >= 0) {
				imgData.getPw().setColor(i, yi-1, corNova);
			}
			
			if((yf+1) <= imgData.getH()) {
				imgData.getPw().setColor(i, yf+1, corNova);
			}
			
			if((yf-1) >= 0) {
				imgData.getPw().setColor(i, yf-1, corNova);
			}
		}
		
		for (int i = yi; i <= yf; i++) {
			imgData.getPw().setColor(xi, i, corNova);
			imgData.getPw().setColor(xf, i, corNova);
			
			if((xi+1) <= imgData.getW()) {
				imgData.getPw().setColor(xi+1, i, corNova);
			}
			
			if((xi-1) >= 0) {
				imgData.getPw().setColor(xi-1, i, corNova);
			}
			
			if((xf+1) <= imgData.getW()) {
				imgData.getPw().setColor(xf+1, i, corNova);
			}
			
			if((xf-1) >= 0) {
				imgData.getPw().setColor(xf-1, i, corNova);
			}
		}
		return imgData.getWi();
	}
	
	public static Image bordas(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		try {
			ImageData imgData = new ImageData(img);
			
			for (int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					if((i < 10) || (i > (imgData.getW()-10))) {
						Color corN = new Color(1,0,0, 1);
						
						imgData.getPw().setColor(i,j,corN);
					} else {
						Color corN = new Color(corA.getRed(),corA.getGreen(),corA.getBlue(), corA.getOpacity());
						
						imgData.getPw().setColor(i,j,corN);
					}
					
					if((j<10) || (j> (imgData.getH()-10))) {
						Color corN = new Color(1,0,0, 1);
						
						imgData.getPw().setColor(i,j,corN);
					}
				}
			}
			
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image metadeImagem(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		try {
			ImageData imgData = new ImageData(img);
			
			int half = imgData.getH()/2;
			
			for (int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					
					
					if(j < half) {
						Color corN = new Color(1,0,0, 1);
						
						imgData.getPw().setColor(i,j,corN);
					} else {
						Color corN = new Color(corA.getRed(),corA.getGreen(),corA.getBlue(), corA.getOpacity());
						
						imgData.getPw().setColor(i,j,corN);
					}
				}
			}
			
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image listrarDadoValor(Image img, int qt, int pcr, int pcg, int pcb) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			ImageData imgData = new ImageData(img);
			
			int colunas[] = new int[qt + 1];
			int inicio = imgData.getW()/qt;
			
			colunas[0] = inicio;
			
			int contador = 0;
			boolean efeito = true;
			
			for (int i = 1; i < colunas.length-1; i++) {
				colunas[i] = colunas[i] + inicio + colunas[i-1];
			}
			
			for (int i = 0; i < imgData.getW(); i++) {
				for (int j = 0; j < imgData.getH(); j++) {
					Color corA = imgData.getPr().getColor(i, j);
					
					if(i < colunas[contador]) {
						if(efeito) {
							double media = ((corA.getRed() + corA.getGreen() + corA.getBlue()) / 3);
							if((pcr != 0) || (pcg != 0) || (pcb != 0)) {
								media = (((corA.getRed() * pcr) + (corA.getGreen() * pcg) + (corA.getBlue() * pcb)) / 100);
								Color corN = new Color(media, media, media, corA.getOpacity());
								imgData.getPw().setColor(i, j, corN);
							}
						} else {
							Color corN = new Color(corA.getRed(), corA.getGreen(), corA.getBlue(), corA.getOpacity());
							imgData.getPw().setColor(i, j, corN);
						}
					}
					
					if(i > colunas[contador]) {
						if(contador < (qt-1)) {
							contador++;
							efeito = !efeito;
						}
					}
				}
			}
			
			return imgData.getWi();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Boolean colorIdentify(Image img, int xi, int xf, int yi, int yf) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return false;
		}
		
		ImageData imgData = new ImageData(img);
		Set<String> cores = new HashSet<String>();
		
		for (int i = xi; i < xf; i++) {
			for (int j = yi; j < yf; j++) {
				Color color = imgData.getPr().getColor(i, j);
				cores.add((int)(color.getRed()*255) + "," + (int)(color.getGreen()*255) + "," + (int)(color.getBlue()*255));
			}
		}
		JOptionPane.showMessageDialog(null, cores.size() + " core(s) encontrada(s)!");
		return true;
	}
	
	public static Image cannyBorders(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			Mat matImgSrc = imageToMat(img);
			Mat matImgDst = new Mat();
			
			Imgproc.Canny(matImgSrc, matImgDst, 150, 150);
			img = matAsImage(matImgDst);
			
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image sobelFilter(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			Mat matImgSrc = imageToMat(img);
			Mat matImgDst = new Mat();
			
			Imgproc.Sobel(matImgSrc, matImgDst, CvType.CV_16S, 1, 0);
			img = matAsImage(matImgDst);
			
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image laplaceFilter(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		try {
			Mat matImgSrc = imageToMat(img);
			Mat matImgDst = new Mat();
			
			Imgproc.Laplacian(matImgSrc, matImgDst, CvType.CV_64F);
			img = matAsImage(matImgDst);
			
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image test(Image img) {
		if(!checkImg(img)) {
			JOptionPane.showMessageDialog(null, "Necessário inserir imagem no SLOT 1 para prosseguir!");
			return null;
		}
		
		Mat matImgSrc = imageToMat(img);
		Mat matImgDst = new Mat();
		
		Imgproc.Canny(matImgSrc, matImgDst, 150, 150);
		img = matAsImage(matImgDst);
		
		return img;
	}
	
// PRIVATE FUNC'S
	
	private static void ruidoX(ImageData imgData) {
		Colors c = new Colors();
		
		for (int i = 1; i < imgData.getW() - 1; i++) {
			for (int j = 1; j < imgData.getH() - 1; j++) {
				c.tamanhoVetor(5);
				
				c.adicionaValores(imgData.getPr().getColor(i - 1, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i - 1, j + 1));
				c.adicionaValores(imgData.getPr().getColor(i, j));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j + 1));
				
				c.organiza();
				
				imgData.getPw().setColor(i, j, c.parecido());
			}
		}
	}
	private static void ruidoC(ImageData imgData) {
		Colors c = new Colors();
		
		for (int i = 1; i < imgData.getW() - 1; i++) {
			for (int j = 1; j < imgData.getH() - 1; j++) {
				c.tamanhoVetor(5);
				
				c.adicionaValores(imgData.getPr().getColor(i - 1, j));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j));
				c.adicionaValores(imgData.getPr().getColor(i, j));
				c.adicionaValores(imgData.getPr().getColor(i, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i, j + 1));
				
				c.organiza();
				
				imgData.getPw().setColor(i, j, c.parecido());
			}
		}
	}
	
	private static void ruido3(ImageData imgData) {
		Colors c = new Colors();
		
		for (int i = 1; i < imgData.getW() - 1; i++) {
			for (int j = 1; j < imgData.getH() - 1; j++) {
				c.tamanhoVetor(10);
				
				c.adicionaValores(imgData.getPr().getColor(i - 1, j));
				c.adicionaValores(imgData.getPr().getColor(i - 1, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j));
				c.adicionaValores(imgData.getPr().getColor(i - 1, j + 1));
				c.adicionaValores(imgData.getPr().getColor(i, j));
				c.adicionaValores(imgData.getPr().getColor(i, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i, j - 1));
				c.adicionaValores(imgData.getPr().getColor(i + 1, j + 1));
				
				c.organiza();
				
				imgData.getPw().setColor(i, j, c.parecido());
			}
		}
	}
	
	private static Boolean checkImg(Image img) {
		return (img != null) ? true : false;
	}
	
	
	private static int[] histogramaUnico(Image img) {
		int [] qt = new int [256];
		ImageData imgData = new ImageData(img);
		
		for (int i = 0; i < imgData.getW(); i++) {
			for (int j = 0; j < imgData.getH(); j++) {
				qt[(int)(imgData.getPr().getColor(i, j).getRed()*255)]++;
				qt[(int)(imgData.getPr().getColor(i, j).getGreen()*255)]++;
				qt[(int)(imgData.getPr().getColor(i, j).getBlue()*255)]++;
			}
		}
		return qt;
	}
	
	private static void carregaOpenCv() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static Image matAsImage(Mat frame) {
		carregaOpenCv();
		
    	BufferedImage image = null;
    	
    	int width = frame.width(), height = frame.height(), channels = frame.channels();
    	byte[] sourcePixels = new byte[width * height * channels];
    	
    	frame.get(0, 0, sourcePixels);
    	
    	if(frame.channels() > 1) {
    		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    	} else {
    		image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    	}
    	final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    	System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
    	
    	return SwingFXUtils.toFXImage(image, null);
    }
	
	private static Mat imageToMat(Image img) {
		carregaOpenCv();
		
		ImageData imgData = new ImageData(img);
		byte[] buffer = new byte[(int)img.getWidth() * (int)img.getHeight() * 4];
		
		WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
		imgData.getPr().getPixels(0, 0, (int)img.getWidth(), (int)img.getHeight(), format, buffer, 0, ((int)img.getWidth() * 4));
		
		Mat mat = new Mat((int)img.getHeight(), (int)img.getWidth(), CvType.CV_8UC4);
		mat.put(0, 0, buffer);
		
		return mat;
	}
}
