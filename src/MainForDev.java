import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class MainForDev
{
	/**
	 * képek tesztelésére szolgál
	 * @return: az ablakot adja vissza
	 */
	public static JFrame buildFrame()
	{
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setVisible(true);
        return frame;
	}
	/**
	 * Kirajzolja a képet
	 * @param az image
	 */
	public static void DisplayImage(Image image)
	{
        
        JFrame frame = buildFrame();

        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);        		
            }
        };
        frame.add(pane);
	}
	
    public static Image toBufferedImage(Mat m)
    {
          int type = BufferedImage.TYPE_BYTE_GRAY;
          if ( m.channels() > 1 ) {
              type = BufferedImage.TYPE_3BYTE_BGR;
          }
          int bufferSize = m.channels()*m.cols()*m.rows();
          byte [] b = new byte[bufferSize];
          m.get(0,0,b); // get all the pixels
          BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
          final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
          System.arraycopy(b, 0, targetPixels, 0, b.length);  
          return image;
      }

    /**
     * Konstruktorok és meghívások
     * @param args
     */
	public static void main(String args[])
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		LoadImg li = new LoadImg();
		li.Beolvas();
		li.Resize();
		li.Decolorize();
		

		
		PreFilter pf = new PreFilter();
		pf.preFilter();
		pf.setThreshold();
		
		PollenCounter pc = new PollenCounter();
		pc.pollenCounter();
		
		HosePreFilter hp = new HosePreFilter();
		hp.hoseMorphology();
		hp.setThreshold();
		hp.removeNoiseFromThresholdedImageForHose();
		
		HoseCounter hc = new HoseCounter();
		hc.removePollens();
		hc.HoseCount();

		
		skeleton skel = new skeleton();
		Analize.skeletonImage = skel.createSkeleton();
		Analize.clearedSkeletonImage = skel.clearSkeleton(Analize.skeletonImage);
		
		AssignHoseEndsToPollens a = new AssignHoseEndsToPollens();
		Analize.grownHoseEnds = a.novesztes(Analize.clearedSkeletonImage);
		Analize.HoseEnds = a.vegKeres(Analize.grownHoseEnds);
		a.vegTorles(Analize.HoseEnds);

		
		Image imagetodisplay = toBufferedImage(Analize.HoseEnds);
        DisplayImage(imagetodisplay);
        System.out.printf("Pollen Number is: %d\n", Analize.circleNumber);
        System.out.printf("Hose Number is: %d", Analize.hoseNumber);
	}
}
