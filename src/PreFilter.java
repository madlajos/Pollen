import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PreFilter extends Analize{

	/**
	 * preFilter(): setThreshold-hoz készíti elõ a képet
	 * operation: morphologyEx paraméter
	 * element: morphologyEx paraméter
	 */
	public void preFilter()
	{
		int operation = 3;
		Mat element = Imgproc.getStructuringElement(2, new Size(2 * 5 + 1, 2 * 5 + 1), new Point(5, 5));
		Imgproc.morphologyEx(Analize.greyImage, Analize.preFilteredImageForPollen, operation, element);
	}

	/**
	 * Thresholdozza az elõszûrt képet
	 */
	public void setThreshold()
	{
		Imgproc.threshold(Analize.preFilteredImageForPollen, Analize.thresholdedImageForPollen, 80, 255, 0);
	}
}
