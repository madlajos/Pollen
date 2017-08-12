import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class skeleton extends HoseOperations
{

	public Mat createSkeleton()
	{
		Mat skeleton = new Mat();
		Mat tmpHoses = new Mat();
		tmpHoses = Analize.hoses;
		Imgproc.threshold(tmpHoses, tmpHoses, 80, 255, 0);
		Mat skel_src = new Mat();
		tmpHoses.convertTo(skel_src, CvType.CV_32F);

		for (int i = 0; i<15; i++)
		{
			ThinSubiteration(skel_src, skeleton);
			skel_src = skeleton;
		}

		Mat skeleton_final = new Mat();
		skeleton.convertTo(skeleton_final, CvType.CV_8U);

		return skeleton_final;
	}
	
	public void ThinSubiteration(Mat pSrc, Mat pDst) 
	{
		int rows = pSrc.rows();
		int cols = pSrc.cols();
		pSrc.copyTo(pDst);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (pSrc.get(i, j)[0] == 255) {
					/// get 8 neighbors
					/// calculate C(p)
					double [] neighbor0 = pSrc.get(i - 1, j - 1);
					double [] neighbor1 = pSrc.get(i - 1, j);
					double [] neighbor2 = pSrc.get(i - 1, j + 1);
					double [] neighbor3 = pSrc.get(i, j + 1);
					double [] neighbor4 = pSrc.get(i + 1, j + 1);
					double [] neighbor5 = pSrc.get(i + 1, j);
					double [] neighbor6 = pSrc.get(i + 1, j - 1);
					double [] neighbor7 = pSrc.get(i, j - 1);

					boolean neighbor0_bit, neighbor1_bit, neighbor2_bit, neighbor3_bit,
						neighbor4_bit, neighbor5_bit, neighbor6_bit, neighbor7_bit,
						neighbor8_bit;

					
					if (neighbor0[0] == 255)
						neighbor0_bit = true;
					else
						neighbor0_bit = false;

					if (neighbor1[0] == 255)
						neighbor1_bit = true;
					else
						neighbor1_bit = false;

					if (neighbor2[0] == 255)
						neighbor2_bit = true;
					else
						neighbor2_bit = false;

					if (neighbor3[0] == 255)
						neighbor3_bit = true;
					else
						neighbor3_bit = false;

					if (neighbor4[0] == 255)
						neighbor4_bit = true;
					else
						neighbor4_bit = false;

					if (neighbor5[0] == 255)
						neighbor5_bit = true;
					else
						neighbor5_bit = false;

					if (neighbor6[0] == 255)
						neighbor6_bit = true;
					else
						neighbor6_bit = false;

					if (neighbor7[0] == 255)
						neighbor7_bit = true;
					else
						neighbor7_bit = false;

					int C = ((!neighbor1_bit && (neighbor2_bit || neighbor3_bit) ? 1 : 0) +
							(!neighbor3_bit && (neighbor4_bit || neighbor5_bit) ? 1 : 0) +
							(!neighbor5_bit && (neighbor6_bit || neighbor7_bit) ? 1 : 0) +
							(!neighbor7_bit && (neighbor0_bit || neighbor1_bit) ? 1 : 0));
					if (C == 1) {
						/// calculate N
						int N1 = (neighbor0_bit | neighbor1_bit ? 1 : 0) +
							(neighbor2_bit | neighbor3_bit ? 1 : 0) +
							(neighbor4_bit | neighbor5_bit ? 1 : 0) +
							(neighbor6_bit | neighbor7_bit ? 1 : 0);
						int N2 = (neighbor1_bit | neighbor2_bit ? 1 : 0) +
							(neighbor3_bit | neighbor4_bit ? 1 : 0) +
							(neighbor5_bit | neighbor6_bit ? 1 : 0) +
							(neighbor7_bit | neighbor0_bit ? 1 : 0);
						int N = Math.min(N1, N2);
						if ((N == 2) || (N == 3)) {
							/// calculate criteria 3
							int c3 = ((neighbor1_bit | neighbor2_bit | !neighbor4_bit) & neighbor3_bit) ? 1 : 0;
							if (c3 == 0) {
								pDst.put(i,j,0);
							}
						}
					}
				}
			}
		}
	}
	
	public static Mat clearSkeleton(Mat skeleton)
	{
		Mat tmp = new Mat();
		skeleton.copyTo(tmp);
		int ffillMode = 1;
		int loDiff = 20, upDiff = 20;
		int connectivity = 8;
		Scalar newVal = new Scalar(128, 128, 128);
		int newMaskVal = 128;
		Rect ccomp = new Rect();
		int flags = connectivity + (newMaskVal << 8) +
			(ffillMode == 1 ? Imgproc.FLOODFILL_FIXED_RANGE : 0);

		Mat mask = new Mat(tmp.rows() + 2, tmp.cols() + 2, CvType.CV_8UC1, new Scalar(0));
		for (int i = 0; i< tmp.cols(); i++)
			for (int j = 0; j < tmp.rows(); j++) ///pixelenként vizsgáljuk a képet
			{
				double [] intensity = tmp.get(j, i);
				if (intensity[0] == 255)
				{
					Point seed = new Point(i, j);
					Imgproc.floodFill(tmp, mask, seed, newVal, ccomp, new Scalar(loDiff, loDiff, loDiff), new Scalar(upDiff, upDiff, upDiff), flags);

					for (int m = 1; m< tmp.cols() - 1; m++)
						for (int n = 1; n < tmp.rows() - 1; n++)
						{

							double [] intensity2 = tmp.get(n, m);
							if (intensity2[0] == 128)
							{
								double [] n0 = tmp.get(n, m - 1);
								double [] n1 = tmp.get(n - 1, m - 1);
								double [] n2 = tmp.get(n - 1, m);
								double [] n3 = tmp.get(n - 1, m + 1);
								double [] n4 = tmp.get(n, m + 1);
								double [] n5 = tmp.get(n + 1, m + 1);
								double [] n6 = tmp.get(n + 1, m);
								double [] n7 = tmp.get(n + 1, m - 1);
								double neighbors[] = { n0[0], n1[0], n2[0], n3[0], n4[0], n5[0],
									n6[0], n7[0] };

								int num = 0;

								for (int k = 0; k < 8; k++)
								{
									if (neighbors[k] == 128)
										num++;
								}

								for (int k = 0; k < 8; k++)
								{
									if (neighbors[k] == 254)
										num = 0;
								}


								if (num == 1)
								{
									tmp.put(n, m,254);									
								}

							}

						}
				}
			}
		return tmp;
	}
}