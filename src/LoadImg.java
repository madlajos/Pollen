import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LoadImg extends Analize{

	public void Beolvas(){
		originalImage = Imgcodecs.imread("C:/Users/madla/OneDrive/Documents/GitHub/imgproc/VH484.jpg");
		//Image image = new Image("file:C:\\Users\\madla\\OneDrive\\Documents\\GitHub\\imgproc\\VH484.jpg");
		//return image;
		System.out.println(originalImage.size());
	}

	public void Resize(){
		Imgproc.resize(originalImage, resizedImage, resizedImage.size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
	}

	public void Decolorize(){
		Imgproc.cvtColor(resizedImage, greyImage, Imgproc.COLOR_RGB2GRAY);
	}
}
