import java.util.Random;

import mpi.MPI;

public class calculoMatrizSecuencial {
	
	static Integer N = 800;
	
	public static void muestrovector(double[] vector) {
		for (int i = 0; i < vector.length; i++) {
			System.out.println("	"+i+"----"+vector[i]);
		}	
		System.out.println(".........................................................");
	}
	
	public static void muestroMatriz(double[][]matriz) {
		for (int i = 0; i < N ; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print("	"+matriz[i][j]);
			}
			System.out.println(" ");
		}	
		
	}	
	
	
	public static void main(String[] args) {
	//declaracion variables
	//long startTime = System.nanoTime();
		long startTime = System.currentTimeMillis();
	double start = MPI.Wtime();
	
	Integer i;
	Integer j;
	double sum;
	double [][] matrizA = new double[N][N];
	double [] B = new double[N];
	double [] C = new double[N];
	double [] D = new double[N];
	
	///////////////inicializa el matriz
	Random r = new Random();
	for (i= 0; i<N; ++i) {
		for (j= 0; j<N; ++j) {
		    matrizA[i][j]=r.nextDouble();
		}
		B[i]=r.nextDouble();
	}
	///////////////comienza el calculo
	for (i= 0; i<N; ++i) {
		C[i]=0.0;
		for (j= 0; j<N; ++j) {
		    C[i]=C[i]+matrizA[i][j]*B[j];
		}
	}
	sum = 0.0;
	for (i= 0; i<N; ++i) {
		D[i]=0.0;
		for (j= 0; j<N; ++j) {
		    D[i]=D[i]+matrizA[i][j]*C[j];
		}
		sum = sum+D[i]*C[i];
	}
	double end = MPI.Wtime();
	//////////////resultado de X 
	muestroMatriz(matrizA);
	System.out.println("vector B");
	muestrovector(B);	
	System.out.println("Vector C(N) = A(N x N) x B(N) ");
	muestrovector(C);
	System.out.println("Vector D(N) = A(NxN) × C(N)\r\n" + "");
	muestrovector(D);
	System.out.println("X es "+sum);
	//System.out.println("Empieza: "+start+" termina: "+end);
	//System.out.println("El tiempo es: "+String.valueOf(end-start));
	
	long estimatedTime = (System.currentTimeMillis()-startTime);
	System.out.println("El tiempo es: "+estimatedTime);
	}	
}
