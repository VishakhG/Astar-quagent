

import java.util.*;

class PathWalker extends Quagent {
	/*This class walks the shortest path 
	between the start position of the quagent
	and a "data" object at a known location.
	The calculation of the path is done in by 
	an object of type Astar ( see Astar.java).
	This returns a 2D array of the the path in 
	terms of array elements ( x and y). The quagent
	then moves 32 clicks in the direction denoted 
	by each coordinate, then looks at the next coordinate.
	Because of reducing energy and wisdom leading to inconsistent
	motion, the quagent relies on a final data seeking method to pickup the data.
	
	 
	  *****NOTE ****: 
	 The viewer character should move out of the way
	 of the quagent spawn position , for this to work 
	 smoothly.
	 ****NOTE******
					
	*/
	//The starting position
	private double currentx=7;
	private double currenty=2;
	//Incoming coordinates
	private int x=0;
	private int y =0;
	//Iterator to move through the coordinates 
	private int i=1;
	//The grid size, one element of motion
	private int distance =32;
	//The path
	private int[][] path ;
    
	public static void main(String[] args) throws Exception {
		new PathWalker();
    }

    PathWalker () throws Exception {
		// run the constructer of the super class
		super();
		this.stand();
		//Call an object of type Astar
		Astar astar = new Astar();
		//This method calculates the shortest path and returns the path
		path=astar.astar_algorithm();
		
		try {
			while(i<path[0].length){
				//Get the next coordinate
				x=path[0][i];
				y=path[1][i];
				//Check if this is move forward
				if(x==currentx &&y>currenty){
				  move_up();
				  currentx=x;
				  currenty=y;
				  System.out.print("Moving Forward");
				  }
				//Check if this is a move right
				if(y==currenty&&x>currentx){
					move_right();
					currentx=x;
					currenty=y;
				   System.out.print("Moving Right");
				}
				//Check if this is a move right diagonal
				if(y>currenty && x>currentx){
					right_diagonal();
					currentx=x;
					currenty=y;
					System.out.print(" Diagonal to the right");
					
					
				}
				if(y>currenty&&x<currentx){
				  left_diagonal();
				  System.out.println("Diagonal to the left");
				  currentx=x;
				  currenty=y;
				}
				//Check if this is a move backwards
				if(x==currentx && y<currenty){
					move_back();
					System.out.println("Backwards");
					currentx=x;
					currenty=y;
				}
				//Check if this is a move to the back left 
				if(x<currentx&&y<currenty){
				 back_left_diag();
				 System.out.println("Back and to the left");
				 currentx=x;
				 currenty=y;
				}
				//Sleep to keep things coordinated
				Thread.currentThread().sleep(1000);
				//Handle unexpected stops
				this.rays(1);
				handle_stopped(this.events());
			}
			/*For various factors : misrepresentation of room,
			  decaying wisdom and energy leading to inconsistent motion etc
			  the quagent may end up outside the 60 click radius needed for pick up of data.
			  This final step performs the fine adjustment to the data and picks it up. 
			*/
			while(true){
			this.radius(100000);
			handle_radius(this.events());
			Thread.currentThread().sleep(1000);

			}
		}
		
		catch (QDiedException e) { // the quagent died -- catch that exception
			System.out.println("bot died!");
			this.close();
		}
		catch (Exception e) { // something else went wrong???
			System.out.println("system failure: "+e);
			System.exit(0);
		}
    
	}
     
		/*The methods that move the quagent as if following the grid.
		  This uses the incoming coordinates relationship to the current 
		  coordinate.
		*/
		public void move_right() throws Exception{
			this.turn(-90);
			this.walk(distance);
			Thread.currentThread().sleep(1000);
			this.turn(90);
			i++;
		
		}
		
		public void move_up() throws Exception{
			this.walk(distance);
			Thread.currentThread().sleep(1000);
			i++;
		}
		public void move_back() throws Exception{
			this.turn(180);
			this.walk(distance);
			Thread.currentThread().sleep(1000);
			this.turn(180);
			i++;
		}
		public void back_left_diag() throws Exception{
			this.turn(135);
			this.walk(32);
			Thread.currentThread().sleep(1000);
			this.turn(-135);
			i++;
		}
		public void right_diagonal() throws Exception{
		this.turn(-45);
		this.walk(distance);
		
		Thread.currentThread().sleep(1000);
		this.turn(45);
		i++;
	
		}
		public void left_diagonal()throws Exception{
		 this.turn(45);
		 this.walk(distance);
		Thread.currentThread().sleep(1000);
		 this.turn(-45);
			i++;
		}
		
		public void handle_stopped(Events events)throws Exception{
		/*
		An event handler for the where query.
		This sets the initial location and also the current
		location of the quagent. 
		*/
		for (int ix = 0; ix < events.size(); ix++) {
			String e = events.eventAt(ix);
			if (e.indexOf("STOPPED") >= 0) {
				// TELL STOPPED <number>
				String[] tokens = e.split("\\s+");
				double dist = Double.parseDouble(tokens[2]);
				if (dist<32){
				i--;
				move_back();
				
				}
			}	
		}
    }
	
	public void handle_radius(Events events) throws Exception {
			for (int ix = 0; ix < events.size(); ix++) {
			String e = events.eventAt(ix);
			if (e.indexOf("radius") >= 0){
				String[] tokens = e.split("[()\\s]+");
			        if (tokens.length>=7){
				double x = Double.parseDouble(tokens[6]);
				double y = Double.parseDouble(tokens[7]);
				String type = tokens[5];
				double angle = Math.atan2(Math.abs(y),x);
				angle = Math.toDegrees(angle);
				angle = (-1)*angle;
			    double distance = Math.sqrt(x*x + y*y);
					
					System.out.println("I see Data!!!");
					this.turn((int)angle);
					this.walk((int)distance);
				    
					if (distance <100){
					this.pickup(type);
					}
			}
				  else{
					this.turn(30);
					
				    }
				}
			}
		}
}
	
 
