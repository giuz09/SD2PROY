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
		double  vectorBN [] = new double[M];
		double vectorCompletoCN [] = new double[6];
		double vectorResultado[] = new double[N];
		
		double subMatriz1[][] = new double[N][M]; //original
		double subMatriz2[][] = new double[N][M]; 
		
		double subMatrizAux[][] = new double[N][M];
		double matrizFinal[][] = new double[M][M];
		Object objetoFinal=new Object();
		
		double sumatoria = 0.0;
		
		
		///////////////////////////// maestro /////////////////////////////////////////////
		if(me==0)
		{
			
				Random r = new Random();
				for(int i=0;i<N;i++)
				{
					for(int j=0;j<M;j++)
					{
						subMatriz1[i][j]=r.nextDouble();
						subMatriz2[i][j]=r.nextDouble();	
					}				
					vectorBN[i]=r.nextDouble();
				}
					
				envio[0]=(Object)subMatriz1;
				envio[1]=(Object)subMatriz2;
				
				for (int i = 0; i <= 1; i++) {					
						MPI.COMM_WORLD.Send(envio, i,1, MPI.OBJECT, i+1 , 10);  // manda desde la pos 0, 1 objeto, a partir del hilo 1 al 3
						System.out.println("soy el maestro");				
				}		
				System.out.println("Finaliza matriz enviada por el hilo: " + me);
				
				for (int i = 0; i < vectorCompletoCN.length; i++) {
					vectorCompletoCN[i] =0.0;
				}
				for (int i = 0; i < vectorResultado.length; i++) {
					vectorResultado[i] =0.0;
				}
		}
		
		
		///////////////////////////////////////////////////////////////////////////////////////
		
		if (me!=0)
		{
				Object recepcion[]=new Object[2];
				MPI.COMM_WORLD.Recv(recepcion,0, 1, MPI.OBJECT, 0,10);	
				System.out.println(recepcion.toString());
				
				subMatrizAux=(double[][])  recepcion[0];		
				System.out.println("Finaliza matriz recibida por el hilo: " + me);
						
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////
		MPI.COMM_WORLD.Bcast(vectorBN, 0, M, MPI.DOUBLE, 0); //envia vector a los demas hilos
		/////////////////////////////////////////////////////////////////////////////////////////
		if(me!=0) {
				for (int i = 0; i < N; i++) {
					sumatoria = 0.0;
					for (int j = 0; j < M; j++) {	
						
					  sumatoria = (sumatoria +subMatrizAux[i][j]*vectorBN[j]);
					}
					System.out.println("sumatoria = "+sumatoria+" hilo "+me);
					vectorResultado[i] = sumatoria;
				//	System.out.println(vectorResultado[i]);
				}	
					for (int i = 0; i < vectorResultado.length; i++) {
						System.out.println(" i:"+i+"vector resultador"+vectorResultado[i]+"hilo "+me);
					}				
		}

		///////////////////////////////////////////////////////////////////////////////////////////////
		
			MPI.COMM_WORLD.Gather(vectorResultado, 0,vectorResultado.length , MPI.DOUBLE ,vectorCompletoCN, 0, vectorResultado.length, MPI.DOUBLE, 0); //vectorCompleto = B(M)

		
		
		System.out.println("despues del gather"+me);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		if(me ==0) {
			
			for (int i = 0; i < vectorCompletoCN.length; i++) {
				System.out.println("vector completo "+vectorCompletoCN[i]);
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		
	} //llave main

}
