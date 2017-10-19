package src.pkg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Graph {
	private Vector<String> nodes = new Vector<String>();
	  private Vector<String> source = new Vector<String>();
	  private boolean isGraph;
	  
	  public int matrix[][], 
	  bridge[][][];
	  private boolean[] vis;
	  
	  Graph() {
	    this.nodes = new Vector<String>();
	    this.source = new Vector<String>();
	  }
	  
	  Graph createDirectedGraph(String fileName) {
	    try {
	      nodes.clear(); source.clear();
	      FileReader fr = new FileReader(new File(fileName));
	      int temp = 0;
	      StringBuilder tempWord = new StringBuilder();
	      
	      while((temp = fr.read())!= -1) {
	        if (('a'<=temp&&temp<='z')||('A'<=temp&&temp<='Z')){
	          tempWord.append((char)temp);
	        }
	        else if (temp != ',' && temp != '.' && temp != ';' && temp != ':' && temp != '\''
	                        && temp != '"' && temp != '-' && temp != '(' && temp != ')' && temp != '!'){
	          continue;}
	        else {
	          String addWord = tempWord.toString().toLowerCase();
	          if(addWord.length() != 0) {
	                        this.source.addElement(addWord);
	                        if (this.findNode(addWord) == -1)
	                            this.nodes.addElement(addWord);
	                    }
	                    tempWord.delete(0, tempWord.length());
	                }
	            }
	            fr.close();
	        } catch(IOException e) {
	            System.out.println("The path you input is wrong, please input again after checking");
	            isGraph = false;
	            return this;
	        }
	        int numb = this.nodes.size();
	        matrix = new int [numb][numb];
	        int preIndex=this.findNode(this.source.elementAt(0)), newIndex;
	        for(int i = 1; i < this.source.size();i++) {
	            newIndex = this.findNode(this.source.elementAt(i));
	            matrix[preIndex][newIndex]++;
	            preIndex = newIndex;
	        }
	        bridge = new int [numb][numb][numb];
	        for(int i = 0; i < numb; i ++){
	            for(int j = 0; j < numb; j ++){
	                for(int k = 0; k < numb; k ++){
	                    if(matrix[i][j]!=0&&matrix[j][k]!=0) {
	                        bridge[i][k][++bridge[i][k][0]]=j;
	                    }
	                }
	            }
	        }
	        isGraph = true;
	        return this;
	    }
	    boolean isGraph(){
	        return  isGraph;
	    }
	    int findNode(String str) {
	    	str = str.toLowerCase();
	        for (int i = 0; i < this.nodes.size(); i++) {
	            if (this.nodes.elementAt(i).equals(str))
	                return i;
	        }
	        return -1;
	    }
	    String getNode(int x) {
	        return this.nodes.elementAt(x);
	    }
}
