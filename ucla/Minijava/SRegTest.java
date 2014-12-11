class SRegTest{
	public static void main(String [] a){
		System.out.println(new SRT().Start());
	}
}

class SRT{
	public int Start(){
		int t0; 
		int t1;
		int t2;
		int t3;
		int t4;
		int t5;
		int t6;
		int t7;
		int t8;
		int t9;
		int s0;
		int s1;
		int sum;
		
		t0 = 0;
		t1 = 1;
		t2 = 2;
		t3 = 3;
		t4 = 4;
		t5 = 5;
		t6 = 6;
		t7 = 7;
		t8 = 8;
		t9 = 9;
		s0 = 10;
		s1 = 11;
		
		sum = t0;
		sum = sum + t1;
		sum = sum + t2;
		sum = sum + t3;
		sum = sum + t4;
		sum = sum + t5;
		sum = sum + t6;
		sum = sum + t7;
		sum = sum + t8;
		sum = sum + t9;
		sum = sum + s0;
		sum = sum + s1;
		
		t0 = sum - t0;
		t1 = sum - t1;
		t2 = sum - t2;
		t3 = sum - t3;
		t4 = sum - t4;
		t5 = sum - t5;
		t6 = sum - t6;
		t7 = sum - t7;
		t8 = sum - t8;
		t9 = sum - t9;
		s0 = sum - s0;
		s1 = sum - s1;
		
		return sum;
		
	}
}
