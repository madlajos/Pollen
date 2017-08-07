import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class HosePreFilter extends HoseOperations
{
	public void hoseMorphology()
	{
		int operation = 6; 
		Mat element = Imgproc.getStructuringElement(1, new Size(2 * 21 + 1, 2 * 21 + 1), new Point(21, 21));
		Imgproc.morphologyEx(Analize.greyImage, Analize.preFilteredImageForHose, operation, element);		
	}
	
	public void setThreshold()
	{
		Imgproc.threshold(Analize.preFilteredImageForHose, Analize.thresholdedImageForHose, 8, 255, 0);
	}
	
	public Mat RemoveNoise(Mat dst)	
	{
		Mat dst_gray = new Mat();
		dst.copyTo(dst_gray);
		int ffillMode = 1;
		int loDiff = 20, upDiff = 20;
		int connectivity = 4;
		boolean isColor = true;
		boolean useMask = false;
		int newMaskVal = 128;
		Rect ccomp = new Rect();
		int flags = connectivity + (newMaskVal << 8) +
			(ffillMode == 1 ? Imgproc.FLOODFILL_FIXED_RANGE : 0);
		int size;

		Mat mask = new Mat(dst.rows() + 2, dst.cols() + 2, CvType.CV_8UC1, new Scalar(0));
		for (int i = 0; i < Analize.circlesList.size(); i++)
		{
			double[] circle = Analize.circlesList.get(i);
			double radius = circle[2];
			if(((circle[0] + radius*0.9) < Analize.preFilteredImageForPollen.cols()) && 
					((circle[0] - radius*0.9) > 0) && 
					((circle[1] + radius*0.9) < Analize.preFilteredImageForPollen.rows()) && ((circle[1] - radius*0.9) > 0)) 
			{
				Scalar newVal = new Scalar(128, 128, 128); ///A növesztett régió új színe
				Point seed = new Point(circle[0],circle[1]); ///Régiónövesztés gyökere
				size = Imgproc.floodFill(dst_gray, mask, seed, newVal, ccomp, new Scalar(loDiff, loDiff, loDiff), new Scalar(upDiff, upDiff, upDiff), flags);
			}
		}
		return mask;
	}
	
	public void removeNoiseFromThresholdedImageForHose()
	{
		Analize.removedNoiseFromHoseImage = RemoveNoise(Analize.thresholdedImageForHose);
	}
}

