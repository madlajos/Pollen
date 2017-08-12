import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import utility.PointToReturnType;

public class HoseLengthMeasure extends AssignhoseEndsToPollens {
	
	static ArrayList<PointToReturnType> pointsToReturn = new ArrayList<PointToReturnType>();
	
	public static void singleCount(int counter, Point vegpont, Mat mask) //megméri egy oylan tömlõ hosszát, ami csak egy pollenhez tartozik
	{
		int y = (int) vegpont.y;
		int x = (int) vegpont.x;
		
		double[] color = mask.get(y, x) ;

		mask.put(y, x, 80); // 80 = itt már voltunk

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

					if (num == 0)  //végponthoz értünk
					{
						if (pointsToReturn.size() == 0)
							return;

						Analize.onePollenHoseLengths.add(actualLength);	//Ez a végleges hossz, ami egy végponthoz tartozik
						Point nextPoint = pointsToReturn.get(pointsToReturn.size()-1).point;
						actualLength = pointsToReturn.get(pointsToReturn.size() - 1).length;
						pointsToReturn.remove(pointsToReturn.size() - 1);
						singleCount(1, nextPoint, mask);
						return;
					}
					else    //megyünk tovább
					{
						Point nextPoint = positions[firstIndex];
						actualLength = actualLength + lengthIndicator[firstIndex];	//Az a hossz aminél éppen járunk
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
}
