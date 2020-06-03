package histograma;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HistogramaOpenCvController {
	
	@FXML private Button startButton;

	@FXML private ImageView currentFrame;
	
	@FXML private CheckBox grayScale;
	
	private VideoCapture capture;
	private boolean cameraActive;
	private ScheduledExecutorService timer;
	
	public void initialize() {
		this.capture = new VideoCapture();
		this.cameraActive = false;
		
	}
    
	@FXML
	protected void startCamera(ActionEvent event) {
		if(!this.cameraActive) {
			this.capture.open("webcam.exe");
			
			if(this.capture.isOpened()) {
				this.cameraActive = true;
				
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run() {
						Mat frame = readCurrentFrame();
						Image imageToShow = matAsImage(frame);
						
						updateCurrentImage(imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 100, TimeUnit.MILLISECONDS);
			}
		} else {
			this.cameraActive = false;
		}
	}

    private Mat readCurrentFrame() {
    	Mat frame = new Mat();
    	
    	this.capture.read(frame);
    	
    	if(grayScale.isSelected()) {
    		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
    	}
    	
    	return frame;
    }
    
    private Image matAsImage(Mat frame) {
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
    
    private void updateCurrentImage(ImageView imageView, Image image) {
    	imageView.setImage(image);
    }
    
    private void updateCurrentImage(Image image) {
    	currentFrame.setImage(image);
    }
    
//    private void writeHistogram(Mat frame) {
//    	boolean gray = grayScale.isSelected();
//    	
//    	List<Mat> images = new ArrayList<>();
//    	Core.split(frame, images);
//    	
//    	MatOfInt histSize = new MatOfInt(256);
//    	MatOfInt channels = new MatOfInt(0);
//    	MatOfFloat histRange = new MatOfFloat(0, 256);
//    	
//    	Mat histB = new Mat();
//    	Mat histG = new Mat();
//    	Mat histR = new Mat();
//    	
//    	Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), histB, histSize, histRange, false);
//    	
//    	if (!gray) {
//    		Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), histG, histSize, histRange, false);
//    		Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), histR, histSize, histRange, false);
//    	}
//    	
//    	int histW = 150;
//    	int histH = 150;
//    	int binW = (int) Math.round(histW / histSize.get(0, 0)[0]);
//    	
//    	Mat histImage = new Mat(histH, histW, CvType.CV_8UC3, new Scalar(0, 0, 0));
//    	
//    	
//    }
    
}
