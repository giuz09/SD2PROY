import java.util.Random;

import mpi.MPI;

public class calcularMatriz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int actual, cantHilos;
		double sumaParcial[] = new double[1]; // suma parcial de la matriz
		double sumaTotal[]=new double[1];  // suma total de la matriz
		
		Random rand = new Random();
		args = MPI.Init(args); //inicializo region paralela
		actual = MPI.COMM_WORLD.Rank(); //identificacion del proceso
		cantHilos = MPI.COMM_WORLD.Size();
		int data[][]=new int[50000][50000]; //defino la matriz
		
		
		
		//metodo inicializacion de matriz
		if(actual==0)
		{
			//inicializo el vector como master con valores de 0 a 9 aleatorios
			for(int i=0;i<50000;i++)
			{
				for(int j=0;j<50000;j++)
				{				
				data[i][j]=rand.nextInt(10);
				}
			}		
		}

		
		//metodo calcular matriz
		
		
		
		MPI.Finalize();
	}
	
	
	
}
