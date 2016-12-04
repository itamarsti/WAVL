import java.util.Arrays;

public class tests {
	public static void main(String[] args){
		int[]arr = new int[4];
		//System.out.println(Arrays.toString(arr));
		String yoav = "123 5 8 9 ";
		String[] str_arr = yoav.split(" ");
		//int yoel = Integer.parseInt(yoav);
		for (int i=0; i<arr.length;i++){
        	arr[i] = Integer.parseInt(str_arr[i]);
		
		}
		System.out.println(Arrays.toString(arr));
	}
}
