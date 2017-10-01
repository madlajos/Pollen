import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import utility.PointToReturnType;

public class HoseLengthMeasure extends AssignhoseEndsToPollens {
	
	static ArrayList<PointToReturnType> pointsToReturn = new ArrayList<PointToReturnType>();
	static ArrayList<Point> branches = new ArrayList<Point>();
	static ArrayList<Point> pointsToRecolor = new ArrayList<Point>();
	
	public static void singleCount(int counter, Point vegpont, Mat mask) //megm�ri egy oylan t�ml� hossz�t, ami csak egy pollenhez tartozik
	{
		int y = (int) vegpont.y;
		int x = (int) vegpont.x;
		
		double[] color = mask.get(y, x) ;

		mask.put(y, x, 80); // 80 = itt m�r voltunk

					double [] n0 = mask.get(y, x + 1);
					double [] n1 = mask.get(y + 1, x + 1);
					double [] n2 = mask.get(y + 1, x);
					double [] n3 = mask.get(y + 1, x - 1);
					double [] n4 = mask.get(y, x - 1);
					double [] n5 = mask.get(y - 1, x - 1);
					double [] n6 = mask.get(y - 1, x);
					double [] n7 = mask.get(y - 1, x + 1);

					double neighbors[] = { n0[0], n1[0], n2[0], n3[0], n4[0], n5[0],
						n6[0], n7[0] };

					Point positions[] = { new Point(x+1, y), new Point(x + 1, y + 1), new Point(x, y + 1),
										 new Point(x - 1, y + 1), new Point(x - 1, y), new Point(x - 1, y - 1),
										 new Point(x, y - 1), new Point(x + 1, y - 1) };
					
					double lengthIndicator[] = { 1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2)};

					int firstIndex = 0, num=0, k;
					
					for (k = 0; k < 8; k++)
					{
						if (neighbors[k] == 128 || neighbors[k] == 254)
						{
							if (num == 0)
								firstIndex = k;
							else
							{
								PointToReturnType branch = new PointToReturnType();
								branch.length = actualLength;
								branch.point = positions[k];
								pointsToReturn.add(branch);
							}
							num++;
						}
					}

					if (num == 0)  //v�gponthoz �rt�nk
					{
						if (pointsToReturn.size() == 0)
							return;

						Analize.onePollenHoseLengths.add(actualLength);	//Ez a v�gleges hossz, ami egy v�gponthoz tartozik
						Point nextPoint = pointsToReturn.get(pointsToReturn.size()-1).point;
						actualLength = pointsToReturn.get(pointsToReturn.size() - 1).length;
						pointsToReturn.remove(pointsToReturn.size() - 1);
						singleCount(1, nextPoint, mask);
						return;
					}
					else    //megy�nk tov�bb
					{
						Point nextPoint = positions[firstIndex];
						actualLength = actualLength + lengthIndicator[firstIndex];	//Az a hossz amin�l �ppen j�runk
						singleCount(1, nextPoint, mask);
						//return;
					}
					
	}
	
	public static double getMaxHouseLength()
	{
		double max = onePollenHoseLengths.get(0);

		for (int i = 0;  i < onePollenHoseLengths.size(); i++)
		{
			if (onePollenHoseLengths.get(i) > max)
				max = onePollenHoseLengths.get(i);
		}

		return max;
	}

//	public static void findOtherEnd(Point currentPosition, Mat mask, Point pointToFind)	//doubleCount sz�m�ra megtal�lja az �tvonalat a k�t v�gpont k�zt, az utat 80-ra sz�nezi
//	{
//		int y = (int) currentPosition.y;
//		int x = (int) currentPosition.x;
//		mask.put(y, x, 80); // 80 = itt m�r voltunk. Ebben a f�ggv�nyben most a m�sik pollenhez tartoz� v�gpontot keress�k, ez�rt a 80-ra sz�nezett pontokat a 
//									//pointsToRecolor vektorban t�roljuk, �s visszasz�nezz�k az eredetire ha visszat�r�nk egy v�gpontt�l az el�gaz�shoz.
//
//		double [] n0 = mask.get(y, x - 1);
//		double [] n1 = mask.get(y - 1, x - 1);
//		double [] n2 = mask.get(y - 1, x);
//		double [] n3 = mask.get(y - 1, x + 1);
//		double [] n4 = mask.get(y, x + 1);
//		double [] n5 = mask.get(y + 1, x + 1);
//		double [] n6 = mask.get(y + 1, x);
//		double [] n7 = mask.get(y + 1, x - 1);
//		double neighbors[] = { n0[0], n1[0], n2[0], n3[0], n4[0], n5[0],
//			n6[0], n7[0] };
//
//		Point positions[] = { new Point(x + 1, y), new Point(x + 1, y + 1), new Point(x, y + 1),
//				new Point(x - 1, y + 1), new Point(x - 1, y), new Point(x - 1, y - 1),
//				new Point(x, y - 1), new Point(x + 1, y - 1) };
//
//		int firstIndex = 0; 
//		int num = 0; 
//		int k;
//		boolean isElement = false; //megmondja, hogy egy adott pont amit vizsg�lok, benne van-e m�r a pointsToReturn vektorban.
//
//		for (k = 0; k < 8; k++)
//		{
//			isElement = false;
//			for (int l = 0; l < pointsToReturn.size(); l++)
//			{
//				if (pointsToReturn.get(l).point == positions[k])
//					isElement = true;
//			}
//			if ((neighbors[k] == 128 || neighbors[k] == 254) && !isElement)	
//			{
//				if (num == 0)
//					firstIndex = k;
//				else
//				{
//					PointToReturnType branch = new PointToReturnType();
//					//branch.length = actualLength; //Itt m�g nem sz�m�t a hossz, mert a t�ml� m�sik v�g�hez keress�k csak az �tvonalat
//					branch.point = positions[k];
//					pointsToReturn.add(branch);
//					if (num == 1)
//					branches.add(currentPosition);	//Azt a pontot mentj�k le a sz�nez�shez, ahonnan indul az el�gaz�s, de csak egyszer, ez�rt az if el�tte..
//				}
//				num++;
//			}
//		}
//
//		if (num == 0)  //v�gponthoz �rt�nk
//		{
//			if (currentPosition == pointToFind)	//ha megtal�ltuk a m�sik pollenhez tartoz� v�gpontot akkor az �t be van sz�nezve 80-ra �s visszat�rhet�nk.
//				return;
//
//			for (int i = pointsToRecolor.size()-1; i >= 0; i--)
//			{
//				mask.put((int)pointsToRecolor.get(i).y,(int) pointsToRecolor.get(i).x,20);
//				if (branches.size() !=0)
//				if (pointsToRecolor.get(i-1) ==  branches.get(branches.size() - 1))	//csak az utols� el�gaz�sig �ll�tjuk vissza a sz�neket
//				{
//					branches.remove(branches.size() - 1);
//					
//					if (branches.size() == 0)
//					{
//						pointsToRecolor.remove(pointsToRecolor.size() - 1);
//					}
//					break;
//				}
//				pointsToRecolor.remove(pointsToRecolor.size() - 1);
//
//			}
//			//onePollenHoseLengths.push_back(actualLength);	//Ez a v�gleges hossz, ami egy v�gponthoz tartozik
//			Point nextPoint = pointsToReturn.get(pointsToReturn.size() - 1).point;
//			//actualLength = pointsToReturn[pointsToReturn.size() - 1].length;
//			pointsToReturn.remove(pointsToReturn.size() - 1);
//			findOtherEnd(nextPoint, mask, pointToFind);
//			return;
//		}
//		else    //megy�nk tov�bb
//		{
//			Point nextPoint = positions[firstIndex];
//			//actualLength = actualLength + lengthIndicator[firstIndex];	//Az a hossz amin�l �ppen j�runk
//			pointsToRecolor.add(currentPosition);
//			findOtherEnd(nextPoint, mask, pointToFind);
//			//return;
//		}
//	}
//
//	public static  double measureDoubleLength(Mat mask, Point start, Point end)
//	{
//		double length = 0;
//		double lengthIndicator[] = { 1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2) };
//		Point actualPoint = start;;
//		ArrayList<Point> branches_double = new ArrayList<Point>();
//
//		while (actualPoint != end)
//		{
//			int y = (int) actualPoint.y;
//			int x = (int) actualPoint.x;
//
//			double [] n0 = mask.get(y, x - 1);
//			double [] n1 = mask.get(y - 1, x - 1);
//			double [] n2 = mask.get(y - 1, x);
//			double [] n3 = mask.get(y - 1, x + 1);
//			double [] n4 = mask.get(y, x + 1);
//			double [] n5 = mask.get(y + 1, x + 1);
//			double [] n6 = mask.get(y + 1, x);
//			double [] n7 = mask.get(y + 1, x - 1);
//			double neighbors[] = { n0[0], n1[0], n2[0], n3[0], n4[0], n5[0],
//				n6[0], n7[0] };
//
//			Point positions[] = { new Point(x + 1, y), new Point(x + 1, y + 1), new Point(x, y + 1),
//					new Point(x - 1, y + 1), new Point(x - 1, y), new Point(x - 1, y - 1),
//					new Point(x, y - 1), new Point(x + 1, y - 1) };
//
//			for (int i = 0; i < 8; i++)
//			{
//				if (neighbors[i] == 128 || neighbors[i] == 20)
//				{
//					boolean contain = false;
//					for (int k = 0; k < branches_double.size(); k++)	//Megvizsg�ljuk, hogy az el�gaz�s benne van-e m�r a vektorban
//					{
//						if (branches_double.get(k) == positions[i])
//						{
//							contain = true;
//						}
//					}
//					if (contain == false)							//Ha az el�gaz�s nincs benne a vektorban akkor az
//						branches_double.add(positions[i]);	//el�gaz�sokat mentj�k
//				}
//				if (neighbors[i] == 80)
//				{
//					mask.put(y, x, 70); //�j sz�n a k�t pollen k�zti �tvonalon
//					actualPoint = positions[i];
//					length = length + lengthIndicator[i];
//				}
//			}
//		}
//		System.out.println(branches_double.size());
//		while (branches_double.size() > 0)
//		{
//			actualLength = 0;
//			onePollenHoseLengths.clear();
//			singleCount(1, branches_double.get(branches_double.size() -1 ), mask, 128, 20);/*TODO modositsd singlecountot*/
//			branches_double.remove(branches_double.size() -1 );
//			System.out.println(getMaxHouseLength());
//		}
//		return length;
//	}
//
//	public static void doubleCount(ArrayList<Point> vegpontok, Mat mask)
//	{
//		int y_1 = (int) (vegpontok.get(0).y+1);
//		int x_1 = (int) (vegpontok.get(0).x+1);
//
//		int y_2 = (int) (vegpontok.get(1).y+1);
//		int x_2 = (int) (vegpontok.get(1).x+1);
//
//		ArrayList<Point> elagazasok = new ArrayList<Point>();
//		String a = String.valueOf(vegpontok.get(0).x);
//
//		findOtherEnd(new Point(x_1, y_1), mask, new Point(x_2, y_2));
//		double connectedLength = measureDoubleLength(mask,new Point(x_1,y_1),new Point(x_2,y_2));
//
//		return;
//	}
}
