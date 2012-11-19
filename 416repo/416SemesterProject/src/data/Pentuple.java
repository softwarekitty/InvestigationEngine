package data;

public class Pentuple<A, B,C,D,E> {
	private A first;
	private B second;
	private C third;
	private D fourth;
	private E fifth;

	public Pentuple(A first, B second,C third,D fourth,E fifth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public void setSecond(B second) {
		this.second = second;
	}
	
	public void setThird(C third){
		this.third=third;
	}
	
	public void setFourth(D fourth) {
		this.fourth = fourth;
	}
	
	public void setFifth(E fifth) {
		this.fifth = fifth;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}
	
	public C getThird(){
		return third;
	}

	public D getFourth() {
		return fourth;
	}

	public E getFifth() {
		return fifth;
	}
}
