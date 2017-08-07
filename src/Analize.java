import java.util.ArrayList;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.sun.javafx.geom.Vec3f;

public class Analize {
	protected Mat originalImage = new Mat();
	protected Mat resizedImage = new Mat();
	protected Mat greyImage = new Mat();
	protected Mat preFilteredImage = new Mat();
	protected Mat thresholdedImage = new Mat();
	protected Mat circles = new Mat();
	
	public static void main(String args[]){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
<<<<<<< HEAD
		LoadImg li = new LoadImg();
		
		li.Beolvas();
		li.Resize();
		li.Decolorize();
		
		
		PreFilter pf = new PreFilter();
		pf.preFilter();
		pf.setThreshold();
=======
		Mat m = Imgcodecs.imread("file:C:/Users/madla/OneDrive/Documents/GitHub/imgproc/VH484.jpg");

		Image image = new Image("file:C:\\Users\\Kenderák József\\git\\Pollen\\VH485.jpg");

		img.setImage(image);

>>>>>>> origin/master
	}
}
