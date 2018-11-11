import java.util.Random;

import mpi.Datatype;
import mpi.MPI;

public class MPIMultMatriz2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int me,size;
		args = MPI.Init(args);
		me = MPI.COMM_WORLD.Rank();
		size = MPI.COMM_WORLD.Size();
		int N=2;
		int M=4;
		
		Object envio[]=new Object[2];
		double [] vectorB = new double[M];
		double matrizResultado1[][] = new double[N][M];
		double matrizResultado2[][] = new double[N][M];
		
		double subMatriz1[][] = new double[N][M]; //oriinal
		double subMatriz2[][] = new double[N][M]; 
		
		double subMatrizAux1[][] = new double[N][M];
		double subMatrizAux2[][] = new double[N][M];
		double matrizFinal[][] = new double[M][M];
		Object objetoFinal=new Object();
		Object matrizCompleta = null;
		
		
		///////////////////////////// maestro /////////////////////////////////////////////
		if(me==0)
		{
			//master
		
		////////////////////////////////inicializa el matriz
		Random r = new Random();
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				subMatriz1[i][j]=r.nextDouble();
				subMatriz2[i][j]=r.nextDouble();	
	
			}				
			vectorB[i]=r.nextDouble();			
			System.out.println(subMatriz1);			
		}
		/////////////////////////////////// fin inicializa matriz  ///////
		
		envio[0]=(Object)subMatriz1;
		envio[1]=(Object)subMatriz2;
		
		for (int i = 0; i < 2; i++) {
			//
			MPI.COMM_WORLD.Send(envio, 0, 1, MPI.OBJECT, (i+1), 10);  // manda desde la pos 0, 1 objeto, a partir del hilo 1 al 3
		}
		System.out.println("Finaliza matriz enviada por el hilo: " + me);
	
		}////////////////////    fin maestro  ///////////////////////////////////////////////////////////////////
		
		
		//////////////////////////// otros hilos ///////////////////////////////////////////////////////////
		else
		{
			Object recepcion[]=new Object[1];
			MPI.COMM_WORLD.Recv(recepcion,0, 1, MPI.OBJECT, 0,10);	
			System.out.println(recepcion.toString());
			 subMatrizAux2=(double[][])  recepcion[0];		
			System.out.println("Finaliza matriz recibida por el hilo: " + me);
					
		}
		//////////////////////////////////////////////////////////////////////////////////////
		MPI.COMM_WORLD.Bcast(vectorB, 0, M, MPI.DOUBLE, 0);
		System.out.print("vector---  ");	
		for(int i=0;i<N;i++)
		{
			System.out.print(vectorB[i]+ " ");			
			
		}
		System.out.print("---  ");
		System.out.println();
		System.out.println("Finaliza el vector recibido por el hilo: " + me);	
		//////////////////////////////	
		
		System.out.println("La submatriz es");
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < vectorB.length; j++) {
				
				matrizResultado1[i][j] = subMatrizAux2[i][j]*vectorB[j];
				System.out.println("---"+matrizResultado1[i][j]+ " ");
			
			}
			
			
		}
		/// falta asignas ambas submatrices a una submatriz
		objetoFinal = matrizFinal;
		

		MPI.COMM_WORLD.Gather(objetoFinal, 0,1 , MPI.OBJECT ,matrizCompleta, 0, 1, MPI.OBJECT, 0);
		
		
	
	} //llave main

}
