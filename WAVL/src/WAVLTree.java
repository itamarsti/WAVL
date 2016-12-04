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
	int height;
	int depth;
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
	   while(cur!=null){          
			if (cur.key == k){           //if key is in tree
				return -1;
			}
			else if(k<cur.key){
				cur = cur.left;
				if (cur == null){        //place to be added found
					cur = add;
					add.parent = cur.parent;
					return rebalance(add,0);         //****need to work on****
				}
				else{
					continue;			//continue to the next loop
				}
			}
			else{
				cur = cur.right;
				if (cur == null){        //place to be added found
					cur.right = add;
					add.parent = cur.parent;
					return rebalance(add,0);     //****need to work on****
				}
				else{
					continue;
				}
			}
		}
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
					return rebalance(cur,0);
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
					return rebalance(cur,0);
				}
				else{
					cur = next;
				}
			}
		}
		//if tree is empty
		return -1;
 }
 
 private int rebalance(WAVLNode node,int cnt){
	   WAVLNode parent = node.parent;
	   int parank = parent.rank;
	   if (parank == 0){
		   parent.rank = 1;
		   cnt += 1;
//		   this.root.rebalance(parent);
	   }
	   
	   return cnt;
	   
 }


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
	  int rank;
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
  
