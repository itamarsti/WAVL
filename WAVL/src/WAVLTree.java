import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;           ////////////////////only for tests////////////////
import java.util.Set;

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
			cnt++;									//cnt == 1
			cnt = rebalanceInsertRec(parent, cnt); //recursive rebalancing calls (calls helper func)
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
				WAVLNode chuck = node.right; //in presentation chuck = B
				rotate(chuck,true);
				rotate(chuck,false);
				node.rank --;
				chuck.rank ++ ;
				chuck.right.rank -- ;
				return cnt + 5;
			case 5: //**********************maybe more ranks should be changed, like in case 3 delete********
				rotate(node,true);
				node.left.rank --;
				return cnt+2;
			case 6:	
				WAVLNode norris = node.left; //in presentation norris = B
				rotate(norris,false);
				rotate(norris,true);
				node.rank -- ;
				norris.rank ++ ;
				norris.left.rank -- ;
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
			if ((rankRight(parent)==2)&&(rankLeft(parent)==0)){
				if (rankRight(node) == 2){
					return 2;
				}
				else if(rankLeft(node) == 2){
					return 3;
				}
			}
			else if ((rankLeft(parent)==2) && (rankRight(parent)==0)){ //mirror images
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
	
	private void rotate(WAVLNode node,boolean left){ //rotate right unless left == true
		WAVLNode bTemp = node.right;
		if (left){						
			 bTemp = node.left;
		}
		WAVLNode zParent = node.parent;
		WAVLNode grandParent = node.parent.parent;
		//connect node parent to be node right child
		if (left){
			node.left = zParent;
		}
		else{
			node.right = zParent;
		}
		if (grandParent != null){
			//connect grandparent of node to node
			if(grandParent.left == zParent){  //if node.parent is left child
				grandParent.left = node;
			}
			else {
				grandParent.right = node;
			}
			//connect node to its grandparent
			node.parent = grandParent;
		}
		else{
			this.root = node;
			node.parent = null;
		}
		//connect node new child to node
		zParent.parent = node;
		//connect node child to grandson
		if (left){
			zParent.right = bTemp;
		}
		else{
			zParent.left = bTemp;
		}
		if (bTemp!=null){
			//connect node grandson to child
			bTemp.parent = zParent; 
		}
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
	   
	   if(cur==this.minode){			//*******considering swaping or insert to rebalance
		  if(cur.right == null){
			  this.minode = cur.parent;
		  }
		  else{
			  this.minode = findSuc(cur);
		  }
	   }
	   if(cur==this.maxnode){
		   if(cur.left == null){
				  this.maxnode = cur.parent;
			  }
			  else{
				  this.maxnode = findPre(cur);
			  }
	   }
	   return rebalanceDelete(cur);				//time to rebalance tree
}


//------------------------------------DeleteRebalance---------------------------------------

	private int rebalanceDelete(WAVLNode node){
		
		int cnt = 0;
		if (node==this.root){	
			if (node.right==null && node.left==null){				//only root in tree
				this.maxnode = null;					
				this.minode = null;
				this.root = null;
				this.size--;
				return cnt;
			}
			else if ((node.right!=null && node.left==null) || (node.left!=null&& node.right==null)){
				WAVLNode child;
				if(node.left==null){
					child = node.right;						//checking the child side
				}
				else{
					child = node.left;
				}
				this.root = child;                         //delete unary root
				this.minode = child;
				this.maxnode = child;
				child.parent = null;
				this.size--;
				return cnt;
			}
			
			
		}
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
					cnt++;
					return rebalanceDeleteRec(parent.parent ,cnt);			//calling to rebalance
				}			
				else{
					removeLeaf(node);
					return rebalanceDeleteRec(parent,cnt);
				}
			}
		}
		else if(node.left != null && node.right != null){                 //binary node 
																		//to check root case
			WAVLNode tmp = findSuc(node);
			node.key = tmp.key;
			node.info = tmp.info;
			if (this.maxnode == tmp){
				this.maxnode = node;
			}
			return rebalanceDelete(tmp);
			
		}
		else if ((node.right!=null&& node.left==null) || (node.left!=null&& node.right==null)) {										                  //unary case
			WAVLNode child;
			if(node.left==null){
				child = node.right;						//checking the child side
			}
			else{
				child = node.left;
			}
			if (node == this.root && node.rank==1){			//delete unary root
				this.root = child;
				this.minode = child;
				this.maxnode = child;
				child.parent = null;
				this.size--;
				return cnt;
			}
			
			if(rankLeft(parent) == 1 && rankRight(parent) ==1){    //node has 1-1 parent
				replaceUnari(node, child);
				return cnt;
			}
			else{
				if((isLeft(node)&& rankLeft(parent)==1) ||(!isLeft(node)&& rankRight(parent)==1)){ 		//node has 1-rank difference of parent
					replaceUnari(node,child);
					return cnt;
				}
				else{					//node has 2-rank difference of his parent
					replaceUnari(node, child);
					return rebalanceDeleteRec(parent,cnt);
					}
				}
			}
		else{
			return 0; //*******if error return go back here
		}
	}
					
	
	private int rebalanceDeleteRec(WAVLNode node, int cnt){
		if (node == null){ //halting condition
			return cnt;
		}
		WAVLNode parent = node.parent;
		int Case = caseDelete(node);
		switch (Case){
			case 1:
				node.rank--;
				cnt++;
				return rebalanceDeleteRec(parent,cnt);
			case 2: //check which side child is on
				if (rankRight(node)==1){
					node.right.rank--;
				}
				else{
					node.left.rank--;
				}
				node.rank--;
				cnt+= 2;
				return rebalanceDeleteRec(parent, cnt);
			case 3:
				node.rank--;
				node.right.rank++; 		 //changing rank before rotate because node.right is different after rotate
				rotate(node.right,true); //rotate left
				cnt += 2;
				if((rankLeft(node) == 2 && rankRight(node) == 2)&& (node.left==null && node.right==null)){ //node is a 2-2 leaf
					node.rank--;
					cnt++;
				}
				return cnt;
			
			case 4:			//*****to ask to make sure demote -2 is 2 actions
				WAVLNode temp = node.right.left;	//keeping pointer to the demand node
				rotate(temp,false);		//rotate right
				rotate(temp,true);		//rotate left
				node.rank -= 2;
				temp.rank += 2;
				temp.right.rank--;  //should be after rotate
				cnt+= 7;
				return cnt;
				
			case 13:							//symertric case of 3
				node.rank--;
				node.left.rank++;			//changing rank before rotate because node.right is different after rotate
				rotate(node.left,false);		//rotate right
				cnt += 2;
				if((rankLeft(node) == 2 && rankRight(node) == 2)&& (node.left==null && node.right==null)){ //node is a 2-2 leaf
					node.rank--;
					cnt++;
				}
				return cnt;
			
			case 14:			//symertric of 4
				//*****to ask to make sure demote -2 is 2 actions
				WAVLNode temp14 = node.left.right;	//keeping pointer to the demand node
				rotate(temp14,true);		//rotate left
				rotate(temp14,false);		//rotate right
				node.rank -= 2;
				temp14.rank += 2;
				temp14.left.rank--; //should be after rotate
				cnt+= 7;
				return cnt;			
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
				else if(!isLeft(child) && rankRight(child) == 1){
					return 3;
				}
				else if(isLeft(child) && rankLeft(child) ==1){ //symetric of 3
					return 13;
				}
				else if(!isLeft(child) && rankRight(child) == 2){
					return 4;
				}
				else if(isLeft(child) && rankLeft(child) ==2 ){ //symetric case of 4
					return 14;
				}	
			}
		}
		return 0;		//no problems found
	}
 


//------------------------------------GeneralMethods----------------------------------------------
//------------------------------------RankRight----------------------------------------------
	/**
	 * must not be static mathod!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * @param node
	 * @return
	 */

	private static int rankRight(WAVLNode node){ //input is parent node (no leaves)
		if (node.right == null){ //is a "-1" node
			return node.rank+1;
		}
		return Math.abs(node.rank-node.right.rank);
	}//end of right rank checker helper func

//------------------------------------RankLeft----------------------------------------------

	private static int rankLeft(WAVLNode node){ //input is parent node (no leaves)
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
	
	private WAVLNode findPre(WAVLNode node){			//only for binari node
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
 * Returns the iîfo of the item with the smallest key in the tree,
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
 public int[] keysToArray(){
	 int[] arr = new int[this.size];
	 int index;
     index = keysArrayRec(this.root, arr,0);
     return arr;             
 }
 
//------------------------------------KeysToArrayRec------------------------------------------//
 

public int keysArrayRec(WAVLNode node, int[] arr_rec, int index){
	 if (node == null){
		 return index;
	 }
	 index = keysArrayRec(node.left, arr_rec,index);
	 arr_rec[index] = node.key;
	 index++;
	 index = keysArrayRec(node.right, arr_rec,index);
	 return index;
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
	 int index;
     index = infoArrayRec(this.root, arr,0);
     return arr;               
 }
 
//------------------------------------infoToArrayRec------------------------------------------//
 

public int infoArrayRec(WAVLNode node, String[] arr_rec, int index){
	 if (node == null){
		 return index;
	 }
	 index = infoArrayRec(node.left, arr_rec,index);
	 arr_rec[index] = node.info;
	 index++;
	 index = infoArrayRec(node.right, arr_rec,index);
	 return index;
}


//-------------------------------------------------------------------------------------\\

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
 }
 //-----------------------------------------------------------------------------------------//
 


 
//------------------------------------MainFunction----------------------------------------------

  public static void main (String[] args){
	
	 
	  WAVLTree b = new WAVLTree();
		Random rand = new Random();
		String s="";
		
		for (int i=0;i<1;i++) //tree amount
		{
			int minisert = 0;
			int maxkey = 30000;
			int actions = 30;
			
			b = new WAVLTree();			
			int k=minisert+rand.nextInt(actions); //action amount
			
			List<Integer> list = new ArrayList<Integer>();
			for(int j=0;j<k;j++){        //each action
				boolean insert;
				if( j < minisert){
					insert = true;
				}
				else{
					insert = rand.nextBoolean();
				}
				if(insert){
					int n=rand.nextInt(maxkey);
					b.insert(n, "");
					if(list.contains(n)){
						j--;
						continue;
					}
					else{
					list.add(n);
					}
				}
				else if(!insert){
					if (b.empty()){
						j--;
						continue;
					}
					else{
						Integer deleteItem = rand.nextInt(list.size());
						b.delete(list.get(deleteItem));
						list.remove(deleteItem);
					}
				}
			}
			if(b.isWAVL(b.root)){
				if(i%10 == 0){
					System.out.println("tree number: "+i+" is WAVL"+"has "+k+" nodes");
					System.out.println(s);
				}
			}
			else{
				System.out.println("tree number: "+i+" is NOT WAVL"+"has "+k+" nodes");
				return;			
			}
		}
		WAVLTree yourMother = new WAVLTree();
		yourMother.insert(6, "");
		yourMother.insert(7, "");
		yourMother.insert(8, "");
		yourMother.insert(9, "");
		yourMother.insert(10, "");

		System.out.println(Arrays.toString(b.infoToArray()));
		System.out.println(Arrays.toString(b.keysToArray()));
	
		System.out.println("yeah");		
	  }
		
 

	public static boolean isWAVL(WAVLNode node){
		  if(node == null){
			  return true;
		  }
		  else if ((node.left==null && node.right==null) && !(rankLeft(node) == 1 && rankRight(node) ==1)){
			  return false;
		  }
		  else if((rankLeft(node) == 1 && rankRight(node) ==2)  || (rankLeft(node) == 2 && rankRight(node) ==1) || 
				  (rankLeft(node) == 1 && rankRight(node) ==1) || (rankLeft(node) == 2 && rankRight(node) ==2)){
			  return (isWAVL(node.left) && isWAVL(node.right));
		  }
		  return false;
	}
	
 }
