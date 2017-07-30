import org.opencv.imgproc.Imgproc;

public class PollenCounter extends Analize {
	
	public static int pollenCounter() {
		int minRadius = (int) (thresholdedImage.rows()*0.008);
		int maxRadius = (int) (thresholdedImage.rows()*0.03);
		
		/*TODO csind meg stackoverflow alapjan*/
		Imgproc.HoughCircles(thresholdedImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1, thresholdedImage.rows()*0.02, 1, 8, minRadius, maxRadius);
		
		for (int i = 0; i < circles.size(); i++)
		{
			Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
			int radius = cvRound(circles[i][2]);
			if (((center.x + radius*0.9) < thresholed.cols) && ((center.x - radius*0.9) > 0) && ((center.y + radius*0.9) < thresholed.rows) && ((center.y - radius*0.9) > 0))
			{
				// circle center
				circle(image2, center, 0, Scalar(0, 255, 0), -1, 8, 0);
				// circle outline
				circle(image2, center, radius, Scalar(0, 0, 255), 3, 8, 0);
			}
			CIRCLE_NUMBER = i;
		}
		
		
		
		return 0;
	}
	
}
