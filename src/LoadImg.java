import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.image.Image;

public class LoadImg extends Analize{

	public static Image Beolvas(){
		Mat originalImage = Imgcodecs.imread("file:C:/Users/madla/OneDrive/Documents/GitHub/imgproc/VH484.jpg");
		Image image = new Image("file:C:\\Users\\madla\\OneDrive\\Documents\\GitHub\\imgproc\\VH484.jpg");
		return image;
	}

	public static void Resize(){
		Imgproc.resize(originalImage, resizedImage, resizedImage.size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
	}

	public static void Decolorize(){
		Imgproc.cvtColor(resizedImage, greyImage, Imgproc.COLOR_RGB2GRAY);
	}
}
