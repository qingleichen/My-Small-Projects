
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] data1 = {3,12,5,32,53,1,42,4,7,9, 16, 12}; 
		int[] data2 = {3,12,5,32,53,1,42,4,7,9, 16, 12};
		int[] data3 = {3,12,5,32,53,1,42,4,7,9, 16, 12};
		display(data1);
		bubbleSort(data1);
		display(data1);
		
		mergeSort(data2, 0, data2.length-1);
		display(data2);
		
		quickSort(data3, 0, data3.length-1);
		display(data3);
		
		String s = "11111";
		encodeMutationNum(s);
		
	}
	
	static void bubbleSort(int[] nums){
		int l = nums.length;
		for(int i=0; i<l-1; i++){
			for(int j=0; j<l-i-1;j++){
				if(nums[j]>nums[j+1]){
					int temp = nums[j];
					nums[j]=nums[j+1];  
					nums[j+1]=temp;
				}
			}
		}
	}
	
	static void mergeSort(int[] nums, int s, int e){
		if(s == e){
			return;
		}else{
			mergeSort(nums, s, (int)(s+(e-s)/2));
			mergeSort(nums, (int)(s+(e-s)/2)+1, e);
			merge(nums, s, e);
		}
	}
	
	static void merge(int[] nums, int s, int e){
		int[] temp = new int[e-s+1];
		int mid = s+(e-s)/2+1;
		int h1 = s, h2 = mid;
		int pos = 0;
		while(h1<mid || h2<=e){
			if(h1<mid){
				if(h2<=e){
					if(nums[h1]<=nums[h2]){
						temp[pos] = nums[h1];
						h1++;
					}else{
						temp[pos] = nums[h2];
						h2++;
					}
				}else{
					temp[pos] = nums[h1]; h1++;
				}
			}else{
				temp[pos] = nums[h2]; h2++;
			}
			pos++;
		}
		for(int i=0; i<e-s+1; i++){
			nums[s+i] = temp[i];
		}
	}
	
	static void display(int[] nums){
		for(int i: nums){
			System.out.printf("%4d,", i);
		}
		System.out.println();
	}
	
	static void quickSort(int[] nums, int s, int e){
		if(s>=e){
			return;
		}
		if(s == e-1){
			if(nums[s] > nums[e]){
				int temp = nums[s];
				nums[s] = nums[e];
				nums[e] = temp;
			}
			return;
		}
		int mid = nums[e];
		int l=s, r=e;
		while(l<r){
			if(nums[l]<=mid){
				l++;
			}else{
				nums[r] = nums[l];
				nums[l] = nums[r-1];
				r--;
			}
		}
		nums[l] = mid;
		quickSort(nums, s, l-1);
		quickSort(nums, l+1, e);
	}
	
	
	static int[][] encodeMutationNum(String s){
	    int l = s.length();
	    int[] nums = new int[l];
	    //convert to an array integer
	    for(int i=0; i<l; i++){
	        nums[i] = s.charAt(i)-'0';
	        System.out.printf("%3d,", nums[i]);
	        System.out.println();
	    }
	    //dp matrix
	    int[][] dp = new int[l][l];
	    for(int i=1; i<=l; i++){
	        for(int j=0; j<=l-i; j++){
	            if(i==1){
	               dp[i-1][j] = 1;
	             }else if(i==2){
	                 if(nums[j]!=0 && (nums[j]*10 + nums[j+1]<=25)){
	                      dp[i-1][j] = 2;
	                 }else{
	                      dp[i-1][j] = 1;
	                 }
	             }else{
	                  dp[i-1][j] = dp[i-2][j+1] + dp[i-3][j+2]*(dp[1][j]-1);                            
	             }
	        }
	    }
	    for(int i=0; i<l; i++){
	    	for(int j=0; j<l; j++){
	    		System.out.printf("%3d,", dp[i][j]);
	    	}
	    	System.out.println();
	    }
	    
	    return dp;
	}

}
