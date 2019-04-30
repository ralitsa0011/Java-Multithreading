package testthreadspi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;



public class MyThread extends Thread {
	String nameThread;
	int myRank; //ID of the thread
        long valueK; //number elements of Sum
        int numberThread;
        boolean quiet;
        
        long beginI;// start index of partial sum
        long endI; // end index of partial sum
        MathContext mc; //decimal precision
        BigDecimal partialSum; 
        
	public MyThread(String nameThread, int myRank, long valueK, int numberThread, boolean quiet) {
            this.nameThread = nameThread;
            this.myRank = myRank;
            this.valueK = valueK;
            this.numberThread = numberThread;
            this.quiet = quiet;
            
            this.beginI = (myRank * valueK) / numberThread;
            this.endI = ((myRank + 1) * valueK) / numberThread - 1;
            this.mc = new MathContext(1000); //decimal precision (number after .)
            this.partialSum = BigDecimal.ZERO;
	}

	public void run() {
		partialSum = partialSumOfThread(beginI, endI,  mc);
                if (!quiet) {
                    System.out.println(nameThread + " is starting! " + "Begin index:" + beginI + ",  End index:" + endI);
                    System.out.println(nameThread + ", partSum: " + partialSum.toPlainString());
                }
	}

	       
    public static BigDecimal factorial(long n, MathContext mc) {
        BigDecimal f = BigDecimal.ONE;        
        BigDecimal g = BigDecimal.valueOf(n);
        
        while (g.compareTo(BigDecimal.ONE) == 1) {
            f = f.multiply(g, mc);                  
            g = g.subtract(BigDecimal.ONE);
        }        
        return f;
    }
    
    
    public static BigDecimal powerw(BigDecimal x, long n, MathContext mc)
    {// x^n
        BigDecimal mul = x;
        BigDecimal res = BigDecimal.ONE;
        for(long i = 0; i < n; i++)
        {
            res = res.multiply(mul, mc);
        }
        return res;
    }
   
    public static BigDecimal partialSumOfThread(long start, long end, MathContext mc)
    {
        BigDecimal partialSum = BigDecimal.ZERO;
        BigDecimal numerator = BigDecimal.ONE;
        BigDecimal divisor = BigDecimal.ONE;
             
        for(long k = start; k <= end; k++)
        {
            numerator = BigDecimal.valueOf(1103 + (26390 * k));            
            numerator = numerator.multiply(factorial(4*k, mc), mc);            
            divisor = powerw(BigDecimal.valueOf(396), 4*k, mc);            
            divisor = divisor.multiply(powerw(factorial(k,mc),4, mc));            
            partialSum = partialSum.add(numerator.divide(divisor, mc), mc);                    
        }        
        return partialSum;
    }
}
