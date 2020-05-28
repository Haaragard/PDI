package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import histograma.HistogramaController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Pdi;

public class ScreenController {
	
	// Labels R G B
	@FXML Label labelR;
	@FXML Label labelG;
	@FXML Label labelB;
		
		
	// Images View
	@FXML ImageView imageView1;
	@FXML ImageView imageView2;
	@FXML ImageView imageView3;
	
	
	// Médias
	@FXML TextField txtFieldR;
	@FXML TextField txtFieldG;
	@FXML TextField txtFieldB;
	
	
	// Limiarização
	@FXML Slider sliderLimiar;
	
	
	// Ruidos
	@FXML ToggleGroup tipo;
	@FXML RadioButton vizX;
	@FXML RadioButton vizC;
	@FXML RadioButton viz3;
	
	
	// Adição / Subtração
	@FXML Slider sliderImagem1;
	@FXML Slider sliderImagem2;
	
	// Listrado
	@FXML TextField txtFieldQt;
	
	
	private Image img1, img2, img3;
	
	int xi, yi, xf, yf;
	
	@FXML void initialize() {
		tipo = new ToggleGroup();
		vizX.setToggleGroup(tipo);
		vizC.setToggleGroup(tipo);
		viz3.setToggleGroup(tipo);
	}
	
	@FXML
	private void abreImagem1() {
		img1 = abreImagem(imageView1, img1);
	}
	
	@FXML
	private void abreImagem2() {
		img2 = abreImagem(imageView2, img2);
	}
	
	@FXML
	public void Salvar() {
		if(img3 != null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png"));
			
			File file = fileChooser.showSaveDialog(null);
			if(file != null) {
				BufferedImage bim = SwingFXUtils.fromFXImage(img3, null);
				try {
					ImageIO.write(bim, "PNG", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Não há arquivo para salvar");
			}
		}
	}
	
	@FXML
	public void rasterImage(MouseEvent mEvent) {
		ImageView image = (ImageView) mEvent.getTarget();
		
		if(image.getImage() != null) {
			verificaCor(image.getImage(), (int) mEvent.getX(), (int) mEvent.getY());
		}
	}
	
	@FXML
	public void cinzaAritmetica() {
		img3 = Pdi.cinzaMediaAritmetica(img1, 0, 0, 0);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void limiarizar() {
		double valor = (sliderLimiar.getValue() / 255);
		img3 = Pdi.limiarizar(img1, valor);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void negativar() {
		img3 = Pdi.negativar(img1);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void RemoveRuido() {
		String tipo = ruidoVerif();
		img3 = Pdi.eliminarRuido(img1, tipo);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void adicao() {
		double valor1 = sliderImagem1.getValue() / 255;
		double valor2 = sliderImagem2.getValue() / 255;
		img3 = Pdi.adicao(img1, img2, valor1, valor2);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void subtracao() {
		img3 = Pdi.subtracao(img1, img2);
		if(img3 != null) {
			atualizaImagem3();
		}
	}
	
	@FXML
	public void MarcacaoMousePressed(MouseEvent evento) {
		ImageView img = (ImageView)evento.getTarget();
		if(img.getImage() != null) {
			xi = (int)evento.getX();
			yi = (int)evento.getY();
		}
	}
	
	@FXML
	public void MarcacaoMouseReleased(MouseEvent evento) {
		ImageView img = (ImageView)evento.getTarget();
		if(img.getImage() != null) {
			xf = (int)evento.getX();
			yf = (int)evento.getY();
			
			img3 = Pdi.marcacao(img1, Math.min(xi, xf), Math.min(yi, yf), Math.max(xi, xf), Math.max(yi, yf));
			atualizaImagem3();
		}
	}
	
	@FXML
	public void openHistograma() {
		openHistogramaModal(null);
	}
	
	@FXML
	public void borda() {
		img3 = Pdi.bordas(img1);
		atualizaImagem3();
	}
	
	@FXML
	public void removeMetadeImg() {
		img3 = Pdi.metadeImagem(img1);
		atualizaImagem3();
	}
	
	@FXML
	public void listrarValorado() {
		int qt;
		try {
			qt = Integer.parseInt(txtFieldQt.getText());
			
			img3 = Pdi.listrarDadoValor(img1, qt, 0, 0, 0);
			atualizaImagem3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void colorIdentify() {
		Pdi.colorIdentify(img1, xi, xf, yi, yf);
	}
	
	// Private Funcs.
	
	private File selecionaImagem() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.PNG", "*.jpg", "*.JPG"
				, "*.gif", "*.GIF", "*.bmp", "*.BMP"));
		
		File imgSelec = fileChooser.showOpenDialog(null);
		
		try {
			return imgSelec;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Image abreImagem(ImageView imageView, Image image) {
		File f = selecionaImagem();
		
		if(Objects.nonNull(f)) {
			image = new Image(f.toURI().toString());
			
			imageView.setImage(image);
			imageView.setFitWidth(image.getWidth());
			imageView.setFitHeight(image.getHeight());
		}
		
		return image;
	}
	
	private void atualizaImagem3() {
		if(img3 != null) {
			imageView3.setImage(img3);
			imageView3.setFitWidth(img3.getWidth());
			imageView3.setFitHeight(img3.getHeight());
		}
	}
	
	private void verificaCor(Image img, int x, int y) {
		try {
			Color color = img.getPixelReader().getColor(x, y);
			
			labelR.setText("" + (int) (color.getRed() * 255));
			labelG.setText("" + (int) (color.getGreen() * 255));
			labelB.setText("" + (int) (color.getBlue() * 255));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String ruidoVerif() {
		if(vizX.isSelected()) {
			return "X";
		} else if(vizC.isSelected()) {
			return "C";
		} else if(viz3.isSelected()) {
			return "3";
		}
		return null;
	}
	
	private void openHistogramaModal(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/histograma/HistogramaModal.fxml"));
			Parent root = (Parent) loader.load();
			HistogramaController controller = loader.getController();
			
			Scene newScene = new Scene(root);
			Stage newStage = new Stage();
			newStage.setScene(newScene);
			newStage.show();
			
			if(img1 != null) {
				Pdi.valorizaGrafico(img1, controller.grafico1);
			}
			if(img2 != null) {
				Pdi.valorizaGrafico(img2, controller.grafico2);
			}
			if(img3 != null) {
				Pdi.valorizaGrafico(img3, controller.grafico3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
