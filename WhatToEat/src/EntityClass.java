
public class EntityClass {
	
	private String word;
	private double sentiment;
	private String review;
	private String restName;
	private String zipCode;
	private String user;
	private String time;
	
	public EntityClass()
	{
		
	}
	
	public EntityClass(String w, double sen, String rev, String rest, String code, String name, String t)
	{
		this.word = w;
		this.sentiment = sen;
		this.review = rev;
		this.restName = rest;
		this.zipCode = code;
		this.user = name;
		this.time = t;
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
	
	public void SetRestName(String name)
	{
		this.restName = name;
	}
	
	public void SetZipCode(String code)
	{
		this.zipCode = code;
	}
	
	public void SetName(String name)
	{
		this.user = name;
	}
	
	public void SetTime(String t)
	{
		this.time = t;
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
	
	public String GetRestName()
	{
		return this.restName;
	}
	
	public String GetZipCode()
	{
		return this.zipCode;
	}
	
	public String GetUser()
	{
		return this.user;
	}
	
	public String GetTime()
	{
		return this.time;
	}

}
