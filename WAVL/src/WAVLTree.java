/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */
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
	WAVLNode cur = this.root;
	while(cur !=null){
		int key = cur.key;
		if (key==k){
			return cur.info;
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
					cur = add;
					add.parent = tmp;
					return rebalance(add);         //****need to work on****
				}
				else{
					continue;			//continue to the next loop
				}
			}
			else{
				WAVLNode tmp = cur;
				cur = cur.right;
				if (cur == null){        //place to be added found
					cur.right = add;
					add.parent = tmp;
					return rebalance(add);     //****need to work on****
				}
				else{
					continue;
				}
			}
		}
	   return 0; //should not reach here
 	}

 
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
	   WAVLNode cur = this.root;
	   WAVLNode next = cur;
		while(cur !=null){
			int key = cur.key;
			//if key is in tree
			if (key == k){
				int parkey = cur.parent.key;
				
				return -1;
			}
			else if(key > k){
				//look left
				next = cur.left;
				if (next == null){
					return -1;
				}
				else if(next.key == k){
					cur.left = null;
					return rebalance(cur);
				}
				else{
					cur = next;
				}
			}
			else{
				//look right
				next = cur.right;
				if (next == null){
					return -1;
				}
				else if(next.key == k){
					cur.right = null;
					return rebalance(cur);
				}
				else{
					cur = next;
				}
			}
		}
		//if tree is empty
		return -1;
 }
 //----------------------------------------------Rebalance-------------------------------
 
 	private int rebalance(WAVLNode node){
 		node.rank = 0; //can delete later if new node default rank is 0
 		int cnt = 0;
 		WAVLNode parent = node.parent;
 		int pa_rank = parent.rank;
 		if (pa_rank==1){							//if Case B
 			return cnt;
 			}
 		else{										//if Case A: both node.parent kids were null
 			pa_rank ++;
 			//if case 1....
 			cnt = rebalance_rec(parent, 0); //recursive rebalancing calls (calls helper func)
 			//if case 2
 		}
 		
   return cnt;
	   
 } //end of main rebalance func
 	
 	
 	private int rebalance_rec(WAVLNode node, int cnt){
 		if (node == this.root){ //end condition : reached root
 			return cnt;
 		}
 		int check = Case(node); //check which case needs to be handled (calls helper func)
 		switch (check){
 			case 0:
 				return cnt;
 			case 1:
 				node.parent.rank ++;				//promote parent and continue rebalancing
 				cnt++;
 				rebalance_rec(node.parent, cnt);		
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
 			case 5:
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
 		
 	
 	private int Case(WAVLNode node){
 		WAVLNode parent = node.parent;
 		if (RankLeft(parent)==0 && RankRight(parent)==1){
 			return 1;
 		}
 		else if (RankLeft(parent)==1 && RankRight(parent)==0){ //mirror image of case 1
 			return 1; 
 		}
 		else { // final rebalances
 			if (RankRight(parent)==2){
 				if (RankRight(node) == 2){
 					return 2;
 				}
 				else if(RankLeft(node) == 2){
 					return 3;
 				}
 			}
 			else if (RankLeft(parent)==2){ //mirror images
 				if (RankRight(node) == 2){
 					return 6; // mirror image of 3
 				}
 				else if(RankLeft(node) == 2){
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
 	
 	
	
	private int RankRight(WAVLNode node){ //input is parent node (no leaves)
		if (node.right == null){ //is a "-1" node
			return 2;
		}
		return Math.abs(node.rank-node.right.rank);
	}//end of right rank checker helper func
	
	private int RankLeft(WAVLNode node){ //input is parent node (no leaves)
		if (node.left == null){ //is a "-1" node
			return 2; 
		}
		return Math.abs(node.rank-node.left.rank);
	}// end of left rank checker helper func
		
 	
   /**
    * public String min()
    *
    * Returns the iîfo of the item with the smallest key in the tree,
    * or null if the tree is empty
    */

 
//------------------------------------MinimumValue----------------------------------------------

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
	  WAVLTree tree = new WAVLTree();
	  WAVLNode yosi =tree.new WAVLNode(1, "2"); 
	  yosi.rank = -1;
	  System.out.println(yosi.key);
	  System.out.println(yosi.info);
	  System.out.println(yosi.rank);
	  System.out.println(tree.root);
  }

}
  
