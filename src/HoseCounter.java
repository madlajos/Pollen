import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class HoseCounter extends HoseOperations
{
	public void removePollens()
	{
		int operation = 5;
		Mat element = Imgproc.getStructuringElement(2, new Size(2 * 7 + 1, 2 * 7 + 1), new Point(7, 7));
		Imgproc.morphologyEx(Analize.removedNoiseFromHoseImage, Analize.removedPollens, operation, element);
	}
	
	public Mat HoseCounterMethod(Mat dst_gray)
	{
		int ffillMode = 1;
		int loDiff = 0, upDiff = 0;
		int connectivity = 4;
		boolean isColor = true;
		boolean useMask = false;
		int newMaskVal = 128;
		Rect ccomp = new Rect();
		int flags = connectivity + (newMaskVal << 8) +
			(ffillMode == 1 ? Imgproc.FLOODFILL_FIXED_RANGE : 0);
		int size;

		Imgproc.threshold(dst_gray, dst_gray, 80, 128, 0);
		Mat mask = new Mat(dst_gray.rows() + 2, dst_gray.cols() + 2, CvType.CV_8UC1, new Scalar(0));

		for (int i = 1; i< dst_gray.cols(); i++)
			for (int j = 1; j < dst_gray.rows(); j++)
			{
				double[] intensity = dst_gray.get(j, i);

				if (intensity[0] != 0 && intensity[0] != 255)
				{
					Scalar newVal = new Scalar(255, 255, 255); ///A növesztett régió új színe
					Point seed = new Point(i, j); ///Régiónövesztés gyökere
					size = Imgproc.floodFill(dst_gray, mask, seed, newVal, ccomp, new Scalar(loDiff, loDiff, loDiff), new Scalar(upDiff, upDiff, upDiff), flags);
					if (size < 40)
					{
						newVal = new Scalar(0, 0, 0); //Ha 200 pixelnél kisebb egy régió, akkor fekete lesz
						size = Imgproc.floodFill(dst_gray, mask, seed, newVal, ccomp, new Scalar(loDiff, loDiff, loDiff), new Scalar(upDiff, upDiff, upDiff), flags);
					}
					else
					{
						Analize.hoseNumber++;/*TODO szerintem ne itt számoljuk, me ha metszi egymást akkor szar, viszont amikor
						 					hosszt számolunk akkor ott szétválasztjuk ah metszi egymást*/
					}
				}
			}
		return dst_gray;
	}
	
	public void HoseCount()
	{
		Analize.hoses = HoseCounterMethod(Analize.removedPollens);
	}
}
