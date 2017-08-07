import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PreFilter extends Analize{

	public void preFilter(){
		int operation = 3;
		Mat element = Imgproc.getStructuringElement(2, new Size(2 * 5 + 1, 2 * 5 + 1), new Point(5, 5));
		Imgproc.morphologyEx(greyImage, preFilteredImage, operation, element);
	}

	public void setThreshold(){
		Imgproc.threshold(preFilteredImage, thresholdedImage, 80, 255, 0);
	}
}
