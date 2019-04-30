package testthreadspi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;

public class Pi{

	static long kParamOfSum; //number elements of the sum [0 .. kParamOfSum-1]
        static int numThread; // number threads
	static String threadName;
	static boolean quiet = false;
	static MyThread[] array;

	
	public static void main(String[] args) {
		long timeOfStart = Calendar.getInstance().getTimeInMillis();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p")) {
				kParamOfSum = new Long(args[i + 1]);
			}
			if (args[i].equals("-t")) {
				numThread = new Integer(args[i + 1]);
			}
			if (args[i].equals("-q")) {
				quiet = true;
			}
		}
                if (!quiet) {
                    //System.out.println("args.length: " + args.length);
                    System.out.println("numThread: " + numThread);
                    System.out.println("k: " + kParamOfSum + " (max:1000)");
                }
		if (kParamOfSum <= 0 || numThread <= 0 || args.length < 4
				|| args.length > 5 || kParamOfSum <= numThread || kParamOfSum > 1000) {
			System.out.println("ERROR: Args are not correct!!!");
		} else {
			array = new MyThread [numThread];
			for (int t = 0; t < numThread; t++) {
				array[t] = new MyThread("Thread " + Integer.toString(t), t, kParamOfSum, numThread, quiet);
                                array[t].start();
			}
		
                MathContext mc = new MathContext(1000);//decimal precision (number after .)
                BigDecimal sumOfThread = BigDecimal.ZERO;
		for (int i = 0; i < numThread; i++) {
			try {
				array[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sumOfThread = sumOfThread.add(array[i].partialSum, mc);
		}
                               
                //System.out.println("sumOfThread: "+sumOfThread);
                
                BigDecimal leftConst = new BigDecimal("1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605585073721264412149709993583141322266592750559275579995050115278206057147"); // sqrt(2)
                leftConst = leftConst.multiply(BigDecimal.valueOf(2), mc);
                leftConst = leftConst.divide(BigDecimal.valueOf(9801), mc);
                
                BigDecimal resultPI = BigDecimal.ONE.divide(leftConst.multiply(sumOfThread, mc), mc);
		
		long timeOfEnd = Calendar.getInstance().getTimeInMillis();
		
		System.out.println("Time of calculate Pi: " + (timeOfEnd - timeOfStart)	+ " millis");
		if (!quiet) {
                    System.out.println("Calculate Pi:    "+resultPI);
                    System.out.println("pi (200 digits): 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652");
                }
                }

	}
}
