import java.text.DecimalFormat;
import java.util.Random;

import mpi.Datatype;
import mpi.MPI;

public class MPIMultMatriz2 {

	static int N=2;
	static int M=4;	
	static Object envio[]=new Object[2];
	static double  vectorBN [] = new double[M];
	static double vectorCompletoC [] = new double[6];
	static double vectorSubC[] = new double[N];
	static double vectorCompletoD [] = new double[6];
	static double vectorSubD[] = new double[N];
	static double matrizGigante[][]=new double[M][M];
	static double subMatriz1[][] = new double[N][M]; //original
	static double subMatriz2[][] = new double[N][M]; 	
	static double subMatrizAux[][] = new double[N][M];
	static double matrizFinal[][] = new double[M][M];
	static Object objetoFinal=new Object();
	static double sumatoria = 0.0;
	static int me,size;
	

	
	public static void dividoMatriz(double [][] matrizGigante,Integer M,double [][] subM1,double [][] subM2 ) {
		Integer N = (M/2);
		inicializoMatrizAleatorios(matrizGigante); 
		System.out.println("MATRIZ INICIAL  A ");
		muestroMatrizG(matrizGigante);
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < matrizGigante.length; j++) {
				subM1[i][j]= matrizGigante[i][j];
			}
		}
		for (int i = 2; i < M; i++) {
			for (int j = 0; j < matrizGigante.length; j++) {
				subM2[i-2][j]= matrizGigante[i][j];
			}
		}
	}	
	
	public static double[] multiMatrizVector(double[][] matriz,double[] vector) {
		
		
		 double  vectorResus [] = new double[N];
		inicializoVector00(vectorResus);
		////////metodo que mutiplica matriz por vector
		for (int i = 0; i < N; i++) {
			sumatoria = 0.0;
			for (int j = 0; j < M; j++) {	
				
			  sumatoria = (sumatoria +matriz[i][j]*vector[j]);
			}
			vectorResus[i] = sumatoria;
		}	
			
			return vectorResus;
	}
	
	public static double productoVectores(double[] vectorA, double[] vectorB) {
		double resu =0.00;
		for (int i = 0; i < vectorB.length; i++) {
			resu = resu + (vectorA[i]*vectorB[i]);
		}
		return resu;
	}
	
	public static void muestrovector(double[] vector) {
		for (int i = 0; i < vector.length; i++) {
			System.out.println("	"+i+"----"+vector[i]);
		}	
		System.out.println(".........................................................");
	}
	
	public static void muestroMatriz(double[][]matriz) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print("	"+matriz[i][j]);
			}
			System.out.println(" ");
		}	
		
	}	
	public static void muestroMatrizG(double[][]matriz) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print("	"+matriz[i][j]);
			}
			System.out.println(" ");
		}	
		
	}	
	
	public static void inicializoMatrizAleatorios(double[][] matriz) {
		
		Random r = new Random();
		for(int i=0;i<M;i++)
		{
			for(int j=0;j<M;j++)
			{
				matriz[i][j]=r.nextDouble();	
			}				
		}	
	}
	
	public static void inicializoVectorAleatorios(double[] vector) {
		Random r = new Random();
		for(int i=0;i<vector.length;i++)
		{				
			vector[i]=r.nextDouble();
		}	
	}
	
	public static void inicializoVector00(double [] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] =0.0;
		}
	}
	public static void main(String[] args) {
		
		
		args = MPI.Init(args);
		me = MPI.COMM_WORLD.Rank();
		size = MPI.COMM_WORLD.Size();
		
		///////////////////////////////////////////////////////////////////////////////////
		if(me==0)
		{
			
				//inicializoMatrizAleatorios(subMatriz1); 	
				//inicializoMatrizAleatorios(subMatriz2);
				dividoMatriz(matrizGigante, M, subMatriz1, subMatriz2);
				inicializoVectorAleatorios(vectorBN); 
				inicializoVector00(vectorCompletoC);
				inicializoVector00(vectorSubC);
				inicializoVector00(vectorCompletoD);
				inicializoVector00(vectorSubD);
				envio[0]=(Object)subMatriz1;
				envio[1]=(Object)subMatriz2; 
				
				for (int i = 0; i <= 1; i++) {
					MPI.COMM_WORLD.Send(envio, i,1, MPI.OBJECT, i+1 , 10);}	 
				System.out.println("Matriz sub1"); muestroMatriz(subMatriz1);
				System.out.println("Matriz sub2"); muestroMatriz(subMatriz2);
				System.out.println("vector B");muestrovector(vectorBN);				
		}
		
		////////////////////////////////////////////////////////////////////////////////////	
		if(me!=0) {
		Object recepcion[]=new Object[2];
		MPI.COMM_WORLD.Recv(recepcion,0, 1, MPI.OBJECT, 0,10);	
		subMatrizAux=(double[][]) recepcion[0];	
		
		}
			
		////////////////////////////////////////////OBTENCION DE VECTOR C//////////////////////////////////////////////
		MPI.COMM_WORLD.Bcast(vectorBN, 0, M, MPI.DOUBLE, 0); //envia vector a los demas hilos
			
		if(me!=0) {vectorSubC = multiMatrizVector(subMatrizAux,vectorBN);} //cada hilo multiplica su submatriz por el vector
		
		MPI.COMM_WORLD.Gather(vectorSubC, 0,vectorSubC.length , MPI.DOUBLE ,vectorCompletoC, 0, vectorSubC.length, MPI.DOUBLE, 0); //vectorCompleto = B(M)	
		
		if(me==0) {
			System.out.println("Vector C(N) = A(N x N) x B(N) ");
			muestrovector(vectorCompletoC);}
		////////////////////////////////////////////////OBTENCION DEL VECTOR D///////////////////////////////////////////////
		
		MPI.COMM_WORLD.Bcast(vectorCompletoC, 0, M, MPI.DOUBLE, 0); //envia vector C a todos
		
		if(me!=0) {vectorSubD = multiMatrizVector(subMatrizAux,vectorCompletoC);}
		
		MPI.COMM_WORLD.Gather(vectorSubD, 0,vectorSubD.length , MPI.DOUBLE ,vectorCompletoD, 0, vectorSubD.length, MPI.DOUBLE, 0); //vectorCompleto = B(M)	
		
		if (me==0) {
			System.out.println("Vector D(N) = A(NxN) × C(N)\r\n" + 
					"");
			muestrovector(vectorCompletoD);
			System.out.println("X = C(N) x D(N)= ");
			System.out.println("		"+productoVectores(vectorCompletoC, vectorCompletoD));
			
		}
		///////////////////////////////////////////OBTENGO C * D //////////////////////////////////////////////
		
		
	} //llave main

}
