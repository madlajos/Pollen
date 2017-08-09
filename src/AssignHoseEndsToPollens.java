import java.util.ArrayList;
import java.util.Collections;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.lang.*;

public class AssignHoseEndsToPollens {

	public Mat novesztes(Mat skeleton)
	{
		Mat tmp = new Mat();
		skeleton.copyTo(tmp);
		
		for (int i = 0; i < tmp.cols(); i++)
			for (int j = 0; j < tmp.rows(); j++) ///pixelenként vizsgáljuk a képet
			{
				double [] intensity = tmp.get(j, i);
				if (intensity[0] == 254)
				{
					Point nov = new Point();
					Point feher = new Point(i, j);
					for (int l = 0; l < 2; l++)
					{
						if (0 < feher.x && feher.x < tmp.cols() && 0 < feher.y && feher.y < tmp.rows())
						{
						Point szurke = korulotteSzurke(feher, tmp);
						int novy = (int) (feher.y - (szurke.y - feher.y));
						int novx = (int) (feher.x - (szurke.x - feher.x));
							nov.x = novx;
							nov.y = novy;
						tmp.put((int)feher.y, (int)feher.x, 128);
						if (l == 1)
						{
							tmp.put((int)nov.y, (int)nov.x,200);
						}
						else
						{
							tmp.put((int)nov.y, (int)nov.x,254);
						}
						feher.x = nov.x;
						feher.y = nov.y;
						}
					}
				}
			}
		return tmp;
	}
	public Point korulotteSzurke(Point p, Mat skeleton)
	{
		int i = (int)p.x;
		int j = (int)p.y;
		Point returnValue = new Point();

		for (int k = -1; k < 2; k++)
			for (int l = -1; l < 2; l++)
			{
				if (k == 0 && l == 0)
				{
					continue;
				}
				else
				{
					double [] color = skeleton.get(j + k, i + l);
					if (color[0] == 128)
					{
						returnValue.x = (i + l);
						returnValue.y = (j + k);			
					}						
				}
			}
		return returnValue;/*TODO lehet hiba*/
	}
	
	public Mat extract(Mat image1,Mat image2)
	{
		Mat returnValue = new Mat(image1.cols(),image1.rows(),CvType.CV_8UC1);
		for (int i = 0; i < image1.cols(); i++)
			for (int j = 0; j < image1.rows(); j++)
			{
				double[] pixelofImage1 = image1.get(j, i);
				double[] pixelofImage2 = image2.get(j, i);
				
				double pixel = pixelofImage1[0] - pixelofImage2[0];
				if(pixel < 0)
				{
					pixel = 0;
				}
				
				returnValue.put(j, i, pixel);
			}
		return returnValue;
	}
	
	public Mat vegKeres(Mat aSkeleton)		//A skeletonon fehérre színezi azokat a végeket, amelyek pollenhez tartoznak. Ezek a fehér pontok a "vegek" képben lesznek
	{
		Mat skeleton = new Mat();
		aSkeleton.copyTo(skeleton);
		
		Mat temp = new Mat();
		Mat pollenek = new Mat();
		temp = Analize.thresholdedImageForPollen;/*TODO ez jobb megoldás volt , de ott az extrect ha mégse*/
		int dilation_size = 6;
		int  dilation_type = Imgproc.MORPH_ELLIPSE;
		Mat element = Imgproc.getStructuringElement(dilation_type,
			new Size(2 * dilation_size + 1, 2 * dilation_size + 1),
			new Point(dilation_size, dilation_size));
		
		Imgproc.erode(temp, pollenek, element);


		Mat vegek = new Mat(skeleton.rows(), skeleton.cols(), CvType.CV_8UC1, new Scalar(0));
		for (int i = 0; i < skeleton.cols(); i++)
			for (int j = 0; j < skeleton.rows(); j++)
			{
				double [] intensity = skeleton.get(j, i);
				if (intensity[0] == 200)
				{
					double [] original = pollenek.get(j, i);
					if (original[0] == 0)
					{
						skeleton.put(j, i,240);
						vegek.put(j, i,255);						
					}
				}
			}
		return vegek;
	}
	
	void vegTorles(Mat skeleton)
	{
		Mat centers = new Mat(skeleton.rows(), skeleton.cols(), CvType.CV_8UC1,new Scalar(0));
		ArrayList<Point> centerVector = new ArrayList<Point>();
		ArrayList<Boolean> centerVectorFlags = new ArrayList<Boolean>();		//true = szabad , false = foglalt
		

		for (int  i = 0; i < Analize.circlesList.size(); i++)
		{
			double[] circle = Analize.circlesList.get(i);
			Point center = new Point((circle[0]),(circle[1]));
			centerVector.add(center);
			centerVectorFlags.add(true);
			int radius = (int) (circle[2]);
			double[] korkozepe = Analize.thresholdedImageForPollen.get((int)center.x,(int)center.y);
			if (((circle[0] + radius*0.9) < Analize.preFilteredImageForPollen.cols()) && 
					((circle[0] - radius*0.9) > 0) && 
					((circle[1] + radius*0.9) < Analize.preFilteredImageForPollen.rows()) && ((circle[1] - radius*0.9) > 0)
					)
			{
				//circle(centers, center, 2, 255, -1, 8, 0);
				
				Imgproc.circle(Analize.HoseEnds, center, 0,new Scalar(150), -1, 8, 0);
				Imgproc.circle(Analize.HoseEnds, center, 3, new Scalar(150), 3, 8, 0);
			}
		}
		
		for (int i = 0; i < Analize.HoseEnds.cols(); i++)
			for (int j = 0; j < Analize.HoseEnds.rows(); j++)
			{
				double [] intensity = Analize.HoseEnds.get(j, i);
				if (intensity[0] == 225)
				{
					ArrayList<Double> distanceVector = new ArrayList<Double>();
					ArrayList<Double> temp= new ArrayList<Double>();
					for (int k = 0; k < Analize.circlesList.size(); k++)
					{
						int yDiff, xDiff;
						yDiff = (int) (j - centerVector.get(k).y);
						xDiff = (int) (i - centerVector.get(k).x);
						double distance = Math.sqrt(yDiff*yDiff + xDiff*xDiff);
						distanceVector.add(distance);
					}
					temp = distanceVector;
					Collections.sort(distanceVector);

					double min = distanceVector.get(0); 
					double secondmin=distanceVector.get(1);
					int index, k;

					for (k = 0; k < Analize.circlesList.size(); k++)
						if (temp.get(k) == min && centerVectorFlags.get(k) == true)
						{
							index = k;
							centerVectorFlags.set(k, false);
							System.out.println(centerVector.get(index));
							break;
						}


					if (k == Analize.circlesList.size())
						{
						for (k = 0; k < Analize.circlesList.size(); k++)
							if (temp.get(k) == secondmin && centerVectorFlags.get(k)== true && (1.3*min>=secondmin))
							{
								index = k;
								centerVectorFlags.set(k, false);
								System.out.println(centerVector.get(index));
								break;
							}
							if (k == Analize.circlesList.size())
								Analize.HoseEnds.put(j, i, 0);
						}

				}
			}
	}
}
