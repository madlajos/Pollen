import org.opencv.imgproc.Imgproc;

public class PollenCounter extends Analize
{	
	/**
	 * 
	 */
	public void pollenCounter() 
	{
		/**
		 * minRadius, maxRadius: legkisebb �s legnagyobb elfogadott k�r�k sugara
		 * a szorz�jukat a k�pm�ret szerint kell �t�ll�tani
		 */
		int minRadius = (int) (Analize.thresholdedImageForPollen.rows()*0.008);
		int maxRadius = (int) (Analize.thresholdedImageForPollen.rows()*0.03);

		/**
		 *circles-be menti a k�z�ppontokat �s sugarakat 
		 */
		Imgproc.HoughCircles(Analize.thresholdedImageForPollen, Analize.circles, Imgproc.CV_HOUGH_GRADIENT, 1,
				Analize.thresholdedImageForPollen.rows()*0.02, 1, 8, minRadius, maxRadius);

		for(int i = 0; i < Analize.circles.cols(); i++)
		{
			double[] circle = Analize.circles.get(0, i);
			Analize.circlesList.add(circle);
			double radius = circle[2];
			if(((circle[0] + radius*0.9) < Analize.thresholdedImageForPollen.cols()) && ((circle[0] - radius*0.9) > 0) && ((circle[1] + radius*0.9) < Analize.thresholdedImageForPollen.rows()) && ((circle[1] - radius*0.9) > 0))
			{
				Analize.circleNumber = Analize.circleNumber + 1;     
			}       			
		}
	} 	
}
