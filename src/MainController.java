
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class MainController {
/*
	
	@FXML
	private ImageView img;
	@FXML
	Pagination pagination;

	public void browse() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.jpg"));
		
		List<File> fileList = fileChooser.showOpenMultipleDialog(null);
		System.out.println(fileList.size());
		
		pagination.setMaxPageIndicatorCount(5);
		pagination.setPageCount(fileList.size());
		
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		

		img.setImage(LoadImg.Beolvas());
		Mat a = null;
		//Imgproc.cvtColor(m, a, 0);
		//Imgcodecs.imwrite("asd.jpg", m);
		
		//False-al kre�l�dik, hogy csak a browse ut�n legyen l�that�
		pagination.setVisible(true);
	}
	
	*/
}
