
public class Node {
	private int Fscore=0;
	private int Gscore=0;
	private double Hscore=0;
	private int xpos;
	private int ypos;
	//The position of the goal
	public final static int GOALX =7;
	public final static int GOALY=9;
	private Node parent;
	
	public Node(int x,int y){
	  xpos=x;
	  ypos=y;
	  Fscore=0;
	  Gscore=0;
	  //Heuristic is straight line distance 
	  Hscore = Math.sqrt((x-GOALX)*(x-GOALX) + (y-GOALY)*(y-GOALY));
	  
	}
	public int get_Fscore(){
		return Fscore;
	}
	public int get_Gscore(){
		return Gscore;
	}
	public void set_Fscore(){
		Fscore = Gscore+(int)Hscore;
	}
	public void set_Gscore(int agscore){
		Gscore = agscore;
	}
	public void setparent(Node aparent){
		parent = aparent;
	}
	public Node getparent(){
		return parent;
	}
	public int getx(){
		return xpos;
	}
	public int gety(){
		return ypos;
	}

}

