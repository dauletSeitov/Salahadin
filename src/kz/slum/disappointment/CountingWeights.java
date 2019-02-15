package kz.slum.disappointment;

public class CountingWeights {

        private static final Boolean sigmoid = true;
        private static final Double alpha = 0.5;
        private static final Double e = 2.71828;
        private static final Double threshold = 0.5;

        public static void main(String[] args) {

                Byte i1 = 1;
                Byte i2 = 0;

                Double w1=0.45;
                Double w2=0.78;
                Double w3=0.12;
                Double w4=0.13;
                Double w5=1.5;
                Double w6=-2.3;

                Double H1in = i1 * w1 + i2 * w3;
                Double H2in = i1 * w2 + i2 * w4;

                System.out.println("H1in:" + H1in);
                System.out.println("H2in:" + H2in);

                Double H1out = func(H1in);
                Double H2out = func(H2in);

                System.out.println("\nH1out: " + H1out);
                System.out.println("H2out: " + H2out);

                Double O1in = H1out * w5 + H2out * w6;

                Double O1out = func(O1in);

                System.out.println("\nO1out: " + O1out);

        }

        private static Double func (Double d){

                if(sigmoid){
                        return 1/(1 + Math.pow(e, -alpha * d));
                }else
                        return d > threshold ? 1.0 : 0.0;


        }
}
