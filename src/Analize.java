
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class Analize {
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
		
		Mat m = Imgcodecs.imread("file:C:/Users/madla/OneDrive/Documents/GitHub/imgproc/VH484.jpg");
		Image image = new Image("file:C:\\Users\\madla\\OneDrive\\Documents\\GitHub\\imgproc\\VH484.jpg");

		img.setImage(image);
		
		//False-al kreálódik, hogy csak a browse után legyen látható
		pagination.setVisible(true);
	}
}
