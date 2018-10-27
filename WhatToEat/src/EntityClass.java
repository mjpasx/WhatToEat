
public class EntityClass {
	
	private String word;
	private double sentiment;
	private String review;
	
	public EntityClass()
	{
		
	}
	
	public EntityClass(String w, double sen, String rev)
	{
		this.word = w;
		this.sentiment = sen;
		this.review = rev;
	}
	
	public void SetWord(String w)
	{
		this.word = w;
	}
	
	public void SetSentiment(double sen)
	{
		this.sentiment = sen;
	}
	
	public void SetReview(String rev)
	{
		this.review = rev;
	}
	
	public String GetWord()
	{
		return this.word;
	}
	
	public double GetSentiment()
	{
		return this.sentiment;
	}
	
	public String GetReview()
	{
		return this.review;
	}

}
