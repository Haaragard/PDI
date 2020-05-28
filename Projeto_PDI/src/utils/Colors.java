package utils;

import java.util.Arrays;

import javafx.scene.paint.Color;

public class Colors {
	double [] red;
	double [] green;
	double [] blue;
	double [] opacity;
	int size = 0;
	
	// Recebe qtd pixels
	public void tamanhoVetor(int tamanho) {
		red = new double[tamanho];
		green = new double[tamanho];
		blue = new double[tamanho];
		opacity = new double[tamanho];
		size = 0;
	}
	
	public void adicionaValores(Color rgb) {
		red[size] = rgb.getRed();
		green[size] = rgb.getGreen();
		blue[size] = rgb.getBlue();
		opacity[size] = rgb.getOpacity();
		size++;
	}
	
	public void organiza() {
		Arrays.sort(red);
		Arrays.sort(green);
		Arrays.sort(blue);
		Arrays.sort(opacity);
	}
	
	public Color parecido() {
		int medio = size/2;
		return new Color(red[medio], green[medio], blue[medio], opacity[medio]);
	}
}
