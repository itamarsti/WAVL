/**
*
* WAVLTree
*
* An implementation of a WAVL Tree with
* distinct integer keys and info
*
*/

import java.util.Random;           ////////////////////only for tests////////////////

//-------------------------------------------WAVLTree Class Builders-----------------------------------

public class WAVLTree {
	
	WAVLNode root;
	WAVLNode minode;
	WAVLNode maxnode;
	int size = 0;


//------------------------------------WAVLTree Class Methods-----------------------------------

//------------------------------------TreeIsEmpty----------------------------------------------
 /**
  * public boolean empty()
  *
  * returns true if and only if the tree is empty
  *
  */

public boolean empty() {
	  if(this.root == null){
		  return true;
	  }
	  else{
		  return false;
	  }
 }

//------------------------------------SearchTreeKey----------------------------------------------

/**
  * public String search(int k)
  *
  * returns the info of an item with key k if it exists in the tree
  * otherwise, returns null
  */
	public String search(int k){
		WAVLNode node = searchNode(k);
		return node.info;
	}


//------------------------------------InsertTreeNode----------------------------------------------


 /**
  * public int insert(int k, String i)
  *
  * inserts an item with key k and info i to the WAVL tree.
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
  * returns -1 if an item with key k already exists in the tree.
  */
public int insert(int k, String i) {
	   //initialize 
	   WAVLNode cur = this.root; 
	   WAVLNode add = new WAVLNode(k,i);
	   add.rank = 0;
	   if(this.empty()){						//in case the tree is empty (setting root)
		   cur = add;
		   this.root = add;
		   this.minode = add;
		   this.maxnode = add;
		   this.size++;
		   return 0;  
	   }
	   else{
		   if(add.key< this.minode.key){
			   this.minode = add;
		   }
		   else if (add.key>this.maxnode.key){
			   this.maxnode = add;
		   }
		}
	   
	   while(cur!=null){          
			if (cur.key == k){           //if key is in tree
				return -1;
			}
			else if(k<cur.key){
				WAVLNode tmp = cur;
				cur = cur.left;
				if (cur == null){        //place to be added found
					tmp.left = add;
					add.parent = tmp;
					this.size ++;
					return rebalanceInsert(add);         //****need to work on****
				}
				else{
					continue;			//continue to the next loop
				}
			}
			else{
				WAVLNode tmp = cur;
				cur = cur.right;
				if (cur == null){        //place to be added found
					tmp.right = add;
					add.parent = tmp;
					this.size ++;
					return rebalanceInsert(add);     //****need to work on****
				}
				else{
					continue;
				}
			}
		}
	   return 0; //should not reach here
	}


//----------------------------------------------InsertRebalance-------------------------------

	private int rebalanceInsert(WAVLNode node){
		node.rank = 0; //can delete later if new node default rank is 0
		int cnt = 0;
		WAVLNode parent = node.parent;
		int pa_rank = parent.rank;
		if (pa_rank==1){							//if Case B
			return cnt;
			}
		else{										//if Case A: both node.parent kids were null
			parent.rank ++;
			cnt++;
			//if case 1....
			cnt = rebalanceInsertRec(parent, 0); //recursive rebalancing calls (calls helper func)
			//if case 2
		}
		
  return cnt;
	   
} //end of main rebalance func
	
//------------------------------------InsertRebalanceRec----------------------------------------------

	private int rebalanceInsertRec(WAVLNode node, int cnt){
		if (node == this.root){ //end condition : reached root
			return cnt;
		}
		int check = caseInsrt(node); //check which case needs to be handled (calls helper func)
		switch (check){
			case 0:
				return cnt;
			case 1:
				node.parent.rank ++;				//promote parent and continue rebalancing
				cnt++;
				return rebalanceInsertRec(node.parent, cnt);		
			case 2:
				rotate(node,false);
				node.right.rank -- ;
				return cnt + 2;
			case 3:
				rotate(node.right,true);
				rotate(node.parent,false);
				node.rank --;
				node.parent.rank ++ ;
				node.parent.right.rank -- ;
				return cnt + 5;
			case 5: //**********************maybe more ranks should be changed, like in case 3 delete********
				rotate(node,true);
				node.left.rank --;
				return cnt+2;
			case 6:	
				rotate(node.left,false);
				rotate(node.parent,true);
				node.rank -- ;
				node.parent.rank ++ ;
				node.parent.left.rank -- ;
				return cnt + 5; 
		}
		return 0;	
	}//end of recursive rebalance helper func
		
//------------------------------------InsertCasesFunction----------------------------------------------

	private int caseInsrt(WAVLNode node){
		WAVLNode parent = node.parent;
		if (rankLeft(parent)==0 && rankRight(parent)==1){
			return 1;
		}
		else if (rankLeft(parent)==1 && rankRight(parent)==0){ //mirror image of case 1
			return 1; 
		}
		else { // final rebalances
			if (rankRight(parent)==2){
				if (rankRight(node) == 2){
					return 2;
				}
				else if(rankLeft(node) == 2){
					return 3;
				}
			}
			else if (rankLeft(parent)==2){ //mirror images
				if (rankRight(node) == 2){
					return 6; // mirror image of 3
				}
				else if(rankLeft(node) == 2){
					return 5; //mirror image of 2
				}
			}	
		}
		return 0 ; //tree is legal (parent had rank diff of 2 before rebalancing)
	}// end of case checker helper func
	
	private void rotate(WAVLNode node,boolean left){
		//----------------------------------------//
		WAVLNode bTemp = node.right;
		if (left){						
			 bTemp = node.left;
		}
		//----------------------------------------//
		WAVLNode zParent = node.parent;
		WAVLNode grandParent = node.parent.parent;
		//connect x parent to be x right child
		if (left){
			node.left = zParent;
		}
		else{
			node.right = zParent;
		}
		//connect grandparent of x to x
		if(grandParent.left == zParent){  //if z is left child
			grandParent.left = node;
		}
		else {
			grandParent.right = node;
		}
		//connect x to its grandparent
		node.parent = grandParent;
		//connect x new child to x
		zParent.parent = node;
		//connect x child to grandson
		if (left){
			zParent.right = bTemp;
		}
		else{
			zParent.left = bTemp;
		}
		//connect x grandson to child
		bTemp.parent = zParent; 
	}//end of rotate right helper func
	

	

//------------------------------------DeleteTreeNode----------------------------------------------

 /**
  * public int delete(int k)
  *
  * deletes an item with key k from the binary tree, if it is there;
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
  * returns -1 if an item with key k was not found in the tree.
  */
public int delete(int k)
{
	   WAVLNode cur = searchNode(k);		//searching for the right node
	   if (cur ==null){
		   return -1;
	   }
	   return rebalanceDelete(cur);				//time to rebalance tree
}


//------------------------------------DeleteRebalance---------------------------------------

	private int rebalanceDelete(WAVLNode node){
		
		WAVLNode parent = node.parent;
		if (node.rank==0){				//condition for a leaf
			if (rankRight(parent)==1 && rankLeft(parent)==1){	//leaf in 1-1 parent
				removeLeaf(node);
				return 0;
			}
			else {
				if ((isLeft(node)&& parent.right==null)||(!isLeft(node)&&parent.left==null)){  //leaf in 1-2 parent and other child missing
					parent.rank--;
					removeLeaf(node);
					return rebalanceDeleteRec(parent,1);			//calling to rebalance
				}			
				else{
					removeLeaf(node);
					return rebalanceDeleteRec(parent,0);
				}
			}
		}
		else if(node.left != null && node.right != null){                 //binary node 
			
			WAVLNode tmp = findSuc(node);
			node.key = tmp.key;
			node.info = tmp.info;
			WAVLNode tmpParent = tmp.parent;
			removeLeaf(tmp);
			return rebalanceDeleteRec(tmpParent, 0);
		}
		else{										                  //unary case
			WAVLNode child;
			if(node.left==null){
				child = node.right;						//checking the child side
			}
			else{
				child = node.left;
			}
			
			if(rankLeft(parent) == 1 && rankRight(parent) ==1){    //node has 1-1 parent
				replaceUnari(node, child);
				return 0;
			}
			else{
				if((isLeft(node)&& rankLeft(parent)==1) ||(!isLeft(node)&& rankRight(parent)==1)){ 		//node has 1-rank difference of parent
					replaceUnari(node,child);
					return 0;
				}
				else{					//node has 2-rank difference of his parent
					replaceUnari(node, child);
					return rebalanceDeleteRec(parent,0);
					}
				}
			}

		}
					
	
	private int rebalanceDeleteRec(WAVLNode node, int cnt){
		//start, case, deal with it
		WAVLNode parent = node.parent;
		if (node == null){
			return cnt;
		}
		int Case = caseDelete(node);
		switch (Case){
			case 1:
				node.rank--;
				cnt++;
				return rebalanceDeleteRec(parent,cnt);
			case 2: //check which side child is on
				node.rank--;
				if (rankRight(node)==1){
					node.right.rank--;
				}
				else{
					node.left.rank--;
				}
				cnt+= 2;
				return rebalanceDeleteRec(parent, cnt);
			case 3:
				rotate(node.right,true); //rotate left
				node.rank--;
				node.right.rank--;
				cnt += 2;
				if(rankLeft(node) == 2 & rankRight(node) == 2){ //node is a 2-2 leaf
					node.rank--;
					cnt++;
				}
				return cnt;
			
			case 4:			//*****to ask to make sure demote -2 is 2 actions
				WAVLNode temp = node.right.left;	//keeping pointer to the demand node
				rotate(temp,false);		//rotate right
				rotate(temp,true);		//rotate left
				node.rank -= 2;
				temp.rank++;
				temp.right.rank--;
				cnt+= 6;
				return rebalanceDeleteRec(temp.parent, cnt);
				
			case 6:							//symertric case of 3
				rotate(node.left,false);		//rotate right
				node.rank--;
				node.left.rank--;
				cnt += 2;
				if(rankLeft(node) == 2 & rankRight(node) == 2){ //node is a 2-2 leaf
					node.rank--;
					cnt++;
				}
				return cnt;
			
			case 8:			//symertric of 4
				//*****to ask to make sure demote -2 is 2 actions
				WAVLNode temp8 = node.right.left;	//keeping pointer to the demand node
				rotate(temp8,true);		//rotate left
				rotate(temp8,false);		//rotate right
				node.rank -= 2;
				temp8.rank++;
				temp8.left.rank--;
				cnt+= 6;
				return rebalanceDeleteRec(temp8.parent, cnt);			
		}
		return cnt;		//other cases
	}
	
	private int caseDelete(WAVLNode node){
		if((rankLeft(node) == 3 ) ||  rankRight(node) == 3){
			if (rankRight(node) == 2 || rankLeft(node) == 2){
			return 1 ;
			}
			else{
				WAVLNode child;
				if (rankLeft(node) == 3){
					child = node.right;   //assert child!=null
				}
				else{
					child = node.left; //assert child!=null
				}
				if(rankLeft(child) == 2 && rankRight(child) == 2){
					return 2;
				}
				else if(!isLeft(child) && rankRight(child) == 2){
					return 4;
				}
				else if(isLeft(child) && rankLeft(child) ==2 ){ //symetric case of 4
					return 8;
				}
				else if(!isLeft(child) && rankRight(child) == 1){
					return 3;
				}
				else if(isLeft(child) && rankLeft(child) ==1){ //symetric of 3
					return 6;
				}
				
			}
		}
		return 0;		
	}
 


//------------------------------------GeneralMethods----------------------------------------------
//------------------------------------RankRight----------------------------------------------


	private int rankRight(WAVLNode node){ //input is parent node (no leaves)
		if (node.right == null){ //is a "-1" node
			return node.rank+1;
		}
		return Math.abs(node.rank-node.right.rank);
	}//end of right rank checker helper func

//------------------------------------RankLeft----------------------------------------------

	private int rankLeft(WAVLNode node){ //input is parent node (no leaves)
		if (node.left == null){ //is a "-1" node
			return node.rank+1; 
		}
		return Math.abs(node.rank-node.left.rank);
	}// end of left rank checker helper func

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~whatKindofSon~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\

	private boolean isLeft(WAVLNode node){			//Am I the Left child of my parent?
		WAVLNode parent = node.parent;
		if(parent.left==null){
			return false;	
		}
		else if(parent.left==node){
			return true;
		}
		else {
			return false;
		}
	}
	
//------------------------------------DeleteMethods----------------------------------------------\\
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~RemoveLeaf~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\

	private void removeLeaf(WAVLNode node){
		WAVLNode parent = node.parent;
		if(isLeft(node)){
			parent.left=null;	
		}
		else {
			parent.right=null;
		}
		node.parent=null;
		this.size--;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ReplaceUnariNode~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\

	private void replaceUnari(WAVLNode node, WAVLNode next){
		WAVLNode parent = node.parent;
		if(isLeft(node)){
			parent.left = next;
		}
		else{
			parent.right = next;
		}
		next.parent = parent;
		this.size--;	
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FindingSuccesor And Predeseccor~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\

	private WAVLNode findSuc(WAVLNode node){			//only for binari node
		WAVLNode cur = node.right;
		while (cur.left != null){
			cur = cur.left;
		}
		return cur; 
	}
	
	private WAVLNode findPr(WAVLNode node){			//only for binari node
		WAVLNode cur = node.left;
		while (cur.right != null){
			cur = cur.right;
		}
		return cur; 
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~NodeSearch~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\


	public WAVLNode searchNode(int k){
		WAVLNode cur = this.root;
		while(cur !=null){
			int key = cur.key;
			if (key==k){
				return cur;
			}
			else if(key > k){
				cur = cur.left;
			}
			else{
				cur = cur.right;
			}
		}
		//if tree is empty or key not found
		return null;
	}



//------------------------------------RestoftheCode----------------------------------------------

//------------------------------------MinimumValue----------------------------------------------

/**
 * public String min()
 *
 * Returns the i�fo of the item with the smallest key in the tree,
 * or null if the tree is empty
 */

public String min()
{
	return this.minode.info;
}

//------------------------------------MaximumValue----------------------------------------------


  /**
   * public String max()
   *
   * Returns the info of the item with the largest key in the tree,
   * or null if the tree is empty
   */
public String max()
{
	 return this.maxnode.info;
}


//------------------------------------KeysToArray----------------------------------------------

 /**
  * public int[] keysToArray()
  *
  * Returns a sorted array which contains all keys in the tree,
  * or an empty array if the tree is empty.
  */
 public int[] keysToArray()
 {
       int[] arr = new int[this.size];
       String final_str = "";
       final_str = this.root.keyToString(this.root, final_str);
       String[] str_arr = final_str.split(" ");
       for (int i=0; i<arr.length;i++){
       	arr[i] = Integer.parseInt(str_arr[i]);
       }
       return arr;             
 }

//------------------------------------InfoToArray----------------------------------------------

 /**
  * public String[] infoToArray()
  *
  * Returns an array which contains all info in the tree,
  * sorted by their respective keys,
  * or an empty array if the tree is empty.

  */
 public String[] infoToArray(){
       String[] arr = new String[this.size];
       String final_str = "";
       final_str = this.root.infoToString(this.root, final_str);
       return final_str.split(" ");                    
 }



/**
   * public int size()
   *
   * Returns the number of nodes in the tree.
   *
   * precondition: none
   * postcondition: none
   */
 	
   public int size(){
   	return this.size;
  }

//----------------------------------------------------------------------------------------
//------------------------------WAVLNode Class Builders-----------------------------------

  /**
  * public class WAVLNode
  *
  * If you wish to implement classes other than WAVLTree
  * (for example WAVLNode), do it in this file, not in 
  * another file.
  * This is an example which can be deleted if no such classes are necessary.
  */
 public class WAVLNode{
	  private int key;
	  String info;
	  int rank = 0;
	  WAVLNode right;
	  WAVLNode left;
	  WAVLNode parent;
	  
//------------------------------------WAVLNode Class Methods-----------------------------------

//------------------------------------CreateNewNode----------------------------------------------
	  
	  public WAVLNode(int key, String info) {
		this.key = key;
		this.info = info;
		
	}

//------------------------------------StringOfSortedKeys----------------------------------------------
	  
	  public String infoToString(WAVLNode node, String str){
		  if (node.equals(null)){
			  return str;			  
		  }
		  if (node.rank==0){
			  str += node.info + " ";
			  return str;
		  }
		  else{
			  return infoToString(node.left, str)+node.info+ " "+infoToString(node.right, str);
		  }	  
	  }

//------------------------------------StringOfSortedValues----------------------------------------------

	  public String keyToString(WAVLNode node, String str){
		  if (node.equals(null)){
			  return str;			  
		  }
		  if (node.rank==0){
			  str += node.key + " ";
			  return str;
		  }
		  else{
			  return infoToString(node.left, str)+node.key + " "+infoToString(node.right, str);
		  }	  
	  }
 }
 
 
//------------------------------------MainFunction----------------------------------------------

  public static void main (String[] args){
	  /**
	  WAVLTree tree = new WAVLTree();
	  WAVLNode yosi =tree.new WAVLNode(1, "2"); 
	  yosi.rank = -1;
	  System.out.println(yosi.key);
	  System.out.println(yosi.info);
	  System.out.println(yosi.rank);
	  System.out.println(tree.root);
	  
	  
	  WAVLTree b = new WAVLTree();
		Random rand = new Random();
		//String s="";
		for (int i=0;i<5000;i++)
		{
			//System.out.print(i);	
			b = new WAVLTree();
			//s="";
			int k=1+rand.nextInt(99999);
			for(int j=0;j<k;j++){
				int n=rand.nextInt(Integer.MAX_VALUE);
				//s+=(Integer.toString(n)+" ");
				b.insert(n, "");
			}
			if(b.isWAVL(b.root)){
				System.out.println("tree number: "+i+" is WAVL"+"has "+k+" nodes");
				//System.out.println(s);
				}
			else{
				System.out.println("tree number: "+i+" is NOT WAVL"+"has "+k+" nodes");
				return;
				
			}
		}
		System.out.println("yeah");		
		}
/**  
  public boolean isWAVL(WAVLNode node){
	  if(node == null){
		  return true;
	  }
	  if((RankLeft(node) == 1 && RankRight(node) ==2)  || (RankLeft(node) == 2 && RankRight(node) ==1) || 
			  (RankLeft(node) == 1 && RankRight(node) ==1) || (RankLeft(node) == 2 && RankRight(node) ==2)){
		  return (isWAVL(node.left) && isWAVL(node.right));
	  }
	  return false;
  }
**/
}}



