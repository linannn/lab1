package pkg;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.Scanner;
class Graph{
	//图类
    private Vector<String> nodes = new Vector<String>();
    private Vector<String> source = new Vector<String>();
    private boolean isGraph;
    public int matrix[][], bridge[][][];//bridge：桥接词
    private boolean vis[];
    Graph() {
        this.nodes = new Vector<String>();
        this.source = new Vector<String>();
    }
    Graph createDirectedGraph(String fileName) {//新建图
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
                        && temp != '"' && temp != '-' && temp != '(' && temp != ')' && temp != '!'
                        && temp != '?' && temp != ' ' && temp != '\n')
                    continue;
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

        }//在生成图的同时也将桥接词同时查询并存储在相应的数组中

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
    int findNode(String str) {//查找相应节点
        str.toLowerCase();
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
public class lab1{
    public static String fPath;
    public static Integer id = 0;
	public static void main ( String[] args) {
		System.out.println("Please input the order as follow:");
		System.out.println("Escape : 0");
		System.out.println("Read text: 1 path");
		System.out.println("Show direct Graph: 2");
		System.out.println("Check bridge word: 3 word1 word2");
		System.out.println("Create new text with bridge word: 4 string");
		System.out.println("Calculate the shortest path in two words: 5 word1 word2");
		System.out.println("Random walk: 6");
		String order, fileName, word1, word2, inputText;
		Scanner scanner = new Scanner(System.in);
		Graph G  = new Graph();
		id=0;
		outer:
		while(true){
			order = scanner.next();
			switch(order){
				case "0":
					break outer;
				case "1":
					fileName = scanner.next();
					G = G.createDirectedGraph(fileName);
					if(!G.isGraph())break ;
					System.out.println("Please input the fold that you want to save picture!");
					fPath = scanner.next();
					while(!checkRightPath(fPath)){
					    System.out.println("The path is invalid, please input again after checked!");
					    fPath = scanner.next();
                    }
                    scanner.nextLine();
					break;
				case "2":
				    if(!G.isGraph()){
				        System.out.println("There is not a Graph, please input the path " +
                                "of the text to create the Graph with order \"1 path\"");
				        break;
                    }
					showDirectGraph(G);
                    scanner.nextLine();
					break;
				case "3":
					word1 = scanner.next();
					word2 = scanner.next();
                    if(!G.isGraph()){
                        System.out.println("There is not a Graph, please input the path " +
                                "of the text to create the Graph with order \"1 path\"");
                        scanner.nextLine();
                        break;
                    }
					System.out.println(queryBridgeWords(G, word1, word2));
                    scanner.nextLine();
					break;
				case "4":
					inputText = scanner.next();
					inputText += scanner.nextLine();
                    if(!G.isGraph()){
                        System.out.println("There is not a Graph, please input the path" +
                                " of the text to create the Graph with order \"1 path\"");
                        break;
                    }
					System.out.println(generateNewTest(G, inputText));
					break;
				case "5":
					word1 = scanner.next();
					word2 = scanner.next();
                    if(!G.isGraph()){
                        System.out.println("There is not a Graph, please input the path" +
                                " of the text to create the Graph with order \"1 path\"");
                        scanner.nextLine();
                        break;
                    }
					String res = calcShortestPath(G, word1, word2);
					if(res.equals("")){
						System.out.println("There is not a path from \""+word1+
                                "\" to \""+word2+"\"");
					}
					else {
						System.out.println(res);
						if(res.charAt(0) == 'N'){
						    break ;
                        }
						int ans = 0;
						for(int i = 0; i < res.length(); i ++){
							if(res.charAt(i) == '-')ans++;
						}
						System.out.println("The length of the shortest path from \""
                                +word1+"\" to \""+word2+"\" is "+ans);
					}
                    scanner.nextLine();
					break;
				case "6":
                    if(!G.isGraph()){
                        System.out.println("There is not a Graph, please input the path" +
                                " of the text to create the Graph with order \"1 path\"");
                        scanner.nextLine();
                        break;
                    }
                    System.out.println("Press \"1\" to continue walk, and \"2\" to stop and " +
                            "print!");
					System.out.println(randomWalk(G));
                    scanner.nextLine();
					break;
				default:
					System.out.println("The order you input is invalid, and input order again.");
					scanner.nextLine();
					break;
			}
		}

	}
	private static void showDirectGraph(Graph gra) {
		String pathName = writeScript(gra, "graph");
		if(pathName.equals("error!")){
		    System.out.println("writeScript error!!!");
        }
		try{
			Thread.sleep(300);
		}catch (InterruptedException e) {
			System.out.println("InterruptedException!");
		}
		drawPicture(pathName);
	}
	private static String queryBridgeWords(Graph gra, String word1, String word2) {
		word1=word1.toLowerCase();
		word2=word2.toLowerCase();
		int index1 = gra.findNode(word1);
		int index2 = gra.findNode(word2);
		if (index1 == -1 && index2 == -1) {
		    return "No \"" + word1 + "\" and \"" + word2 + "\" in the Graph!";
        }
		if (index1 == -1) {
			return "No \"" + word1 + "\" in the Graph!";
		}
		if (index2 == -1) {
            return "No \"" + word2 + "\" in the Graph!";
        }
		int bw = gra.bridge[index1][index2][0];
		if (bw == 0)
			return "No bridge words from \""+word1+"\" to \""+word2+"\"!";
		else 
			if (bw == 1) 
				return "The bridge word from \""+word1 + "\" to \""+word2+"\" is: "
                        + gra.getNode(gra.bridge[index1][index2][1]);
			else {
				StringBuilder temp = new StringBuilder();
				for (int i = 1; i<bw; i++)
					temp.append(gra.getNode(gra.bridge[index1][index2][i])+", ");
				temp.append("and "+gra.getNode(gra.bridge[index1][index2][bw]) + ".");
				return "The bridge word from \""+word1 + "\" to \""+word2+"\" are: "+temp;
			}
	}
	private static String generateNewTest(Graph gra, String inputText) {
	    inputText.toLowerCase();
		String temp[] = inputText.split(" ");
		Vector<String> text = new Vector<String>();
		text.addElement(temp[0]);
		int  index1 = gra.findNode(temp[0]) , index2;
		for (int i = 1; i<temp.length;i++) {
			index2 = gra.findNode(temp[i]);
			if(index1 != -1 && index2 != -1)
				if (gra.bridge[index1][index2][0]>0)
					text.addElement(gra.getNode(gra.bridge[index1][index2][1]));
			text.addElement(temp[i]);
			index1=index2;
		}
		StringBuilder text2 = new StringBuilder();
		for (int i = 0; i<text.size()-1;i++) 
			text2.append(text.elementAt(i) + " ");
		text2.append(text.elementAt(text.size()-1));
		return text2.toString();
	}
	private static String calcShortestPath(Graph gra, String word1, String word2) {
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();
		int index1 = gra.findNode(word1), index2 = gra.findNode(word2);
        if (index1 == -1 && index2 == -1) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the Graph!";
        }
        if (index1 == -1) {
            return "No \"" + word1 + "\" in the Graph!";
        }
        if (index2 == -1) {
            return "No \"" + word2 + "\" in the Graph!";
        }
		final int Z = 1000;
		int Graph[][] = new int [Z][Z];
		int t, min = 0, numb = gra.matrix.length;
		int sign[] = new int[numb], distance[] = new int[numb], pre[] = new int [numb];
		for(int i = 0; i < numb; i++) //Dij算法
			for(int j = 0; j<numb;j++) 
				Graph[i][j] = (gra.matrix[i][j] == 0)? Z:gra.matrix[i][j];
		for (int i = 0; i < numb; i++) {
			sign[i] = 0;
			distance[i] = (i==index1 ? 0 : Z);
		}
		for (int i = 0; i < numb; i++){
			t = Z;
			for (int j = 0; j < numb; j++){
				if ((sign[j] == 0) && (distance[j] <= t)){
					t = distance[j];
					min = j;
				}
			}
			sign[min] = 1;
			for (int j = 0; j < numb; j++){
                    if (distance[j] > distance [min] + Graph[min][j]){
                            distance[j] = distance[min] + Graph[min][j];
                            pre[j] = min;
                    }
            }
		}
		if(distance[index2]==Z)return "";
		String pathName = writeScript(gra, pre, index1, index2);
		try{
		    Thread.sleep(500);
		}catch (InterruptedException e) {
		    System.out.println(e);
		}
		drawPicture(pathName);
		return getPath(pre, gra, index2, index1, new String());
	}
    private static String randomWalk(Graph gra) {
        int numb = gra.matrix.length;
        boolean isWalked[][] = new boolean [numb][numb];
        Random rand = new Random();
        int start = (int)(rand.nextInt(numb)), nextWalk;
        StringBuilder walkText = new StringBuilder();
        Vector<Integer> temp = new Vector<Integer>();
        walkText.append(gra.getNode(start)+" ");
        do {
            int judge;
            Scanner scanner = new Scanner(System.in);
            judge = scanner.nextInt();
            if(judge==2)break;
            for (int i = 0; i<numb;i++)
                if (gra.matrix[start][i] != 0)
                    temp.add(i);
            if (temp.size()==0)
                break;
            nextWalk = temp.elementAt((int)rand.nextInt(temp.size()));
            walkText.append(gra.getNode(nextWalk)+" ");
            if (isWalked[start][nextWalk])
                break;
            isWalked[start][nextWalk] = true;
            start = nextWalk;
            temp.clear();
        }while(true);
        return walkText.toString();
    }
	private static String getPath(int pre[], Graph gra, int start, int end, String input) {
		if (start != end) 
			return getPath(pre, gra, pre[start], end, input)+"->"+gra.getNode(start);
		return gra.getNode(end);
	}
    private static boolean checkRightPath(String fp){
        File f = new File(fp);
        return f.isDirectory();
    }
    private static String writeScript(Graph gra, String name){//根据图的信息生成脚本并调用cmd生成图片，返回图片路径

        File script = new File(fPath+"\\",name + ".txt");
        FileOutputStream scriptStream = null;
        try {
            if (script.exists())
                script.delete();
            script.createNewFile();
            scriptStream = new FileOutputStream(script);
            scriptStream.write("diGraph{\n".getBytes());
            for (int i = 0;i<gra.matrix.length; i++) {
                for (int j = 0; j< gra.matrix[i].length;j++) {
                    if (gra.matrix[i][j] != 0) {
                        scriptStream.write((gra.getNode(i)+"->"+gra.getNode(j)).getBytes());
                        scriptStream.write(("[label="+gra.matrix[i][j]+"]").getBytes());
                        scriptStream.write("\n".getBytes());
                    }
                }
            }
            scriptStream.write("}".getBytes());
            scriptStream.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec("cmd /c dot " + fPath + "\\" + name + ".txt -T jpg -o " + fPath + "\\" + name +".jpg");
        }catch(IOException e) {
            return "error!";
        }
        return fPath+"\\"+name+".jpg";
    }
    private static String writeScript(Graph gra, int pre[], int start, int end){//重载，包含最短路径的写脚本函数
        String name = "pic" + id.toString();
        id++;
        File script = new File(fPath, name+".txt");
        FileOutputStream scriptStream = null;
        int isColored[] = new int [pre.length];
        for (int i =0; i<pre.length;i++)
            isColored[i] = -1;
        while(start != end){
            isColored[end] = pre[end];
            end = pre[end];
        }
        try {
            if (script.exists())
                script.delete();
            script.createNewFile();
            scriptStream = new FileOutputStream(script);
            scriptStream.write("diGraph{\n".getBytes());
            for (int i = 0;i<gra.matrix.length; i++) {
                for (int j = 0; j< gra.matrix[i].length;j++) {
                    if (gra.matrix[i][j] != 0) {
                        scriptStream.write((gra.getNode(i)+"->"+gra.getNode(j)).getBytes());
                        scriptStream.write(("[label="+gra.matrix[i][j]+"]").getBytes());
                        if (isColored[j] == i)
                            scriptStream.write("[color=blue]".getBytes());
                        scriptStream.write("\n".getBytes());
                    }
                }
            }
            scriptStream.write("}".getBytes());
            scriptStream.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec("cmd /c dot " + fPath + "\\" + name + ".txt" + " -T jpg -o " + fPath + "\\" + name + ".jpg");
        }catch(IOException e) {
            System.out.println(e);
        }
        return fPath + "\\" + name + ".jpg";
    }
    private static void drawPicture(String pathName){//调用系统的图片查看器显示图片
        try {
            Runtime.getRuntime().exec("rundll32 c:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen "+pathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
