import java.util.*;
import java.util.LinkedList;


public class Astar {
/*This class calculates the shortest path from the quagent start position
  and a data object using A*. The room is represented as an array 
  with 1's denoting walls. The open and closed lists are linked lists
  of custom nodes. The nodes have getter/setters for g,f,and h,scores 
  plus their coordinates and parent. The permissible heuristic used 
  is straight-line distance from the data, and this is calculated in the 
  constructor of the Node. 
*/
	
	private boolean found = false;
	//The room as 32*32 grid tiles 
	private int [] [] grid = new int [16][16];
	//Open and closed lists
	private LinkedList<Node> open = new LinkedList<Node>();
	private LinkedList<Node> closed = new LinkedList<Node>(); 
	
	public Astar(){
		grid = makegrid();
		System.out.println("MAP OF ROOM, 1's are walls");
		print_grid();
	}
	
	public int[][] makegrid(){
	/*Method to construct the grid
	 using 1's to denote walls.
	*/
	int [] []agrid = new int[16][16];
	// The vertical block
	for(int i=4;i<12;i++){
		agrid[i][4]=1;
	}
	// The Top horizontal wall
	for(int i=4;i<14;i++){
		agrid[4][i]=1;
	}
	//The bottom horizontal wall
	for(int i=6;i<14;i++){
		agrid[8][i]=1;
	}
	agrid[7][8]=88;
	return agrid;
	}
	
	public void print_grid(){
	//Prints the grid for visualization
		String row ="";
		for (int i =0;i<16;i++){
			for (int j=0;j<16;j++){
				row+=" "+grid[i][j]+"";
			}
			System.out.println(row);
			row="";
		}
	}
	
	public int[][] astar_algorithm(){
		//The starting position
		Node start_node = new Node(7,2);
		start_node.set_Gscore(0);
		start_node.set_Fscore();
		start_node.setparent(null);
		open.add(start_node);
		Node frontiernode=null;

		while(found == false){
		//Test the node with the lowest Fscore 
		int winner_index = find_winner(open);
		frontiernode = open.get(winner_index);
		open.remove(winner_index);
		closed.add(frontiernode);
		expand_frontier(frontiernode);
		//Stop when the target is found, or the openlist is empty
		if (frontiernode.getx()==7 && frontiernode.gety()==9)
			found=true;
		if(open.size()<=1 && closed.size()>1)
			found=true;
		}
		//Get the path
		int[][] path=get_path(frontiernode);
		
		//Denote the path as 8's on the grid
		for (int i=0;i<path[0].length;i++){
			int x=path[0][i];
			int y=path[1][i];
			grid[x][y]=8;
		}
		grid[6][8]=88;
		System.out.println("Shortest Path denoted by 9's goal is 88");
		print_grid();
		return path;
	}
	
	public int[][] get_path(Node frontiernode){
		/*This method starts with the final node added to the path
		and reconstructs the path by following the parent Nodes back
		to the initial node. 
		*/
		int [] pathx=new int [1000000];
		int [] pathy=new int [1000000];
		Node parent=frontiernode.getparent();
		int firstx=frontiernode.getx();
		int firsty=frontiernode.gety();
		pathx[0]=firstx;
		pathy[0]=firsty;
		int i = 1;
		
		while(parent!=null){
			int pos = closed.indexOf(parent);
			Node temp=closed.get(pos);
			pathx[i]=temp.getx();
			pathy[i]=temp.gety();
			parent=temp.getparent();
			i++;
		}
		int[] newpathx=new int [i];
		int[] newpathy=new int[i];
		for(int j =0 ;j<i;j++){
			newpathx[j]=pathx[j];
			newpathy[j]=pathy[j];
		}
		pathx=newpathx;
		pathy=newpathy;
		pathx=reverse_array(pathx);
		pathy=reverse_array(pathy);
		int[][] path = { pathx,pathy };
		return path;
	}
	public int[] reverse_array(int[] anarray){
		for(int i = 0; i < anarray.length/2; i++){
		    int temp = anarray[i];
		    anarray[i] = anarray[anarray.length - i - 1];
		    anarray[anarray.length - i - 1] = temp;
		}
		return anarray;
	}
	
	public int find_winner(LinkedList<Node> list){
	//This method finds the winning node in a list
		int minpos=0;
		for( int i=0;i<list.size();i++){
		  Node temp = list.get(i);
		  int Fscore=temp.get_Fscore();
		  if( Fscore<minpos)
			  minpos=i;
		  }
		return minpos;
	}
	
	public void expand_frontier(Node currentnode){
		/*This method takes each of the 8 neighbouring 
		grid elements adjacent to the winning node, and
		deals with them. Either adding to the open list, or 
		resetting the parent if the score to the node is lower
		than the score to the same node already in the open list. 
		It also checks to make sure the nodes are not in the closed list
		or un walkable.
		*/
		int nodex = currentnode.getx();
		int nodey = currentnode.gety();
		int neighborx=0;
		int neighbory=0;
		int Fscore=currentnode.get_Fscore();
		
		neighborx=nodex;
		neighbory=nodey+1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex;
		neighbory=nodey-1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex+1;
		neighbory=nodey;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex-1;
		neighbory=nodey+1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex+1;
		neighbory=nodey+1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex+1;
		neighbory=nodey-1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex-1;
		neighbory=nodey+1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
		
		neighborx=nodex-1;
		neighbory=nodey-1;
		neighbor_handler(neighborx,neighbory,currentnode,Fscore);
	}
	
	public void neighbor_handler(int aneighborx,int aneighbory,Node aparent_pos,int anFscore){
	 /*This method checks the possible cases :
		-In the open list
		-In the closed 
		-In the open but a better path exists now
	 */
		boolean check_node1 = check_next(aneighborx,aneighbory);
		if(check_node1==true){
			boolean in_open = check_open(aneighborx,aneighbory);
			if(in_open==false){
				put_in_open(aneighborx,aneighbory,aparent_pos,anFscore);
			}
			if(in_open==true){
				adjust_parent(aneighborx,aneighbory,aparent_pos,anFscore);
			}
		}
		 
	}
	
	public boolean check_next(int x,int y){
	/*This checks whether a node is in the closed list, already in the 
	 open or is a wall and so unwalkable.
	*/
		boolean viable = true;
		if (x<0||x>=15)
			viable =false;
		if(y<0||y>=15)
			viable =false;
		
		for (int i=0;i<closed.size();i++){
			Node current = closed.get(i);
			int checkx=current.getx();
			int checky=current.gety();
			if(x==checkx && y==checky)
				viable = false;
		}
		if(viable==true){
		if (grid[x][y]==1)
			viable = false;
		}
		return viable;
	}
	public boolean check_open(int x,int y){
	/*Checking whether a given node is already
	  in the open list. 
	*/
		boolean not_in_open = false;
		for (int i =0;i<open.size();i++){
			Node current = open.get(i);
			int cx=current.getx();
			int cy=current.gety();
			if(cx==x && cy==y){
				not_in_open = true;
			}
		}
		return not_in_open;

	}
	public void put_in_open(int xpos,int ypos,Node parent_pos,int parent_fscore){
	    //This adds a node to the open list
		int nodex=xpos;
		int nodey=ypos;
		int nodeg=parent_fscore;
		Node parent = parent_pos;
		
		Node insert = new Node(nodex,nodey);
		insert.set_Gscore(nodeg+1);
		insert.setparent(parent);
		insert.set_Fscore();  
		open.add(insert);
		
		}
	public void adjust_parent(int xpos,int ypos,Node parent_pos,int parent_fscore){
		//This method adjusts a path to a node already in the open list 
		//If the new path to that node is better 
		Node check_node;
		int check_node_pos=0;
		for(int i=0; i<open.size();i++){
			Node current = open.get(i);
			int cx=current.getx();
			int cy=current.gety();
			if (cx==xpos &&cy ==ypos){
				check_node=current;
				check_node_pos=i;
				int gscore_comp=check_node.get_Gscore();
				  if (gscore_comp<(parent_fscore+1)){
					  open.remove(check_node_pos);
					  put_in_open(xpos,ypos,parent_pos,parent_fscore);
				  }
			}
		}
	}
}
