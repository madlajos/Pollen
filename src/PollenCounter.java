import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PollenCounter extends Analize {
	
	public void pollenCounter() {
		int minRadius = (int) (thresholdedImage.rows()*0.008);
		int maxRadius = (int) (thresholdedImage.rows()*0.03);
		
		/*TODO csind meg stackoverflow alapjan*/
	
		Imgproc.HoughCircles(thresholdedImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1, thresholdedImage.rows()*0.02, 1, 8, minRadius, maxRadius);
		System.out.println(circles);
		
		
		for(int i = 0; i < circles.cols(); i++) {
			double[] circle = circles.get(0, i);
			//g.drawOval((int)circle[0] - (int)circle[2], (int)circle[1] - (int)circle[2], (int)circle[2] * 2, (int)circle[2] * 2);
		}
		
		//return 0;
	}
	
}
