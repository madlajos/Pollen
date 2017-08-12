import java.util.ArrayList;
import java.util.Collections;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import utility.SkeletonEndsNearCircles;

import java.lang.*;

public class AssignhoseEndsToPollens extends HoseOperations {

	protected static double actualLength;
	/**
	 * 
	 * @param skeleton
	 * @return
	 */
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
	
	/**
	 * 
	 * @param p
	 * @param skeleton
	 * @return
	 */
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
	
	/**
	 * 
	 * @param image1
	 * @param image2
	 * @return
	 */
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
	/**
	 * 
	 * @param aSkeleton
	 * @return
	 */
	public Mat vegKeres(Mat aSkeleton)		//A skeletonon fehérre színezi azokat a végeket, amelyek pollenhez tartoznak. Ezek a fehér pontok a "vegek" képben lesznek
	{
		Mat skeleton = new Mat();
		aSkeleton.copyTo(skeleton);
		
		Mat temp = new Mat();
		Mat pollenek = new Mat();
		temp = Analize.thresholdedImageForPollen;/*TODO ez jobb megoldás volt , de ott az extrect ha mégse*/
		int dilation_size = 10;
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
	
	/**TODO Áron itt módosítottam az algoritmuson, mivel a tiéd úgy mûködött, hogy, ha talált egy skeleton véget, akkor hozzárendelte
	 * a legközelebb lévõ kört. Viszont ilyenkor a körökhöz az a skeleton vég lesz rendelve, amelyet elõbb talál meg a for ciklus, és nem a
	 * legközelebbi skeleton vég. Az enyém úgy mûködik, hogy végigmegy a sketelonvégeket , hozzájuk rendeli a legközelebbi kört , és egy
	 * ArrayList-be(Javas Vector) elmenti a skeleton pontot a hozzárendelt kört és a távolságot. Ezután végigmegy az arraylisten ,
	 * és kiválasztja az egyes körökhöz legközelebbi skeletonvéget. (Ha a cpp kódba 410. sorba nem feketére színezed a pontokat hanem szürkére,
	 * akkor látod hogy pl a 484-es képen a bal legfelsõhöz rossz skeletonvéget rendel. Itt mát jót rendel.) */
	/**
	 * 
	 * @param skeleton
	 */
	void vegTorles()
	{
		ArrayList<Point> centerVector = new ArrayList<Point>();
		ArrayList<Boolean> centerVectorFlags = new ArrayList<Boolean>();		//true = szabad , false = foglalt
		

		for (int  i = 0; i < Analize.circlesList.size(); i++)
		{
			double[] circle = Analize.circlesList.get(i);
			Point center = new Point((circle[0]),(circle[1]));
			centerVector.add(center);
			centerVectorFlags.add(true);
			int radius = (int) (circle[2]);
			double[] korkozepe = Analize.thresholdedImageForPollen.get((int)center.y,(int)center.x);
			if (((circle[0] + radius*0.9) < Analize.preFilteredImageForPollen.cols()) && 
					((circle[0] - radius*0.9) > 0) && 
					((circle[1] + radius*0.9) < Analize.preFilteredImageForPollen.rows()) && ((circle[1] - radius*0.9) > 0) &&
					(korkozepe[0] < 100)
					)
			{
				//circle(centers, center, 2, 255, -1, 8, 0);
				
				Imgproc.circle(Analize.hoseEnds, center, 0,new Scalar(150), -1, 8, 0);
				Imgproc.circle(Analize.hoseEnds, center, 3, new Scalar(150), 3, 8, 0);
			}
		}
		
		ArrayList<SkeletonEndsNearCircles> skeletonEndsNearCircles = new ArrayList<SkeletonEndsNearCircles>();
		
		for (int i = 0; i < Analize.hoseEnds.cols(); i++)
			for (int j = 0; j < Analize.hoseEnds.rows(); j++)
			{
				double [] intensity = Analize.hoseEnds.get(j, i);
				if (intensity[0] == 255)
				{
					ArrayList<Double> distanceVector = new ArrayList<Double>();
					for (int k = 0; k < Analize.circlesList.size(); k++)
					{
						int yDiff, xDiff;
						yDiff = (int) (j - centerVector.get(k).y);
						xDiff = (int) (i - centerVector.get(k).x);
						double distance = Math.sqrt(yDiff*yDiff + xDiff*xDiff);
						distanceVector.add(distance);
					}
					
					double minDistance = distanceVector.get(0);
					int minIndex = 0;
					
					for(int distanceVectorIterator = 0; distanceVectorIterator < distanceVector.size(); distanceVectorIterator++)
					{
						if(distanceVector.get(distanceVectorIterator) < minDistance)
						{
							minDistance = distanceVector.get(distanceVectorIterator);
							minIndex = distanceVectorIterator;
						}
					}
					SkeletonEndsNearCircles skeletonEndNearCircles = new SkeletonEndsNearCircles();
					skeletonEndNearCircles.circleCenterPoint = new Point(centerVector.get(minIndex).x,centerVector.get(minIndex).y);
					skeletonEndNearCircles.distanceFromCricle = minDistance;
					skeletonEndNearCircles.skeletonPoint = new Point(i,j);
					skeletonEndsNearCircles.add(skeletonEndNearCircles);					
					
				}
			}
				
		for(int circleIterator = 0; circleIterator < Analize.circlesList.size(); circleIterator++)
		{
			double minDistance = 0;
			int minIndex = 0;
			
			for(int skelentonEndIterator = 0; skelentonEndIterator < skeletonEndsNearCircles.size(); skelentonEndIterator++)
			{
				if(skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.x == Analize.circlesList.get(circleIterator)[0] &&
				   skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.y == Analize.circlesList.get(circleIterator)[1])
				{
					minDistance = skeletonEndsNearCircles.get(skelentonEndIterator).distanceFromCricle;
					minIndex = skelentonEndIterator;
					break;
				}
			}
			
			for(int skelentonEndIterator = 0; skelentonEndIterator < skeletonEndsNearCircles.size(); skelentonEndIterator++)
			{
				if(skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.x == Analize.circlesList.get(circleIterator)[0] &&
				   skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.y == Analize.circlesList.get(circleIterator)[1] &&
				   skeletonEndsNearCircles.get(skelentonEndIterator).distanceFromCricle < minDistance)
				{
					minDistance = skeletonEndsNearCircles.get(skelentonEndIterator).distanceFromCricle;
					Analize.hoseEnds.put((int)skeletonEndsNearCircles.get(minIndex).skeletonPoint.y,
										 (int)skeletonEndsNearCircles.get(minIndex).skeletonPoint.x,
										 128);
					minIndex = skelentonEndIterator;
				}
				else if(skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.x == Analize.circlesList.get(circleIterator)[0] &&
						skeletonEndsNearCircles.get(skelentonEndIterator).circleCenterPoint.y == Analize.circlesList.get(circleIterator)[1] &&
						skeletonEndsNearCircles.get(skelentonEndIterator).distanceFromCricle > minDistance)
						{
							Analize.hoseEnds.put((int)skeletonEndsNearCircles.get(skelentonEndIterator).skeletonPoint.y,
												 (int)skeletonEndsNearCircles.get(skelentonEndIterator).skeletonPoint.x,
												 128);
						}
				
			}
		}
				
/*				if (intensity[0] == 255)
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
					System.out.println(distanceVector);
					for(int tempIterator = 0; tempIterator < distanceVector.size(); tempIterator++)
					{
						temp.add(distanceVector.get(tempIterator));
					}
					Collections.sort(distanceVector);
					System.out.println(temp);

					double min = distanceVector.get(0); 
					double secondmin = distanceVector.get(1);
					int index, k;

					for (k = 0; k < Analize.circlesList.size(); k++)
					{
						if (temp.get(k) == min && centerVectorFlags.get(k) == true)
						{
							index = k;
							centerVectorFlags.set(k, false);
							System.out.println(centerVector.get(index));
							break;
						}
					}

					if (k == Analize.circlesList.size())
					{
						for (k = 0; k < Analize.circlesList.size(); k++)
						{
							if (temp.get(k) == secondmin && centerVectorFlags.get(k)== true && (1.3*min>=secondmin))
							{
								index = k;
								centerVectorFlags.set(k, false);
								System.out.println(centerVector.get(index));
								break;
							}
						}
						if (k == Analize.circlesList.size())
						{
							Analize.hoseEnds.put(j, i, 128);							
						}
					}
				}*/
						
	}

	public void vegSzamol()	//megszámolja, hogy egy tömlõhöz hány pollen tartozik
	{
		Mat floodFillInput = new Mat();
		Analize.grownhoseEnds.copyTo(floodFillInput);
		int ffillMode = 1;
		int loDiff = 150, upDiff = 150;/*TODO átállítottam 150-re így lett jó, nem tudom 200 al mi baja volt.*/
		int connectivity = 8;
		int newMaskVal = 255;
		Rect ccomp = new Rect();
		int flags = connectivity + (newMaskVal << 8) +
			(ffillMode == 1 ? Imgproc.FLOODFILL_FIXED_RANGE : 0);
		ArrayList<Point> vegpontok = new ArrayList<Point>();
	
		for (int i = 0; i < Analize.hoseEnds.cols(); i++)
			for (int j = 0; j < Analize.hoseEnds.rows(); j++)
			{
				double[] intensity = Analize.hoseEnds.get(j, i);
				if (intensity[0] == 255)
				{
	
					vegpontok.clear();
					vegpontok.add(new Point(i, j));
					Mat mask = new Mat(floodFillInput.rows() + 2, floodFillInput.cols() + 2, CvType.CV_8UC1, new Scalar(0));	//ez a fekete kép, amin csak egy adott tömlõ skeletonja lesz			
					Scalar newVal = new Scalar(255, 255, 255); ///A növesztett régió új színe
					Point seed = new Point(i,j); ///Régiónövesztés gyökere
					Imgproc.floodFill(floodFillInput, mask, seed, newVal, ccomp, new Scalar(loDiff, loDiff, loDiff), new Scalar(upDiff, upDiff, upDiff), flags);	
					
					int counter = 1;
					for (int m = 0; m < Analize.hoseEnds.cols() - 1; m++)
						for (int n = 0; n < Analize.hoseEnds.rows() - 1; n++)
						{
							double[] intensity_mask = mask.get(n+1, m+1);
							double[] intensity_vegek = Analize.hoseEnds.get(n, m);
							if (intensity_mask[0] == 255 && intensity_vegek[0] == 255 && vegpontok.get(0).x != m && vegpontok.get(0).y != n)
							{
								counter++;
								vegpontok.add(new Point(m, n));
								Analize.hoseEnds.put(n, m, 0);			//hogy a külsõ ciklus (i és j) ne számolja bele ezt a pontot
							}			
						}
					if (counter == 1)
					{
						actualLength = 0;
						Analize.onePollenHoseLengths.clear();
						System.out.println("Counter = " + counter + " at: " + vegpontok.get(0));
						mask = skeleton.clearSkeleton(mask);
						HoseLengthMeasure.singleCount(counter, new Point(vegpontok.get(0).x+1, vegpontok.get(0).y+1), mask);
						System.out.println("Max = " + HoseLengthMeasure.getMaxHouseLength());
					}
					else if (counter == 2)
					{
						System.out.println("Counter = " + counter + " at: " + vegpontok.get(0) + " and " + vegpontok.get(1));
						skeleton.clearSkeleton(mask);
					}	
	
					//counter: egy adott tömlõhöz ennyi pollen tartozik
					//vegpontok: tömlõ és pollen csatlakozás pontjainak koordinátái
					//imshow("mask", mask);
	
				}
	
			}
	}
}
