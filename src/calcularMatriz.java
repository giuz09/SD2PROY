import java.util.Random;

import mpi.MPI;

public class calcularMatriz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double start = MPI.Wtime();
		int actual, cantHilos;
		double sumaParcial[][] = new double[1][1]; // suma parcial de la matriz
		double sumaTotal[][]=new double[1][1];  // suma total de la matriz
		
		Random rand = new Random();
		args = MPI.Init(args); //inicializo region paralela
		actual = MPI.COMM_WORLD.Rank(); //identificacion del proceso
		cantHilos = MPI.COMM_WORLD.Size();
		int data[][]=new int[50000][50000]; //defino la matriz
		int dataParcial[][]=new int[12500][12500];
		
		
		
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
		
		//divido los datos en cuatro partes iguales
				MPI.COMM_WORLD.Scatter(data, 0, 12499, MPI.INT, dataParcial, 0, 12499, MPI.INT, 0);
		//trabajo de cada hilo que pasa por aca	
				for(int i=0;i<12500;i++)
					{
						for(int j=0;j<50000;j++)
						{	
						sumaParcial[0][0]=sumaParcial[0][0] + dataParcial[i][j];
						}
					}
				System.out.println("Suma parcial hilo : " + actual + " " + sumaParcial[0]);
				
				
		//devolver al master la reducción en suma de cada cuenta parcial
				//metodo calcular matriz va aca?
				MPI.COMM_WORLD.Reduce(sumaParcial, 0, sumaTotal, 0, 1, MPI.DOUBLE, MPI.SUM, 0);
				
				double end = MPI.Wtime();
				
		//solo el maestro imprime los valores
				if(actual==0)
				{
				System.out.println("La suma total es: " + sumaTotal[0][0] + " y el tiempo es: " + String.valueOf(end-start));
				}
		
		MPI.Finalize();
	}
	
	
	
}
